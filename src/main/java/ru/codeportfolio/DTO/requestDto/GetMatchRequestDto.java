package ru.codeportfolio.DTO.requestDto;

public class GetMatchRequestDto {
    private final String name;

    public GetMatchRequestDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
