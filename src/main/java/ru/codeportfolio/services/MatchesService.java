package ru.codeportfolio.services;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import ru.codeportfolio.DTO.*;
import ru.codeportfolio.DTO.mapper.ToDtoUtil;
import ru.codeportfolio.db.MatchesDao;
import ru.codeportfolio.db.PlayersDao;
import ru.codeportfolio.exceptions.AlreadyExistException;
import ru.codeportfolio.exceptions.NotFoundException;
import ru.codeportfolio.exceptions.ValidationException;
import ru.codeportfolio.models.entities.Match;
import ru.codeportfolio.models.entities.Player;
import ru.codeportfolio.models.score.Score;
import ru.codeportfolio.validators.PlayerValidateUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MatchesService {
    private final MatchesDao matchesDao;
    private final PlayersDao playersDao;
    private static final int SIZE_PAGE = 5;

    private final Map<UUID, Score> scores = new ConcurrentHashMap<>();

    public MatchesService(MatchesDao matchesDao, PlayersDao playersDao) {
        this.matchesDao = matchesDao;
        this.playersDao = playersDao;
    }

    public UUID createMatch(String firstPlayerName, String secondPlayerName) {
        firstPlayerName = PlayerValidateUtil.normalizeRequest(firstPlayerName);
        secondPlayerName = PlayerValidateUtil.normalizeRequest(secondPlayerName);

        if (firstPlayerName.equals(secondPlayerName)) {
            throw new ValidationException("you can't play with yourself!");
        }

        Score score = createScore(firstPlayerName, secondPlayerName);
        UUID uuid = generateUUID();
        scores.put(uuid, score);
        return uuid;
    }

    public ScoreResponseDto incPoint(String uuid, String playerName) {

        UUID id = UUID.fromString(uuid);
        Score score = scores.get(id);
        if (score == null) {
            throw new NotFoundException("Not found match!");
        }
        score.incPoint(playerName);

        if (score.getWinnerName() != null) {
            saveMatch(score);
            scores.remove(id);
        }

        return ToDtoUtil.toResponseDtoFromScore(
                score);
    }


    public ScoreResponseDto findMatch(String uuid) {

        UUID id = UUID.fromString(uuid);
        Score score;
        try {
            score = scores.get(id);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("not find score " + uuid);
        } catch (RuntimeException e) {
            throw e;
        }

        return ToDtoUtil.toResponseDtoFromScore(score);
    }

    public MatchesResponseDto getAllMatches(Integer page, String playerName) {

        List<Match> matches;

        matches = getMatchesFromDao(playerName);

        Integer totalPages = calculateTotalPages(matches);

        page = normalizePage(page);
        matches = getMatchesPage(matches, page);
        page = page + 1;


        List<OneMatchDto> matchesDto = ToDtoUtil.toMatchDtoList(matches);


        MatchesResponseDto matchesResponseDto = new MatchesResponseDto(matchesDto, page, totalPages);
        return matchesResponseDto;
    }


    private void saveMatch(Score score) {
        matchesDao.save(
                new Match(
                        playersDao.findByName(score.getHomePlayerName()).orElse(
                                new Player(score.getHomePlayerName())),
                        playersDao.findByName(score.getGuestPlayerName()).orElse(
                                new Player(score.getGuestPlayerName())),
                        playersDao.findByName(score.getWinnerName()).orElse(
                                new Player(score.getWinnerName()))
                ));
    }

    private @NonNull List<Match> getMatchesFromDao(String playerName) {
        List<Match> matches;
        if (playerName == null || playerName.isBlank()) {
            matches = matchesDao.getAll();
        } else {
            playerName = PlayerValidateUtil.normalizeRequest(playerName);
            matches = matchesDao.find(playerName);

        }
        matches.sort(Comparator.comparing(match -> match.getHomePlayer().getName()));
        return matches;
    }

    private Integer normalizePage(Integer page) {

        if (page == null) {
            page = 0;
        } else {
            page = page - 1;
        }
        return page;

    }

    private Integer calculateTotalPages(List<Match> matches) {
        Integer result;
        result = Math.ceilDiv(matches.size(), SIZE_PAGE);
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
        Player homePlayer = playersDao.findByName(firstPlayerName).orElseGet(
                () -> playersDao.save(new Player(firstPlayerName)));

        Player guestPlayer = playersDao.findByName(secondPlayerName).orElseGet(
                () -> playersDao.save(new Player(secondPlayerName)));

        return new Score(homePlayer.getName(), guestPlayer.getName());
    }

    private UUID generateUUID() {
        return UUID.randomUUID();
    }


}
