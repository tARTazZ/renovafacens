package com.renovafacens.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TesteController {

    @GetMapping("/api/teste")
    public String teste() {
        return "Oiii~!";
    }
}