package ru.codeportfolio.services;

import ru.codeportfolio.DTO.CurrencyDto;
import ru.codeportfolio.DTO.ExchangeRateDto;
import ru.codeportfolio.db.MatchesDao;
import ru.codeportfolio.db.MatchesDaoInterface;
import ru.codeportfolio.exceptions.*;

import ru.codeportfolio.DTO.ExchangeDto;
import ru.codeportfolio.models.Match;
import ru.codeportfolio.mapper.ExchangeRateMapper;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ExchangeRateService {
    private static final String MAIN_CURRENCY_CODE = "USD";

    private final CurrencyService currencyService;
    private final DataSource dataSource;

    public ExchangeRateService(DataSource dataSource) {

        currencyService = new CurrencyService(dataSource);
        this.dataSource = dataSource;
    }

    public List<ExchangeRateDto> getAllExchangeRates() {
        try (Connection conn = dataSource.getConnection()) {

            MatchesDaoInterface matchesDaoInterface = new MatchesDao(conn);
            return ExchangeRateMapper.INSTANCE.toDtoList(matchesDaoInterface.getAll());
        } catch (SQLException e) {
            throw new DataAccessException("DB error", e);
        }

    }


    public ExchangeRateDto addRate(String baseCurrencyCode, String targetCurrencyCode, String unValidationRate) {

        checkTargetValuesOnCorrectRequest(baseCurrencyCode, targetCurrencyCode);

        BigDecimal rate = validateAndFormatRate(unValidationRate);
        rate = routingRate(rate, 6);


        int baseCurrencyId = currencyService.getIdFromCode(baseCurrencyCode);
        int targetCurrencyId = currencyService.getIdFromCode(targetCurrencyCode);
        // в идеале не делать эти лишние запросы в sql, а добавлять в RatesDao не по ID, а сразу по коду,
        // но тогда это будет не чистый mvc - DAO не должен знать про name, его задача здесь - просто добавить в бд строчку

        int result;

        try (Connection conn = dataSource.getConnection()) {

            MatchesDaoInterface matchesDaoInterface = new MatchesDao(conn);

            try{
//                result = matchesDaoInterface.add(baseCurrencyId, targetCurrencyId);
            } catch (AlreadyExistException e){
                throw new AlreadyExistException(e.getMessage() + ". " + baseCurrencyCode + targetCurrencyCode, e);
            }


            if (result == 0){
                throw new DataAccessException("Failed add");
            }

            return ExchangeRateMapper.INSTANCE.toDto(matchesDaoInterface.findById(baseCurrencyId, targetCurrencyId));
        } catch (SQLException e) {
            throw new DataAccessException("DB error", e);
        }

    }

    public void deleteRate(String baseCurrencyCode, String targetCurrencyCode){

        checkTargetValuesOnCorrectRequest(baseCurrencyCode, targetCurrencyCode);

        int baseCurrencyId = currencyService.getIdFromCode(baseCurrencyCode);
        int targetCurrencyId = currencyService.getIdFromCode(targetCurrencyCode);

        try (Connection conn = dataSource.getConnection()) {

            MatchesDaoInterface matchesDaoInterface = new MatchesDao(conn);
            int result = matchesDaoInterface.delete(baseCurrencyId, targetCurrencyId);
            if (result == 0){
                throw new NotFoundException("Not found");
            }

        } catch (SQLException e) {
            throw new DataAccessException("DB error", e);
        }

    }

    public ExchangeRateDto getRate (String baseCurrencyCode, String targetCurrencyCode){

        checkTargetValuesOnCorrectRequest(baseCurrencyCode, targetCurrencyCode);

        int baseCurrencyId = currencyService.getIdFromCode(baseCurrencyCode);
        int targetCurrencyId = currencyService.getIdFromCode(targetCurrencyCode);


        try (Connection conn = dataSource.getConnection()) {

            MatchesDaoInterface matchesDaoInterface = new MatchesDao(conn);

            Match match = matchesDaoInterface
                    .findById(baseCurrencyId, targetCurrencyId);

            if (match == null){
                throw new NotFoundException("Not found");
            }
            return ExchangeRateMapper.INSTANCE.toDto(match);
        } catch (SQLException e) {
            throw new DataAccessException("DB error", e);
        }

    }

    public ExchangeRateDto changeRate(String baseCurrencyCode, String targetCurrencyCode, String unValidationRate){

        checkTargetValuesOnCorrectRequest(baseCurrencyCode, targetCurrencyCode);

        BigDecimal rate = validateAndFormatRate(unValidationRate);
        rate = routingRate(rate, 6);

        int baseCurrencyId = currencyService.getIdFromCode(baseCurrencyCode);
        int targetCurrencyId = currencyService.getIdFromCode(targetCurrencyCode);

        try (Connection conn = dataSource.getConnection()) {

            MatchesDaoInterface matchesDaoInterface = new MatchesDao(conn);

            int result = matchesDaoInterface.update(baseCurrencyId, targetCurrencyId, rate);

            if (result == 0){
                throw new NotFoundException("Not found");
            }

            Match match = matchesDaoInterface
                    .findById(baseCurrencyId, targetCurrencyId);
            if (match != null){
                return ExchangeRateMapper.INSTANCE.toDto(match);
            } else {
                throw new DataAccessException("Not found");
            }
        } catch (SQLException e) {
            throw new DataAccessException("DB error", e);
        }

    }


    public ExchangeDto calculateRate (String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount){

        checkTargetValuesOnCorrectRequest(baseCurrencyCode, targetCurrencyCode);
        checkValueOnEmptyAndNegative(amount);

        CurrencyDto baseCurrency = currencyService.getCurrency(baseCurrencyCode);
        CurrencyDto targetCurrency = currencyService.getCurrency(targetCurrencyCode);

        int baseCurrencyId = baseCurrency.id();
        int targetCurrencyId = targetCurrency.id();

        BigDecimal rate;


        // vvv Логика расчёта курса, AB, BA, USD-A - USD-B vvv

        try (Connection conn = dataSource.getConnection()) {


            MatchesDaoInterface matchesDaoInterface = new MatchesDao(conn);

            Match match = matchesDaoInterface.findById(baseCurrencyId, targetCurrencyId);

            if (match != null){
                rate = match.winner();
            } else {
                match = matchesDaoInterface.findById(targetCurrencyId, baseCurrencyId);

                if (match != null) {
                    rate = calculateReverseDecimal(match.winner());
                } else {
                    rate = calculateRateFromUsd(matchesDaoInterface, baseCurrencyId, targetCurrencyId);
                }
            }

            if (rate.signum() == 0){
                throw new NotFoundException("Rate not found");
            }

            BigDecimal result = amount.multiply(rate);

            result = routingRate(result, 2);

            return new ExchangeDto(baseCurrency, targetCurrency, rate, amount, result);


        } catch (SQLException e) {
            throw new DataAccessException("DB error", e);
        }

    }


    private BigDecimal getUsdRate(MatchesDaoInterface matchesDaoInterface,
                                  int baseCurrencyId, int mainCurrencyId){
        BigDecimal result;
        Match firstRate = matchesDaoInterface.findById(baseCurrencyId, mainCurrencyId);
        if (firstRate != null){
            return firstRate.winner();
        }

        Match secondRate = matchesDaoInterface.findById(mainCurrencyId, baseCurrencyId);
        if (secondRate != null){
            return calculateReverseDecimal(secondRate.winner());
        }

        throw new NotFoundException("Rate " + baseCurrencyId + "-USD not found!");

    }

    private BigDecimal calculateRateFromUsd(MatchesDaoInterface matchesDaoInterface,
                                            int currencyId, int targetCurrencyId){
        int mainCurrencyId = currencyService.getIdFromCode(MAIN_CURRENCY_CODE);

        BigDecimal USDRateBase;
        BigDecimal USDRateTarget;

        USDRateBase = getUsdRate(matchesDaoInterface, currencyId, mainCurrencyId);

        USDRateTarget = getUsdRate(matchesDaoInterface, targetCurrencyId, mainCurrencyId);

        return USDRateBase.divide(USDRateTarget,
                6, RoundingMode.HALF_EVEN);
    }

    private BigDecimal calculateReverseDecimal(BigDecimal value){
        return BigDecimal.ONE.divide(
                value,
                6, RoundingMode.HALF_EVEN);
    }

    private BigDecimal formatRateFromString(String rate){
        try{

            return new BigDecimal(rate);

        } catch (NumberFormatException e){
            throw new ValidationException("Invalid value ", e);
        } catch (NullPointerException e) {
            throw new ValidationException("must be not empty");
        }
    }

    private BigDecimal validateAndFormatRate(String unValidationRate){

        BigDecimal rate = formatRateFromString(unValidationRate);
        checkValueOnEmptyAndNegative(rate);
        return rate;
    }


    private void checkCodesForEquals(String baseCurrencyCode, String targetCurrencyCode){
        if(baseCurrencyCode.equalsIgnoreCase(targetCurrencyCode)){
            throw new SelfRatingException("Self rating, what else is there to write?");
        }
    }

    private void checkTargetValuesOnCorrectRequest(String baseCurrencyCode, String targetCurrencyCode){
        checkCodeOnEmpty(baseCurrencyCode);
        checkCodeOnEmpty(targetCurrencyCode);
        checkCodesForEquals(baseCurrencyCode, targetCurrencyCode);
    }

    private void checkCodeOnEmpty(String code){
        if (code == null || code.isBlank()){
            throw new ValidationException("Code is empty");
        }
    }

    private void checkValueOnEmptyAndNegative(BigDecimal value){
        if(value.signum() == 0){
            throw new ValidationException("Value = 0. Value must be > 0");
        }
        if(value.signum() < 0){
            throw new ValidationException("Value < 0. Value must be > 0");
        }

    }

    private BigDecimal routingRate(BigDecimal rate, int symbolsAfterDot){
        rate = rate.setScale(symbolsAfterDot, RoundingMode.HALF_EVEN);
        return rate;
    }
}
