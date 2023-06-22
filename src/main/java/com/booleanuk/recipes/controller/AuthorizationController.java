package com.booleanuk.recipes.controller;

import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("tost")
@RequiredArgsConstructor
public class AuthorizationController {


    @GetMapping
    public ResponseEntity<String> sayHello() {
        System.out.println("hereee");
        return ResponseEntity.ok("Here is your resource");
    }
}
