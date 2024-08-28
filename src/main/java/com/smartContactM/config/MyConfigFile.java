package com.smartContactM.config;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MyConfigFile  {
		
	@Bean
	public UserDetailsService getUserDetailsService() {
		return new UserDetailsConfigImpl();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider () {
		DaoAuthenticationProvider dap=new DaoAuthenticationProvider();
		dap.setUserDetailsService(this.getUserDetailsService());
		dap.setPasswordEncoder(passwordEncoder());
		return dap;
	}
	
	@Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authenticationProvider());
        return authenticationManagerBuilder.build();
    }

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .authorizeHttpRequests((authz) -> authz
	            .requestMatchers("/users/**").hasRole("USER")
	            .requestMatchers("/admin/**").hasRole("ADMIN")
	            .requestMatchers("/**").permitAll()
	            .anyRequest().authenticated()
	        )
	        .formLogin((form) -> form
	            .loginPage("/signin")
	            .defaultSuccessUrl("/users/index")
	            .permitAll()
	        )
	        .logout((logout) -> logout
	            .logoutUrl("/logout")
	            .logoutSuccessUrl("/signin")
	            .invalidateHttpSession(true)
	            .deleteCookies("JSESSIONID")
	            .permitAll()
	        );
	    return http.build();
	}

	
}		
