package ru.codeportfolio.controllers.other;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.codeportfolio.Config;
import ru.codeportfolio.models.entities.Match;
import ru.codeportfolio.models.entities.Player;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = { "ru.codeportfolio.services", "ru.codeportfolio.db" })
public class MatchesRootConfig {


    @Bean
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

    @Bean
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

    @Bean
    public Gson getGson(){
        return new GsonBuilder().serializeNulls().create();
    }



}
