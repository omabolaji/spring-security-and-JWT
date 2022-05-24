package com.famousb.securityjwt.controller;

import com.famousb.securityjwt.dto.request.AddRoleDto;
import com.famousb.securityjwt.exception.AppUserException;
import com.famousb.securityjwt.exception.RoleNotFoundException;
import com.famousb.securityjwt.model.AppUser;
import com.famousb.securityjwt.model.Role;
import com.famousb.securityjwt.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController @RequiredArgsConstructor
@RequestMapping(path = "/api/v1")
public class AppUserController {

    private final AppUserService appUserService;


    @GetMapping("/users")
    public ResponseEntity<List<AppUser>> getAllAppUsers(){
        return  ResponseEntity.ok().body(appUserService.getAllAppUser());
    }

    @PostMapping("/user/create")
    public ResponseEntity<AppUser> saveAppUser(@RequestBody @Valid AppUser appUser) throws AppUserException {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/user/create").toUriString());
        return  ResponseEntity.created(uri).body(appUserService.saveUser(appUser));
    }

    @PostMapping("/role/create")
    public ResponseEntity<Role> saveRole(@RequestBody @Valid Role role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/role/create").toUriString());
        return  ResponseEntity.created(uri).body(appUserService.saveRole(role));
    }

    @PutMapping("/user/add/role")
    public ResponseEntity<?> addAppUserRole(@RequestBody @Valid AddRoleDto data) throws RoleNotFoundException, AppUserException {
        this.appUserService.addAppUserRole(data.getEmail(), data.getRoleName());
        return  ResponseEntity.ok().build();
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<Optional<AppUser>> getAppUser(@PathVariable @Valid String email) {
        return  ResponseEntity.ok().body(this.appUserService.getAppUser(email));
    }

}
