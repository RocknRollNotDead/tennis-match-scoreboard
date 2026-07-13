package ru.codeportfolio.services;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import ru.codeportfolio.DTO.MatchesResponseDto;
import ru.codeportfolio.DTO.OneMatchDto;
import ru.codeportfolio.DTO.ScoreResponseDto;
import ru.codeportfolio.DTO.mapper.ToDtoUtil;
import ru.codeportfolio.db.MatchesDao;
import ru.codeportfolio.db.PlayersDao;
import ru.codeportfolio.exceptions.NotFoundException;
import ru.codeportfolio.exceptions.ValidationException;
import ru.codeportfolio.models.entities.Match;
import ru.codeportfolio.models.entities.Player;
import ru.codeportfolio.models.TennisMatch;
import ru.codeportfolio.validators.PlayerValidateUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MatchesService {
    private final MatchesDao matchesDao;
    private final PlayersDao playersDao;
    private static final int SIZE_PAGE = 5;

    private final Cache<UUID, TennisMatch> scores = Caffeine.newBuilder()
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();


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

        TennisMatch score = createScore(firstPlayerName, secondPlayerName);
        UUID uuid = generateUUID();
        scores.put(uuid, score);
        return uuid;
    }

    public ScoreResponseDto incPoint(String uuid, String playerName) {
        UUID id;
        try {
            id = UUID.fromString(uuid);
        } catch (RuntimeException e) {
            throw new ValidationException("Uncorrected uuid in request", e);
        }

        TennisMatch score = scores.getIfPresent(id);
        if (score == null) {
            throw new NotFoundException("Not found match!");
        }

        synchronized (score) {
            if (score.getWinner() != null) {
                throw new ValidationException("Match was finished");
            }

            score.incPoint(playerName);
            if (score.getWinner() != null) {
                saveMatch(score);
                scores.invalidate(id);
            }
        }

        return score.getScore();
    }


    public ScoreResponseDto findMatch(String uuid) {
        UUID id;
        try {
            id = UUID.fromString(uuid);
        } catch (RuntimeException e) {
            throw new ValidationException("Uncorrected uuid in request", e);
        }
        TennisMatch score;

        score = scores.getIfPresent(id);
        if (score == null){
            throw new NotFoundException("not find score " + uuid);
        }

        return score.getScore();
    }

    public MatchesResponseDto getAllMatches(Integer page, String playerName) {

        List<Match> matches;

        int offset = calculateOffset(page);
        matches = getMatchesFromDao(playerName, offset, offset + SIZE_PAGE);

        long totalPages = calculateTotalPages(playerName);

        List<OneMatchDto> matchesDto = ToDtoUtil.toMatchDtoList(matches);

        if (page == null) {
            page = 1;
        }

        MatchesResponseDto matchesResponseDto = new MatchesResponseDto(matchesDto, page, totalPages);
        return matchesResponseDto;
    }

    private int calculateOffset(Integer page) {
        if (page == null) {
            return 0;
        } else if (page <= 0) {
            return 0;
        }
        return (page - 1) * (SIZE_PAGE);
    }


    private void saveMatch(TennisMatch score) {
        matchesDao.save(
                new Match(
                        playersDao.findByName(score.getScore().firstPlayer().name()).orElse(
                                new Player(score.getScore().firstPlayer().name())),
                        playersDao.findByName(score.getScore().secondPlayer().name()).orElse(
                                new Player(score.getScore().secondPlayer().name())),
                        playersDao.findByName(score.getWinner()).orElse(
                                new Player(score.getWinner()))
                ));
    }

    private @NonNull List<Match> getMatchesFromDao(String playerName, int offset, int limit) {
        List<Match> matches;
        if (playerName == null || playerName.isBlank()) {
            matches = matchesDao.getAll(offset, limit);
        } else {
            playerName = PlayerValidateUtil.normalizeRequest(playerName);
            matches = matchesDao.find(playerName, offset, limit);
        }
        return matches;
    }

    private long calculateTotalPages(String playerName) {
        long matches;
        if (playerName == null || playerName.isBlank()){
            matches = matchesDao.countMatches();
        } else {
            matches = matchesDao.countMatches(playerName);
        }

        return Math.ceilDiv(matches, SIZE_PAGE);
    }

    private List<Match> getMatchesPage(List<Match> matches, Integer page) {
        int firstIndex = page * SIZE_PAGE;

        if (firstIndex >= matches.size() || page < 0) {
            return Collections.emptyList();
        }

        int lastIndex = Math.min(firstIndex + SIZE_PAGE, matches.size());
        return matches.subList(firstIndex, lastIndex);
    }


    private TennisMatch createScore(String firstPlayerName, String secondPlayerName) {
        Player homePlayer = playersDao.findByName(firstPlayerName).orElseGet(
                () -> playersDao.save(new Player(firstPlayerName)));

        Player guestPlayer = playersDao.findByName(secondPlayerName).orElseGet(
                () -> playersDao.save(new Player(secondPlayerName)));

        return new TennisMatch(homePlayer.getName(), guestPlayer.getName());
    }

    private UUID generateUUID() {
        return UUID.randomUUID();
    }


}
