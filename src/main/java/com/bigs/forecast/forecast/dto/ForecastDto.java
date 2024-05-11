package com.bigs.forecast.forecast.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

public class ForecastDto {

    @Getter
    @AllArgsConstructor
    public static class Response{
        private String baseDate;
        private String baseTime;
        private String category;
        private String fcstDate;
        private String fcstTime;
        private String fcstValue;

        private int nx;
        private int ny;
    }
}
