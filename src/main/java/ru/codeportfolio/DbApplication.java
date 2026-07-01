package ru.codeportfolio;

import ru.codeportfolio.db.MatchesDao;
import ru.codeportfolio.db.MatchesDaoInterface;
import ru.codeportfolio.db.PlayersDao;
import ru.codeportfolio.db.PlayersDaoInterface;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.codeportfolio.models.Match;
import ru.codeportfolio.models.Player;

public class DbApplication {

    public static void main(String[] args) {

        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();

        PlayersDaoInterface playersDao = new PlayersDao(factory);
        MatchesDaoInterface matchesDao = new MatchesDao(factory);

        Player player1 = playersDao.findByName("Иван").orElseThrow();
        Player player2 = playersDao.findByName("Прохор").orElseThrow();




        Match match = matchesDao.find(player2, player1).orElseThrow();

//        for (Match match3 : matchesDao.getAll()) {
//            System.out.println(match3);
//        }

        System.out.println(match);


        factory.close();
    }







}
