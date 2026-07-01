package ru.codeportfolio.DTO;

import ru.codeportfolio.models.Match;

import java.util.List;

public record MatchesResponseDto(
    List<Match> matches,
    Integer currentPage,
    Integer totalPages)
{
}
