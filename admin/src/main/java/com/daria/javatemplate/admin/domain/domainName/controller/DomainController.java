package com.daria.javatemplate.admin.domain.domainName.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/", produces = "application/json")
@RequiredArgsConstructor
public class DomainController {

    @PostMapping(path = "/example1")
    public ResponseEntity<String> postSample() {
        return ResponseEntity.ok("post okay");
    }

    @GetMapping(path = "/example1")
    public ResponseEntity<String> getSample() {
        return ResponseEntity.ok("get okay");
    }

    @PatchMapping(path = "/example1")
    public ResponseEntity<String> patchSample() {
        return ResponseEntity.ok("patch okay");
    }

    @DeleteMapping(path = "/example1")
    public ResponseEntity<String> deleteSample() {
        return ResponseEntity.ok("delete okay");
    }

}
