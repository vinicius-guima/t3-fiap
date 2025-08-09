package br.com.fiap.t3_security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import br.com.fiap.t3_security.service.MyUserDetailService;
import br.com.fiap.t3_security.support.encoder.PlaintextPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final MyUserDetailService myUserDetailService;

	public SecurityConfig(MyUserDetailService myUserDetailService) {
		this.myUserDetailService = myUserDetailService;
	}

	@Bean
	AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(userDetailsService()).passwordEncoder(encoder());
		return authenticationManagerBuilder.build();

	}

	@Bean
	PasswordEncoder encoder() {
		return new PlaintextPasswordEncoder();
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider() {

		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//		provider.setUserDetailsService(myUserDetailService);
		provider.setUserDetailsService(userDetailsService());
		provider.setPasswordEncoder(encoder());
		return provider;

	}

	@Bean
	SecurityFilterChain chain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(authorizeRequests -> {
			authorizeRequests.requestMatchers("/public").permitAll();
			authorizeRequests.requestMatchers("/logout").permitAll();
			authorizeRequests.anyRequest().authenticated();
		});
		http.httpBasic(Customizer.withDefaults());
		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		// Configuração de usuário em memória
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(User.withUsername("usuario").password(encoder().encode("senha")).roles("USER").build());
		manager.createUser(
				User.withUsername("admin").password(encoder().encode("password")).roles("ADMIN", "USER").build());
		return manager;
	}

}
