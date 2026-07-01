package ru.codeportfolio.controllers;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.codeportfolio.db.MatchesDao;
import ru.codeportfolio.db.MatchesDaoInterface;
import ru.codeportfolio.models.Match;
import ru.codeportfolio.models.Player;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/matches")
public class MatchesController {
    SessionFactory factory = new Configuration()
            .configure("hibernate.cfg.xml")
            .buildSessionFactory();

    @PostMapping
    public String createMatch(Player player1, Player player2){
        MatchesDaoInterface matchesDao = new MatchesDao(factory);
        Match match = matchesDao.save(player1, player2, player1); // заглушка
        return String.valueOf(match);// должен вернуть хэш id
    }

//    @PostMapping("/{uuid}/point")
//    public Match getOneMatch(@PathVariable UUID uuid){
//        MatchesDaoInterface matchesDao = new MatchesDao(factory);
//        //sdkfdk
//        return matchesDao.find()
//    }

    @GetMapping("/{uuid}")
    public Match getScore(@PathVariable UUID uuid){
        ///
    }




    @GetMapping
    public List<Match> getMatches(){

        MatchesDaoInterface matchesDao = new MatchesDao(factory);
        return matchesDao.getAll();

    }



}
