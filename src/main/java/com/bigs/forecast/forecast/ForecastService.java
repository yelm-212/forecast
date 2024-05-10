package com.bigs.forecast.forecast;


import com.bigs.forecast.config.AppConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ForecastService {

    @Autowired
    private final AppConfig appConfig;
    private final ForecastRepository forecastRepository;

    public ResponseEntity getForecast(int nx, int ny) {
        List<Forecast> forecasts = forecastRepository.findAll();

        if (forecasts.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // TODO: forecasts를 반환하도록 수정 예정
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity createForecast(int nx, int ny) throws ParseException {

        // 오늘 날짜를 가져와서 baseDate로 설정
        String baseDate = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String baseTime = "0500"; // baseTime은 무조건 "0500"
        int pageNo = 1, num = 14;

        RestTemplate restTemplate = new RestTemplate();

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
        ObjectMapper mapper = new ObjectMapper();

        JSONObject responseBody = mapper.convertValue(jsonResponse.get("response"), JSONObject.class);
        JSONObject body = mapper.convertValue(responseBody.get("body"), JSONObject.class);
        JSONObject items = mapper.convertValue(body.get("items"), JSONObject.class);
        JSONArray itemList = mapper.convertValue(items.get("item"), JSONArray.class);

        // 받은 데이터를 Forecast 객체로 변환하여 저장
        for (Object obj : itemList) {
            JSONObject item = mapper.convertValue(obj, JSONObject.class);

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
}
