package com.famousb.securityjwt;

import com.famousb.securityjwt.model.AppUser;
import com.famousb.securityjwt.model.Role;
import com.famousb.securityjwt.service.AppUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class SecurityjwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityjwtApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(AppUserService appUserService){
		return args -> {
			appUserService.saveRole(new Role(null, "ROLE_USER"));
			appUserService.saveRole(new Role(null, "ADMIN"));
			appUserService.saveRole(new Role(null, "SUPER_ADMIN"));
			appUserService.saveRole(new Role(null, "AGENT"));

			appUserService.saveUser(new AppUser(null, "Test", "test@gmail.com", "123456","test@2020", new ArrayList<>()));
			appUserService.saveUser(new AppUser(null, "Bola", "bola@gmail.com","123456", "bola@2020", new ArrayList<>()));
			appUserService.saveUser(new AppUser(null, "Kola", "kola@gmail.com","123456", "kola@2020", new ArrayList<>()));
			appUserService.saveUser(new AppUser(null, "Ade", "ade@gmail.com","123456", "ade@2020", new ArrayList<>()));

			appUserService.addAppUserRole("test@gmail.com", "ROLE_USER");
			appUserService.addAppUserRole("test@gmail.com", "ADMIN");
			appUserService.addAppUserRole("kola@gmail.com", "SUPER_ADMIN");
			appUserService.addAppUserRole("ade@gmail.com", "SUPER_ADMIN");
			appUserService.addAppUserRole("test@gmail.com", "SUPER_ADMIN");
			appUserService.addAppUserRole("bola@gmail.com", "ADMIN");
			appUserService.addAppUserRole("ade@gmail.com", "ROLE_USER");
			appUserService.addAppUserRole("test@gmail.com", "AGENT");

		};
	}
}
