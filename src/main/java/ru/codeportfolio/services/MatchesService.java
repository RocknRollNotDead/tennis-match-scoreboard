package ru.codeportfolio.services;

import org.springframework.stereotype.Service;
import ru.codeportfolio.DTO.OneMatchDto;
import ru.codeportfolio.DTO.MatchesResponseDto;
import ru.codeportfolio.DTO.ResponseDto;
import ru.codeportfolio.DTO.ToDtoUtil;
import ru.codeportfolio.db.MatchesDao;
import ru.codeportfolio.db.PlayersDao;
import ru.codeportfolio.exceptions.ValidationException;
import ru.codeportfolio.models.entities.Match;
import ru.codeportfolio.models.entities.Player;
import ru.codeportfolio.models.Score;
import ru.codeportfolio.validators.PlayerValidateUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MatchesService {
    private final MatchesDao matchesDao;
    private final PlayersDao playersDao;
    private static final int SIZE_PAGE = 5;

    public MatchesService(MatchesDao matchesDao, PlayersDao playersDao) {
        this.matchesDao = matchesDao;
        this.playersDao = playersDao;
    }

    private static final Map<UUID, Score> scores = new ConcurrentHashMap<>();


    public UUID createMatch(String firstPlayerName, String secondPlayerName) {
        // todo обработать создлание players
        Score score = createScore(firstPlayerName, secondPlayerName);
        UUID uuid = generateUUID();
        scores.put(uuid, score);
        return uuid;
    }

    public ResponseDto incPoint(UUID uuid, String playerName) {

        Score score = scores.get(uuid);
        if (score.getHomePlayer().getName().equalsIgnoreCase(playerName)) {
            score.incHomePlayerPoint();
        } else if (score.getGuestPlayer().getName().equalsIgnoreCase(playerName)) {
            score.incGuestPlayerPoint();
        } else {
            throw new ValidationException("not find player");
        }
        return ToDtoUtil.toResponseDtoFromScore(
                score);
    }

    public ResponseDto findMatch(UUID uuid) {
        return ToDtoUtil.toResponseDtoFromScore(
                scores.get(uuid));
    }

    public MatchesResponseDto getAllMatches(Integer page, String playerName) {

        List<Match> matches;

        if (playerName == null || playerName.isBlank()) {
            matches = matchesDao.getAll();
        } else {
            playerName = PlayerValidateUtil.normalizeRequest(playerName);
            Player player = playersDao.findByName(playerName).orElseThrow();
            matches = matchesDao.find(player);
        }

        Integer totalPages = calculateTotalPages(matches);
        if (page >= 1) {
            page = page - 1;
        } else {
            page = null;
        }

        matches = getMatchesPage(matches, page);
        List<OneMatchDto> matchesDto = ToDtoUtil.toMatchDtoList(matches);

        MatchesResponseDto matchesResponseDto = new MatchesResponseDto(matchesDto, page, totalPages);
        // переделать в дто с помощью MapStruct
        return matchesResponseDto;
    }


    private Integer calculateTotalPages(List<Match> matches) {
        Integer result;
        result = matches.size() / 5;
        if (result == 0) {
            result = null;
        }

        return result;
    }

    private List<Match> getMatchesPage(List<Match> matches, Integer page) {
        int firstIndex = page * SIZE_PAGE;

        if (firstIndex >= matches.size() || page < 0) {
            return Collections.emptyList();
        }

        int lastIndex = Math.min(firstIndex + SIZE_PAGE, matches.size());
        return matches.subList(firstIndex, lastIndex);
    }


    private Score createScore(String firstPlayerName, String secondPlayerName) {
        System.out.println(firstPlayerName + "   " + secondPlayerName);
        Player homePlayer = playersDao.findByName(firstPlayerName).orElseThrow();
        System.out.println(firstPlayerName);
        Player guestPlayer = playersDao.findByName(secondPlayerName).orElseThrow();
        System.out.println(secondPlayerName);

        return new Score(homePlayer, guestPlayer);
    }

    private UUID generateUUID() {
        return UUID.randomUUID();
    }


}
