package com.bigs.forecast.forecast.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

public class ForecastDto {

    @Getter
    @Builder
    public static class Response{
        private String baseDate;
        private String baseTime;
        private String category;
        private String fcstDate;
        private String fcstTime;
        private String fcstValue;
        private int nx;
        private int ny;
        @Builder
        @QueryProjection
        public Response(String baseDate, String baseTime, String category, String fcstDate, String fcstTime, String fcstValue, int nx, int ny) {
            this.baseDate = baseDate;
            this.baseTime = baseTime;
            this.category = category;
            this.fcstDate = fcstDate;
            this.fcstTime = fcstTime;
            this.fcstValue = fcstValue;
            this.nx = nx;
            this.ny = ny;
        }
    }
}
