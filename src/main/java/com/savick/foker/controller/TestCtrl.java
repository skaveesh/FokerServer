package com.savick.foker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by skaveesh on 2017-06-04.
 */

@RestController
@CrossOrigin("*")
@RequestMapping("test")
public class TestCtrl {

    @RequestMapping(value = "t1", method = RequestMethod.GET)
    public Object getTest() {
        return ResponseEntity.status(HttpStatus.OK).body("TEST");
    }
}
