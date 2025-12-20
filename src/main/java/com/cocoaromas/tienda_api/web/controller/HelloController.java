package com.cocoaromas.tienda_api.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    
    @GetMapping("/")
    public String index() {
        return "API OK " + java.time.LocalDateTime.now();
    }
}
