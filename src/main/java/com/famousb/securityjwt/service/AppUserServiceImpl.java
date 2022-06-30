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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service @Transactional @RequiredArgsConstructor
@Slf4j
public class AppUserServiceImpl implements AppUserService, UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //todo: you can either use username or email to replace the username field
        log.info("UserName {} ", username);
//        Optional<AppUser> appUser = appUserRepository.findByUsername(username);
        Optional<AppUser> appUser = appUserRepository.findByEmail(username);
        System.out.println("APPUSER--"+appUser.get().getUsername());
        if(appUser.isEmpty()){
            log.error("User not found in db {} ", username);
            throw  new UsernameNotFoundException("User not found");
        }else {
            log.info("User found in db {} ", username);
        }
        //todo: get the role authority for the user
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        appUser.get().getRoles().forEach(role -> {
           authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        //todo: return the spring user class implementation
        return new User(appUser.get().getEmail(),appUser.get().getPassword(), authorities);
    }

    @Override
    public AppUser saveUser(AppUser appUser) throws AppUserException {
        log.info("saving new user {} to db", appUser.getUsername());
        //todo: check if email exist
        boolean email = appUserRepository.existsByEmail(appUser.getEmail());
        if(email) throw new AppUserException("Email already taken");
        //todo: check if username exist
        boolean username = appUserRepository.existsByUsername(appUser.getUsername());
        if(username) throw new AppUserException("Username already taken");
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
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

        //todo: check is user with this email exist
        Optional<AppUser> user = appUserRepository.findByEmail(email);
        if(user.isEmpty()) throw new AppUserException("User not found");

        //todo: find role to be added for this user
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
    public Optional<AppUser> getAppUserByUsernameOrEmail(String query) {
        log.info("get user info", query);
        return appUserRepository.findByUsernameOrEmail(query);
    }



    @Override
    public List<AppUser> getAllAppUser() {
        log.info("fetch all user");
        return appUserRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }


}
