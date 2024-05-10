package com.bigs.forecast.forecast;


import com.bigs.forecast.address.AddressService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/forecast")
@RequiredArgsConstructor
public class ForecastController {
    private final ForecastService forecastService;

    @GetMapping("/hello")
    public String test(){
        return "hello";
    }

    @PostMapping("/")
    public ResponseEntity postForecast(@RequestParam(required = false, defaultValue = "문충로74")
                                     String address) throws ParseException {


        return forecastService.createForecast(address);
    }

    @GetMapping("/")
    public ResponseEntity getForecast(@RequestParam(required = false, defaultValue = "문충로74")
                                          String address){


        // DB에 해당 지역 단기예보 저장되지 않은 경우
        return forecastService.getForecast(address);
    }
}
