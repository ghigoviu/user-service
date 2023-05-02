package com.microservices.userservice.feign;

import com.microservices.userservice.model.Car;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "car-service", url="http://localhost:8002")
public interface CarFeignClient {
    @PostMapping()
    Car save(@RequestBody Car car);

    @GetMapping("/byuser/{userId}")
    List<Car> getCars(@PathVariable("userId") int userId);

}