package com.microservices.userservice.controller;

import com.microservices.userservice.entity.User;
import com.microservices.userservice.model.Bike;
import com.microservices.userservice.model.Car;
import com.microservices.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @GetMapping
    public ResponseEntity<List<User>> getAll(){
        List<User> users = userService.getAll();
        if (users.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable("id") int id){
        User users = userService.getUserById(id);
        if (users == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(users);
    }

    @PostMapping()
    public ResponseEntity<User> save(@RequestBody User user){
        User userNew = userService.save(user);
        return ResponseEntity.ok(userNew);
    }

    @GetMapping("/cars/{userId}")
    public ResponseEntity<List<Car>> getCars(@PathVariable("userId") int userId){
        User user = userService.getUserById(userId);
        if(user == null)
             return ResponseEntity.notFound().build();
        List<Car> cars = userService.getCars(userId);
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/bikes/{userId}")
    public ResponseEntity<List<Bike>> getBikes(@PathVariable("userId") int userId){
        User user = userService.getUserById(userId);
        if(user == null)
            return ResponseEntity.notFound().build();
        List<Bike> bikes = userService.getBikes(userId);
        return ResponseEntity.ok(bikes);
    }

    @PostMapping("/savecar/{userId}")
    public ResponseEntity<Car> saveCar(@PathVariable("userId") int userId, @RequestBody Car car){
        Car carnew = userService.saveCar(userId, car);
        if (userService.getUserById(userId) == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(carnew);
    }

    @PostMapping("/savebike/{userId}")
    public ResponseEntity<Bike> saveBike(@PathVariable("userId") int userId, @RequestBody Bike bike){
        Bike bikeNew = userService.saveBike(userId, bike);
        return ResponseEntity.ok(bikeNew);
    }

    @GetMapping("/getAll/{userId}")
    public ResponseEntity<Map<String, Object>> getAllVehicles(@PathVariable("userId") int userid){
        Map<String, Object> result = userService.getUserAndVehicles(userid);
        return ResponseEntity.ok(result);
    }
}