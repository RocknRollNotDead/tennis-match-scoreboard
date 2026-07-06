package ru.codeportfolio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.codeportfolio.DTO.MatchesResponseDto;
import ru.codeportfolio.DTO.OneMatchDto;
import ru.codeportfolio.db.MatchesDao;
import ru.codeportfolio.db.PlayersDao;
import ru.codeportfolio.models.entities.Match;
import ru.codeportfolio.models.entities.Player;
import ru.codeportfolio.services.MatchesService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/*
добавление очка
два AD - сброс в 40
6 с разницей в 2 очка - добавление очка сета
7 с разницей в 2 очка - добавления очка сета
6-6 - тайбрейк
тайбрейк - 7 очков добавляют сет
2 выигранных сета заканчивают игру
и попадают в таблицу
 */

@ExtendWith(MockitoExtension.class)
public class IncScoreTest {

    public static final String PLAYER_1 = "aaaaaTest";
    public static final String PLAYER_2 = "bbbbbTest";

    @Mock
    private PlayersDao playersDao;
    @Mock
    private MatchesDao matchesDao;

    @InjectMocks
    MatchesService service;

    UUID uuid;


    @BeforeEach
    public void addMatch(){
        Player player1 = new Player(PLAYER_1);
        Player player2 = new Player(PLAYER_2);

        when(playersDao.findByName(PLAYER_1)).thenReturn(Optional.of(player1));
        when(playersDao.findByName(PLAYER_2)).thenReturn(Optional.of(player2));

        uuid = service.createMatch(PLAYER_1, PLAYER_2);
    }

    @Test
    public void incScore(){

        service.incPoint(uuid.toString(), PLAYER_1);
        String result = service.findMatch(uuid.toString()).firstPlayer().points();
        assertEquals("15", result);
    }
    @Test
    public void incScoreFrom40ToAD(){
        inc40ToAll();
        service.incPoint(uuid.toString(), PLAYER_1);

        String result = service.findMatch(uuid.toString()).firstPlayer().points();
        assertEquals("AD", result);
    }

    @Test
    public void incScoreToAD(){
        inc40ToAll();
        service.incPoint(uuid.toString(), PLAYER_1);
        service.incPoint(uuid.toString(), PLAYER_1);

        int result = service.findMatch(uuid.toString()).firstPlayer().games();
        assertEquals(1, result);
    }

    @Test
    public void incADTo40(){
        inc40ToAll();
        service.incPoint(uuid.toString(), PLAYER_1);
        service.incPoint(uuid.toString(), PLAYER_2);

        String result = service.findMatch(uuid.toString()).firstPlayer().points();
        assertEquals("40", result);

    }

    @Test
    public void incGame(){
        for (int i = 0; i < 4; i++) {
            service.incPoint(uuid.toString(), PLAYER_1);
        }

        int result = service.findMatch(uuid.toString()).firstPlayer().games();
        assertEquals(1, result);
    }

    @Test
    public void incSetFrom6games(){
        for (int i = 0; i < 6; i++) {
            inc1Game(PLAYER_1);
        }

        int result = service.findMatch(uuid.toString()).firstPlayer().sets();
        assertEquals(1, result);
    }

    @Test
    public void incSetFrom7games(){
        for (int i = 0; i < 5; i++) {
            inc1Game(PLAYER_1);
        }

        for (int i = 0; i < 5; i++) {
            inc1Game(PLAYER_2);
        }

        inc1Game(PLAYER_1);
        inc1Game(PLAYER_1);

        int result = service.findMatch(uuid.toString()).firstPlayer().sets();
        assertEquals(1, result);
    }

    @Test
    public void getTieBreak(){

        for (int i = 0; i < 5; i++) {
            inc1Game(PLAYER_1);
        }

        for (int i = 0; i < 5; i++) {
            inc1Game(PLAYER_2);
        }

        inc1Game(PLAYER_1);
        inc1Game(PLAYER_2);

        Integer result = service.findMatch(uuid.toString()).firstPlayer().tieBreakPoints();
        assertEquals(0, result);

    }

    @Test
    public void checkSave(){
        when(playersDao.findByName(PLAYER_1)).thenReturn(Optional.empty());

        for (int i = 0; i < 12; i++) {
            inc1Game(PLAYER_1);
        }

        verify(matchesDao).save(
                new Match(
                        new Player(PLAYER_1),
                        new Player(PLAYER_2),
                        new Player(PLAYER_1)
                        )
        );
    }

    @Test
    public void checkGetPageNull(){
        when(matchesDao.getAll(0, 5)).thenReturn(new ArrayList<>());
        when(matchesDao.countMatches()).thenReturn(0L);

        MatchesResponseDto result = service.getAllMatches(null, "");
        assertEquals(new MatchesResponseDto(
                new ArrayList<>(), 1, 0
        ), result);

    }

    @Test
    public void checkGetPage1(){
        when(matchesDao.getAll(0, 5)).thenReturn(
                new ArrayList<>(List.of(
                        new Match(new Player(PLAYER_1), new Player(PLAYER_2), new Player(PLAYER_1)),
                        new Match(new Player(PLAYER_2), new Player(PLAYER_1), new Player(PLAYER_2))
                        )));
        when(matchesDao.countMatches()).thenReturn(2L);

        MatchesResponseDto result = service.getAllMatches(null, "");
        assertEquals(new MatchesResponseDto(
                new ArrayList<>(List.of(
                        new OneMatchDto(PLAYER_1, PLAYER_2, PLAYER_1),
                        new OneMatchDto(PLAYER_2, PLAYER_1, PLAYER_2)
                )), 1, 1
        ), result);

    }






    private void inc40ToAll(){
        for (int i = 0; i < 3; i++) {
            service.incPoint(uuid.toString(), PLAYER_1);
        }
        for (int i = 0; i < 3; i++) {
            service.incPoint(uuid.toString(), PLAYER_2);
        }
    }

    private void inc1Game(String player){
        for (int i = 0; i < 4; i++) {
            service.incPoint(uuid.toString(), player);
        }
    }

}
