package com.famousb.securityjwt.service;

import com.famousb.securityjwt.exception.AppUserException;
import com.famousb.securityjwt.exception.RoleNotFoundException;
import com.famousb.securityjwt.model.AppUser;
import com.famousb.securityjwt.model.Role;

import java.util.List;
import java.util.Optional;

public interface AppUserService {
    public AppUser saveUser(AppUser appUser) throws AppUserException;
    public Role saveRole(Role role);
    public void  addAppUserRole(String email, String roleName) throws AppUserException, RoleNotFoundException;
    public Optional<AppUser> getAppUser(String email);
    public List<AppUser> getAllAppUser();
}
