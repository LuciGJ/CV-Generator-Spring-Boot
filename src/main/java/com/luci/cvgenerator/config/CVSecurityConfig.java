package com.luci.cvgenerator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.luci.cvgenerator.service.UserService;

@Configuration
@EnableWebSecurity
public class CVSecurityConfig {

	@Autowired
	private UserService userService;

	@Autowired
	private CVAuthenticationSuccessHandler authenticationSuccessHandler;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests().requestMatchers("/").permitAll().requestMatchers("/confirmAccount").permitAll()
				.requestMatchers("/deleteConfirm").permitAll().requestMatchers("/recovery/*").anonymous()
				.requestMatchers("/admin/*").hasRole("ADMIN").requestMatchers("/register/*").anonymous()
				.requestMatchers("/account/*").hasRole("USER").requestMatchers("/userdata/*").hasRole("USER")
				.requestMatchers("/language/*").hasRole("USER").requestMatchers("/education/*").hasRole("USER")
				.requestMatchers("/experience/*").hasRole("USER").requestMatchers("/hardskill/*").hasRole("USER")
				.requestMatchers("/softskill/*").hasRole("USER").requestMatchers("/project/*").hasRole("USER")
				.requestMatchers("/interest/*").hasRole("USER").requestMatchers("/menu").hasRole("USER")
				.requestMatchers("/cv/*").hasRole("USER").requestMatchers("/user-photos/*").permitAll()
				.requestMatchers("/img/*").permitAll().requestMatchers("/css/*").permitAll()
				.requestMatchers("/favicon.ico").permitAll().and().rememberMe().key("uniqueAndSecret")
				.tokenValiditySeconds(86400).rememberMeParameter("remember-me").and().formLogin().loginPage("/login")
				.loginProcessingUrl("/authenticateTheUser").successHandler(authenticationSuccessHandler).permitAll()
				.and().logout().permitAll().deleteCookies("JSESSIONID");

		return http.build();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userService);
		auth.setPasswordEncoder(passwordEncoder);
		return auth;
	}

}
