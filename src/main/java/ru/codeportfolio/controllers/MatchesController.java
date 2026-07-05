package ru.codeportfolio.controllers;

import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.codeportfolio.DTO.MatchesResponseDto;
import ru.codeportfolio.DTO.ScoreResponseDto;
import ru.codeportfolio.DTO.requestDto.CreateMatchRequestDto;
import ru.codeportfolio.DTO.requestDto.GetMatchRequestDto;
import ru.codeportfolio.services.MatchesService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/matches")
public class MatchesController {
    @Autowired
    private final MatchesService service;

    private final Gson gson;

    public MatchesController(MatchesService service, Gson gson) {
        this.service = service;
        this.gson = gson;
    }

    @PostMapping()
    public ResponseEntity<Map<String, String>> createMatch(@RequestBody CreateMatchRequestDto dto) {
        String firstPlayerName = dto.getFirstPlayerName();
        String secondPlayerName = dto.getSecondPlayerName();
        UUID uuid = service.createMatch(firstPlayerName, secondPlayerName);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", uuid.toString()));
    }

    @PostMapping("/{uuid}/point")
    public ResponseEntity<String> incPoint(@PathVariable(name = "uuid") String uuid,
                                                     @RequestBody GetMatchRequestDto dto) {

        String playerName = dto.getName();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(gson.toJson(
                        service.incPoint(uuid, playerName)));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<String> getScore(@PathVariable(name = "uuid") String uuid) {
        ScoreResponseDto scoreResponseDto = service.findMatch(uuid);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(gson.toJson(scoreResponseDto));
    }


    @GetMapping()
    public ResponseEntity<MatchesResponseDto> getMatches(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "player_name", required = false) String playerName) {
        MatchesResponseDto matches = service.getAllMatches(page, playerName);
        return ResponseEntity.ok(matches);

    }



}
