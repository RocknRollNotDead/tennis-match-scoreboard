package ru.codeportfolio.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.codeportfolio.DTO.CurrencyDto;
import ru.codeportfolio.models.Player;

import java.util.List;

@Mapper
public interface CurrencyMapper {
    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    //@Mapping(source = "fullName", target = "name")
    CurrencyDto toDto(Player player);


    List<CurrencyDto> toDtoList(List<Player> currencies);

    Player toModel(CurrencyDto dto);
}
