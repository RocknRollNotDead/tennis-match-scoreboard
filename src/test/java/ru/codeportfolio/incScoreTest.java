package ru.codeportfolio;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import ru.codeportfolio.DTO.OneMatchDto;
import ru.codeportfolio.DTO.requestDto.CreateMatchRequestDto;
import ru.codeportfolio.controllers.MatchesController;
import ru.codeportfolio.db.MatchesDao;
import ru.codeportfolio.db.PlayersDao;
import ru.codeportfolio.models.entities.Match;
import ru.codeportfolio.models.entities.Player;
import ru.codeportfolio.services.MatchesService;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
добавление очка
два AD - сброс в 40
6 с разницей в 2 очка - добавление очка сета
7 с разницей в 2 очка - добавления очка сета
6-6 - тайбрейк
тайбрейк - 7 очков добавляют сет
2 выигранных сета заканчивают игру
и попадаюет в таблицу



 */
public class incScoreTest {

    MatchesController controller  = new MatchesController();

    public static final String PLAYER_1 = "aaaaTest";
    public static final String PLAYER_2 = "bbbbTest";

    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/tennis_scoreboard");
        config.setUsername("postgres");
        config.setPassword(Config.getPassword());
        config.setDriverClassName("org.postgresql.Driver");

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(30000);

        return new HikariDataSource(config);
    }

    public SessionFactory sessionFactory(DataSource dataSource) {
        Map<String, Object> settings = new HashMap<>();

        settings.put("hibernate.connection.datasource", dataSource);
        settings.put("hibernate.show_sql", true);
        settings.put("hibernate.hbm2ddl.auto", "update");

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(settings)
                .build();

        MetadataSources sources = new MetadataSources(registry);
        sources.addAnnotatedClass(Match.class);
        sources.addAnnotatedClass(Player.class);

        Metadata metadata = sources.getMetadataBuilder().build();
        return metadata.getSessionFactoryBuilder().build();
    }


    PlayersDao playersDao = new PlayersDao(sessionFactory(dataSource()));
    MatchesDao matchesDao = new MatchesDao(sessionFactory(dataSource()));


    MatchesService service  = new MatchesService(matchesDao, playersDao);

    UUID uuid;

// тестируем не контроллер, а сервис, потому что контроллер отдаёт ResponseBody
    @BeforeEach
    public void addMatch(){
        uuid = service.createMatch(PLAYER_1, PLAYER_2);
    }

    @Test
    public void incScore(){
        service.incPoint(uuid.toString(), PLAYER_1);
        String result = service.findMatch(uuid.toString()).firstPlayer().points();
        assertEquals("15", result);
    }
    @Test
    public void incScoreTo40(){
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
        service.incPoint(uuid.toString(), PLAYER_1);
        service.incPoint(uuid.toString(), PLAYER_1);
        service.incPoint(uuid.toString(), PLAYER_1);
        service.incPoint(uuid.toString(), PLAYER_1);

        int result = service.findMatch(uuid.toString()).firstPlayer().games();
        assertEquals(1, result);
    }

    @Test
    public void incSetFrom6games(){
        inc1Game(PLAYER_1);
        inc1Game(PLAYER_1);
        inc1Game(PLAYER_1);
        inc1Game(PLAYER_1);
        inc1Game(PLAYER_1);
        inc1Game(PLAYER_1);

        int result = service.findMatch(uuid.toString()).firstPlayer().sets();
        assertEquals(1, result);
    }

    @Test
    public void incSetFrom7games(){
        inc1Game(PLAYER_1);
        inc1Game(PLAYER_1);
        inc1Game(PLAYER_1);
        inc1Game(PLAYER_1);
        inc1Game(PLAYER_1);

        inc1Game(PLAYER_2);
        inc1Game(PLAYER_2);
        inc1Game(PLAYER_2);
        inc1Game(PLAYER_2);
        inc1Game(PLAYER_2);

        inc1Game(PLAYER_1);
        inc1Game(PLAYER_1);

        int result = service.findMatch(uuid.toString()).firstPlayer().sets();
        assertEquals(1, result);
    }

    @Test
    public void getTieBreak(){
        inc1Game(PLAYER_1);
        inc1Game(PLAYER_1);
        inc1Game(PLAYER_1);
        inc1Game(PLAYER_1);
        inc1Game(PLAYER_1);

        inc1Game(PLAYER_2);
        inc1Game(PLAYER_2);
        inc1Game(PLAYER_2);
        inc1Game(PLAYER_2);
        inc1Game(PLAYER_2);

        inc1Game(PLAYER_1);
        inc1Game(PLAYER_2);

        Integer result = service.findMatch(uuid.toString()).firstPlayer().tieBreakPoints();
        assertEquals(0, result);

    }

    @Test
    public void getGameFromTable(){
        inc1Game(PLAYER_1);
        inc1Game(PLAYER_1);
        inc1Game(PLAYER_1);
        inc1Game(PLAYER_1);
        inc1Game(PLAYER_1);
        inc1Game(PLAYER_1);

        inc1Game(PLAYER_1);
        inc1Game(PLAYER_1);
        inc1Game(PLAYER_1);
        inc1Game(PLAYER_1);
        inc1Game(PLAYER_1);
        inc1Game(PLAYER_1);

        boolean result = service.getAllMatches(1, PLAYER_1).matches()
                .contains(new OneMatchDto(PLAYER_1, PLAYER_2, PLAYER_1));
        assertTrue(result);
    }







    private void inc40ToAll(){
        service.incPoint(uuid.toString(), PLAYER_1);
        service.incPoint(uuid.toString(), PLAYER_1);
        service.incPoint(uuid.toString(), PLAYER_1);
        service.incPoint(uuid.toString(), PLAYER_2);
        service.incPoint(uuid.toString(), PLAYER_2);
        service.incPoint(uuid.toString(), PLAYER_2);
    }

    private void inc1Game(String player){
        service.incPoint(uuid.toString(), player);
        service.incPoint(uuid.toString(), player);
        service.incPoint(uuid.toString(), player);
        service.incPoint(uuid.toString(), player);
    }

}
