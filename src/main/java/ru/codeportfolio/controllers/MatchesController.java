package ru.codeportfolio.controllers;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.codeportfolio.DTO.MatchesResponseDto;
import ru.codeportfolio.DTO.ResponseDto;
import ru.codeportfolio.db.MatchesDao;
import ru.codeportfolio.db.MatchesDaoInterface;
import ru.codeportfolio.models.Match;
import ru.codeportfolio.services.MatchesService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/matches")
public class MatchesController {
    SessionFactory factory = new Configuration()
            .configure("hibernate.cfg.xml")
            .buildSessionFactory();
    MatchesService service = new MatchesService();

    @PostMapping
    public ResponseEntity<String> createMatch(@RequestBody String firstPlayerName, @RequestBody String secondPlayerName){

        String id = service.createMatch(firstPlayerName, secondPlayerName);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);// должен вернуть хэш id

    }

    @PostMapping("/{uuid}/point")
    public ResponseEntity<ResponseDto> incPoint(@PathVariable UUID uuid, @RequestBody String playerName){

        return ResponseEntity.ok(service.incPoint(uuid, playerName));

    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ResponseDto> getScore(@PathVariable UUID uuid){
        ///
        ResponseDto responseDto = service.findMatch(uuid);
        return ResponseEntity.ok(responseDto);
    }




    @GetMapping
    public ResponseEntity<MatchesResponseDto> getMatches(){

        MatchesResponseDto matches = service.getAllMatches();
        return ResponseEntity.ok(matches);

    }



}
