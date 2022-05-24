package com.famousb.securityjwt.service;

import com.famousb.securityjwt.exception.AppUserException;
import com.famousb.securityjwt.exception.RoleNotFoundException;
import com.famousb.securityjwt.model.AppUser;
import com.famousb.securityjwt.model.Role;
import com.famousb.securityjwt.repository.AppUserRepository;
import com.famousb.securityjwt.repository.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service @Transactional @RequiredArgsConstructor
@Slf4j
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;

    @Override
    public AppUser saveUser(AppUser appUser) throws AppUserException {
        log.info("saving new user {} to db", appUser.getUsername());

        boolean email = appUserRepository.existsByEmail(appUser.getEmail());
        if(email) throw new AppUserException("Email already taken");

        boolean username = appUserRepository.existsByUsername(appUser.getUsername());
        if(username) throw new AppUserException("Username already taken");

        return appUserRepository.save(appUser);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("saving new role {} to db", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addAppUserRole(String email, String roleName) throws AppUserException, RoleNotFoundException {
        log.info("adding new {} role to user {} to db", email, roleName);

        Optional<AppUser> user = appUserRepository.findByEmail(email);
        if(user.isEmpty()) throw new AppUserException("User not found");

        Optional<Role> role = roleRepository.findByName(roleName);
        if(role.isEmpty()) throw new RoleNotFoundException("User not found");

        user.get().getRoles().add(role.get());
    }

    @Override
    public Optional<AppUser> getAppUser(String email) {
        log.info("get user info", email);
        return appUserRepository.findByEmail(email);
    }

    @Override
    public List<AppUser> getAllAppUser() {
        log.info("fetch all user");
        return appUserRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }
}
