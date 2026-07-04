package ru.codeportfolio;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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

/*
добавление очка
два AD - сброс в 40
6 с разницей в 2 очка - добавление очка сета
7 с разницей в 2 очка - добавления очка сета
6-6 - тайбрейк
тайбрейк - 7 очков добавляют сет
2 выигранных сета заканчивают игру
законченная игра попадает в таблицу



 */
public class incScoreTest {

    MatchesController controller  = new MatchesController();

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
    public void addMatch(){
        CreateMatchRequestDto dto = new CreateMatchRequestDto("AAA", "bbbb");
        uuid = service.createMatch("РазПроверка", "ДваПроверка");
    }

    @Test
    public void incScore(){
        service.incPoint(uuid.toString(), "Разпроверка");
    }

    public void incScoreTo40(){

    }

    public void incScoreToAD(){

    }

    public void incGame(){

    }

    public void incSetFrom6games(){

    }
    public void incSetFrom7games(){

    }


    public void getTieBreak(){

    }


}
