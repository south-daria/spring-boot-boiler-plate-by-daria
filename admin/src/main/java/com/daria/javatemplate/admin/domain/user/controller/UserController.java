package com.daria.javatemplate.admin.domain.user.controller;

import com.daria.javatemplate.admin.domain.user.service.UserAdminService;
import com.daria.javatemplate.core.domain.user.model.dto.UserDTO;
import com.daria.javatemplate.core.domain.user.model.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/admin/v1/user", produces = "application/json")
@RequiredArgsConstructor
public class UserController {

    private final UserAdminService userService;

    @PostMapping()
    public ResponseEntity<UserEntity> createUser(
            @RequestBody UserDTO userDto
    ) {
        UserEntity userEntity = userService.createUser(userDto);
        return ResponseEntity.ok(userEntity);
    }

    @GetMapping()
    public ResponseEntity<String> getSample() {
        return ResponseEntity.ok("get okay");
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<String> getSample(@PathVariable Long id) {
        return ResponseEntity.ok("get okay" + id);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<String> patchSample(@PathVariable Long id) {
        return ResponseEntity.ok("patch okay" + id);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteSample(@PathVariable Long id) {
        return ResponseEntity.ok("delete okay" + id);
    }

}
