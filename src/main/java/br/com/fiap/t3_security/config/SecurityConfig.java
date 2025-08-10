package br.com.fiap.t3_security.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import br.com.fiap.t3_security.support.encoder.PlaintextPasswordEncoder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

	private final UserDetailsService customDetailService;

	@Bean
	PasswordEncoder encoder() {
		return new PlaintextPasswordEncoder();
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customDetailService);
		provider.setPasswordEncoder(encoder());
		return provider;

	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(
					authorizeRequests -> {
				authorizeRequests.requestMatchers("/h2-console/**").permitAll();
				authorizeRequests.requestMatchers("/admin/**").hasRole("ADMIN");
				authorizeRequests.requestMatchers("/user/**").hasRole("USER");
				authorizeRequests.anyRequest().authenticated();
			}
		)
		.formLogin(form -> form.successHandler(customAuthenticationSuccessahndler()))
		.logout(logout -> logout.permitAll());
		
		http.headers(headers -> headers.frameOptions().disable());
		http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));
		return http.build();
	}

	private AuthenticationSuccessHandler customAuthenticationSuccessahndler() {
		return new AuthenticationSuccessHandler() {
			
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request,
					HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {

				if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
					response.sendRedirect("/admin");
				}
				
				if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
					response.sendRedirect("/user");
				}else {
					response.sendRedirect("/");
				}
			}
		};
	}

//	@Bean
//	UserDetailsService userDetailsService() {
//		// Configuração de usuário em memória
//		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//		manager.createUser(User.withUsername("usuario").password(encoder().encode("senha")).roles("USER").build());
//		manager.createUser(
//				User.withUsername("admin").password(encoder().encode("password")).roles("ADMIN", "USER").build());
//		return manager;
//	}

}
