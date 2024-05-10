package com.bigs.forecast.forecast;


import com.bigs.forecast.address.AddressService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    public ResponseEntity postForecast(@RequestParam(required = false, defaultValue = "62") int nx,
                                       @RequestParam(required = false, defaultValue = "130") int ny) throws ParseException, JsonProcessingException {


        return forecastService.createForecast(nx, ny);
    }

    @GetMapping("/")
    public ResponseEntity getForecast(@RequestParam(required = false, defaultValue = "62") int nx,
                                      @RequestParam(required = false, defaultValue = "130") int ny){

        return forecastService.getForecast(nx,ny);
    }
}
