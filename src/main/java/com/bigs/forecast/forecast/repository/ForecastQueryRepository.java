package com.bigs.forecast.forecast.repository;

import static com.bigs.forecast.forecast.entity.QForecast.forecast;

import com.bigs.forecast.forecast.dto.ForecastDto;
import com.bigs.forecast.forecast.dto.QForecastDto_Response;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ForecastQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;


    public Page<ForecastDto.Response> getForecasts(Pageable pageable) {
        List<ForecastDto.Response> responses = jpaQueryFactory
                .select(new QForecastDto_Response(
                        forecast.baseDate.stringValue(),
                        forecast.baseTime,
                        forecast.category,
                        forecast.fcstDate.stringValue(),
                        forecast.fcstTime,
                        forecast.fcstValue,
                        forecast.nx,
                        forecast.ny
                ))
                .from(forecast)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory
                .select(forecast.count())
                .from(forecast)
                .fetchOne();

        return new PageImpl<>(responses, pageable, count);
    }
}
