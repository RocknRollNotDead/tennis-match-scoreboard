package ru.codeportfolio.DTO.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.codeportfolio.DTO.MatchesResponseDto;
import ru.codeportfolio.models.entities.Match;

import java.util.ArrayList;
import java.util.List;


public class MatchesMapper {
    MatchesMapper INSTANCE = Mappers.getMapper(MatchesMapper.class);

    private static final int SIZE_PAGE = 5;
/*
    //@Mapping(source = "fullName", target = "name")
    MatchesResponseDto toDto(List<Match> matches, Integer currentPage, Integer totalPages){
        int pages = matches.size();

        List<Match> result = new ArrayList<>();

        for (int i = 0; i < SIZE_PAGE; i++) {
            result.add(matches.get(currentPage * SIZE_PAGE + i));
        }

        return new MatchesResponseDto(result, currentPage, totalPages);


    }*/


}
