package com.bigs.forecast.forecast.controller;


import com.bigs.forecast.forecast.service.ForecastService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
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

    @PostMapping("")
    public ResponseEntity postForecast(@RequestParam(required = false, defaultValue = "62") int nx,
                                       @RequestParam(required = false, defaultValue = "130") int ny,
                                       @RequestParam(required = false, defaultValue = "1") int page) throws ParseException, JsonProcessingException {


        return forecastService.createForecast(nx, ny, page);
    }

    @GetMapping("")
    public ResponseEntity getForecast(@RequestParam(required = false, defaultValue = "62") int nx,
                                      @RequestParam(required = false, defaultValue = "130") int ny,
                                      @RequestParam(required = false, defaultValue = "1") int page){

        return forecastService.getForecast(nx,ny, page);
    }
}
