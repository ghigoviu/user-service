package com.microservices.userservice.service;

import com.microservices.userservice.entity.User;
import com.microservices.userservice.feign.BikeFeignClient;
import com.microservices.userservice.feign.CarFeignClient;
import com.microservices.userservice.model.Bike;
import com.microservices.userservice.model.Car;
import com.microservices.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    BikeFeignClient bikeFeignClient;
    @Autowired
    CarFeignClient carFeignClient;

    public List<User> getAll(){
        return userRepository.findAll();
    }
    public User getUserById(int id){
        return userRepository.findById(id).orElse(null);
    }
    public User save(User user){
        User newUser = userRepository.save(user);
         return newUser;
    }
    public List<Car> getCars(int userId){
        List<Car> cars = restTemplate.getForObject("http://localhost:8002/cars/byuser/" + userId, List.class);
        return cars;
    }
    public List<Bike> getBikes(int userId){
        List<Bike> bikes = restTemplate.getForObject("http://localhost:8003/bikes/byuser/" + userId, List.class);
        return bikes;
    }
    public Car saveCar(int userId, Car car){
        car.setUserId(userId);
        return carFeignClient.save(car);
    }

    public Bike saveBike(int userId, Bike bike){
        bike.setUserId(userId);
        return bikeFeignClient.save(bike);
    }

    public Map<String, Object> getUserAndVehicles(int userId){
        Map<String, Object> result = new HashMap<>();
        User user =  userRepository.findById(userId).orElse(null);
        if(user == null){
            result.put("Mensaje", "No existe el usuario");
            return result;
        }
        result.put("user", user);
        List<Car> cars  = carFeignClient.getCars(userId);
        if (cars.isEmpty())
            result.put("Cars", "usuario sin carros");
        else
            result.put("Cars", cars);

        List<Bike> bikes = bikeFeignClient.getBikes(userId);
        if (cars.isEmpty())
            result.put("Bikes", "usuario sin motos");
        else
            result.put("Bikes", bikes);

        return result;
    }

}