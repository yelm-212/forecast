package com.bigs.forecast.forecast;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForecastRepository extends JpaRepository<Forecast, Long> {
}
