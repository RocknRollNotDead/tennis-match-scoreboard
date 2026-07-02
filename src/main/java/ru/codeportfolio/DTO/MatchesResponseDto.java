package ru.codeportfolio.DTO;

import ru.codeportfolio.models.entities.Match;

import java.util.List;

public record MatchesResponseDto(
    List<MatchDto> matches,
    Integer currentPage,
    Integer totalPages)
{
}
