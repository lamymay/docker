package com.arc.docker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class DockerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DockerApplication.class, args);
    }


    @GetMapping("/info")
    public ResponseEntity code2() {
        ResponseEntity responseEntity = new ResponseEntity<Object>("hello-info", HttpStatus.OK);
        return responseEntity;
    }
    @GetMapping("/info2")
    public ResponseEntity info2() {
        ResponseEntity responseEntity = new ResponseEntity<Object>("hello-info-2", HttpStatus.OK);
        return responseEntity;
    }

}
