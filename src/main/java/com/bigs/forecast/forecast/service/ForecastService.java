package com.bigs.forecast.forecast.service;


import com.bigs.forecast.config.AppConfig;
import com.bigs.forecast.forecast.entity.Forecast;
import com.bigs.forecast.forecast.repository.ForecastRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ForecastService {

    @Autowired
    private final AppConfig appConfig;
    private final ForecastRepository forecastRepository;

    @Autowired
    private final RestTemplate restTemplate;
    @Autowired
    private final ObjectMapper objectMapper;

    public ResponseEntity getForecast(int nx, int ny) {
        List<Forecast> forecasts = forecastRepository.findAll();

        if (forecasts.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // TODO: forecasts를 반환하도록 수정 예정
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity createForecast(int nx, int ny, int pageNo) throws ParseException {

        JSONArray itemList = getItemList(nx, ny, pageNo);

        // 받은 데이터를 Forecast 객체로 변환하여 저장
        for (Object obj : itemList) {
            JSONObject item = objectMapper.convertValue(obj, JSONObject.class);

            System.out.println(item);

            Forecast forecast = Forecast.builder()
                    .baseDate(LocalDate.parse((String) item.get("baseDate"), DateTimeFormatter.BASIC_ISO_DATE))
                    .baseTime((String) item.get("baseTime"))
                    .category((String) item.get("category"))
                    .fcstDate(LocalDate.parse((String) item.get("fcstDate"), DateTimeFormatter.BASIC_ISO_DATE))
                    .fcstTime((String) item.get("fcstTime"))
                    .fcstValue((String) item.get("fcstValue"))
                    .nx((Integer) item.get("nx")) // Integer로 받아도 되지만 예제에서는 Long으로 처리했습니다.
                    .ny((Integer) item.get("ny")) // Integer로 받아도 되지만 예제에서는 Long으로 처리했습니다.
                    .build();

            // forecastRepository를 사용하여 엔티티를 데이터베이스에 저장
            forecastRepository.save(forecast);
        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    private JSONArray getItemList(int nx, int ny, int pageNo) {
        // 오늘 날짜를 가져와서 baseDate로 설정
        String baseDate = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String baseTime = "0500"; // baseTime은 무조건 "0500"
        int num = 14;

        // JSON 파싱을 위한 MappingJackson2HttpMessageConverter 추가
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        restTemplate.getMessageConverters().add(converter);

        // String 파싱을 위한 StringHttpMessageConverter 제거
        restTemplate.getMessageConverters().removeIf(m -> m instanceof StringHttpMessageConverter);

        // API 호출 및 JSON 데이터 받아오기
        String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
        String serviceKey = appConfig.getOriginKey();
        String url = apiUrl +
                "?serviceKey=" + serviceKey +
                "&pageNo=" + pageNo +
                "&numOfRows=" + num +
                "&dataType=JSON" +
                "&base_date=" + baseDate +
                "&base_time=" + baseTime +
                "&nx=" + nx +
                "&ny=" + ny;

        // JSON 데이터를 String 형태로 받아옴
        JSONObject jsonResponse = restTemplate.getForObject(url, JSONObject.class);
//        System.out.println(jsonResponse);

        // JSON 데이터 파싱

        JSONObject responseBody = objectMapper.convertValue(jsonResponse.get("response"), JSONObject.class);
        JSONObject body = objectMapper.convertValue(responseBody.get("body"), JSONObject.class);
        JSONObject items = objectMapper.convertValue(body.get("items"), JSONObject.class);
        JSONArray itemList = objectMapper.convertValue(items.get("item"), JSONArray.class);
        return itemList;
    }
}
