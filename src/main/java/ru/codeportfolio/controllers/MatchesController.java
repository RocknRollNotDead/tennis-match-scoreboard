package ru.codeportfolio.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.codeportfolio.DTO.MatchesResponseDto;
import ru.codeportfolio.DTO.ResponseDto;
import ru.codeportfolio.DTO.requestDto.CreateMatchRequestDto;
import ru.codeportfolio.DTO.requestDto.GetMatchRequestDto;
import ru.codeportfolio.services.MatchesService;

import java.util.UUID;

@RestController
@RequestMapping("/matches")
public class MatchesController {
    @Autowired
    MatchesService service;


    @PostMapping()
    public ResponseEntity<UUID> createMatch(@RequestBody CreateMatchRequestDto dto) {
        String firstPlayerName = dto.getFirstPlayerName();
        String secondPlayerName = dto.getSecondPlayerName();
        UUID id = service.createMatch(firstPlayerName, secondPlayerName);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PostMapping("/{uuid}/point")
    public ResponseEntity<ResponseDto> incPoint(@PathVariable UUID uuid,
                                                @RequestBody GetMatchRequestDto dto) {

        String playerName = dto.getPlayerName();
        return ResponseEntity.ok(service.incPoint(uuid, playerName));

    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ResponseDto> getScore(@PathVariable UUID uuid) {
        ResponseDto responseDto = service.findMatch(uuid);
        return ResponseEntity.ok(responseDto);
    }


    @GetMapping()
    public ResponseEntity<MatchesResponseDto> getMatches(
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "player_name", required = false) String playerName) {
        MatchesResponseDto matches = service.getAllMatches(page, playerName);
        return ResponseEntity.ok(matches);

    }



}
