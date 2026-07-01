package ru.codeportfolio.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.codeportfolio.DTO.ExchangeRateDto;
import ru.codeportfolio.models.Match;

import java.util.List;

@Mapper(uses = CurrencyMapper.class)


public interface ExchangeRateMapper {

    ExchangeRateMapper INSTANCE = Mappers.getMapper(ExchangeRateMapper.class);
    ExchangeRateDto toDto(Match match);

    List<ExchangeRateDto> toDtoList(List<Match> matches);

    Match toModel(ExchangeRateDto dto);
}
