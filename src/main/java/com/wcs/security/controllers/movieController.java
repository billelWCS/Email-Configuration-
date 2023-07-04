package com.wcs.security.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movie")
public class movieController {

    @GetMapping("")
    public String greeting (){
        return "Hello" ;
    }



}
