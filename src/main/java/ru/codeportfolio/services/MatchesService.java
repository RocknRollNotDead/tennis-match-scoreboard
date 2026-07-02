package ru.codeportfolio.services;

import org.springframework.stereotype.Service;
import ru.codeportfolio.DTO.MatchDto;
import ru.codeportfolio.DTO.MatchesResponseDto;
import ru.codeportfolio.DTO.ResponseDto;
import ru.codeportfolio.DTO.ToDtoUtil;
import ru.codeportfolio.db.MatchesDao;
import ru.codeportfolio.db.PlayersDao;
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

    private static Map<UUID, Score> scores = new ConcurrentHashMap<>();


    public UUID createMatch(String firstPlayerName, String secondPlayerName) {
        // создать новый объект класса match или score.
        // Score score = createScore(firstPlayerName, secondPlayerName)
        // UUID uuid = generateUUID(score);
        // scores.put(uuid, score)
        // return uuid;
        return null;
    }

    public ResponseDto incPoint(UUID uuid, String playerName) {
        return null;
    }

    public ResponseDto findMatch(UUID uuid) {
        return null;
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
        List <MatchDto> matchesDto = ToDtoUtil.toMatchDtoList(matches);

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


    private Score createScore() {
        // создаваться начальная стадия объекта
        return null; // вернуть обьект score
    }

    private UUID generateUUID(Score score) {

        return null;
    }


}
