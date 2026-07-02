package ru.codeportfolio.DTO;

import java.util.List;

public record MatchesResponseDto(
    List<OneMatchDto> matches,
    Integer currentPage,
    Integer totalPages)
{
}
