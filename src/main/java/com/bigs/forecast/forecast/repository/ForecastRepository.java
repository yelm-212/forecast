package com.bigs.forecast.forecast.repository;

import com.bigs.forecast.forecast.entity.Forecast;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForecastRepository extends JpaRepository<Forecast, Long> {
}
