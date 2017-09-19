package com.aim.kafka.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/")
public class HomeController {

    @RequestMapping(value = "/ping", method = GET)
    public ResponseEntity getAllOrders() {
        return new ResponseEntity<String>("Checked", HttpStatus.OK);
    }
}
