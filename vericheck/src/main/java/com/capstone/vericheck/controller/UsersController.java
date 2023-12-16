package com.capstone.vericheck.controller;

import com.capstone.vericheck.model.UsersModel;
import com.capstone.vericheck.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class UsersController {

    private final UsersService userService;

    public UsersController(UsersService userService) {
        this.userService = userService;
    }

    @GetMapping("/berita-terbaru")
    //TODO make service
    public String getListBeritaTerbaru(){
        return "akan return list berita baru";
    }
    @GetMapping("/berita-hoax")
    //TODO make service
    public String getListBeritaHoax(){
        return "akan return list berita hoax";
    }

    @PostMapping("/berita-terbaru")
    //TODO make service
    public String createBeritaTerbaru(){
        return "akan create berita baru sesuai input";
    }
    @PostMapping("/berita-hoax")
    //TODO make service
    public String createBeritaHoax(){
        return "akan create berita hoax sesuai input";
    }
    @GetMapping("/profile")
    //TODO make service
    public String getProfile(){
        return "profile";
    }

    @PostMapping("/register")
    public UsersModel register(@RequestBody UsersModel usersModel){
        System.out.println("tes register" + usersModel);
        UsersModel registerUser = userService.registerUser(usersModel.getUsername(), usersModel.getPassword(), usersModel.getEmail());
        return registerUser;
    }

    @PostMapping("/login")
    public boolean login(@RequestBody UsersModel usersModel){
        System.out.println("tes login" + usersModel);
        return userService.authenticate(usersModel.getUsername(), usersModel.getPassword());
    }


}
