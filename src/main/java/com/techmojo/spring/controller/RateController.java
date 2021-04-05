package com.techmojo.spring.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateController {

    //source link
    @GetMapping("/tenant")
    @RateLimiter(name = "tenantRateLimit", fallbackMethod = "tenantFallBack")
    public ResponseEntity tenant(@RequestParam(value = "name", defaultValue = "tenant") String name) {
        return ResponseEntity.ok().body("Response is successful: " + name);
    }

    //After limit is exceeded
    public ResponseEntity tenantFallBack(String name, io.github.resilience4j.ratelimiter.RequestNotPermitted ex) {
        System.out.println("Rate limit applied no further calls are accepted");


        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.set("Retry-After", "3600"); //retry after 1 hour i.e 3600 seconds

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .headers(responseHeaders) //send retry header
                .body("Too many requests please retry after 1 hour"); //call limit is exceeded
    }
}
