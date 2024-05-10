package com.bigs.forecast.forecast;

import javax.persistence.*;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Builder
@Getter
@Table(name = "forecast")
public class Forecast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate baseDate;
    private String baseTime;
    private String category;
    private LocalDate fcstDate;
    private String fcstTime;
    private String fcstValue;

    private int nx;
    private int ny;

}
