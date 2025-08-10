package br.com.fiap.t3_security.service;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.fiap.t3_security.entity.User;
import br.com.fiap.t3_security.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service(value = "customDetailService")
@AllArgsConstructor
public class CustomDetailService implements UserDetailsService {

	private final UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = repository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(username + "not found."));

		List<SimpleGrantedAuthority> auth = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName())).toList();

		return new org.springframework.security.core.userdetails.User(
				user.getUsername(),
				user.getPassword(),
				user.isEnabled(),
				true,
				true,
				true,
				auth
			);
	}
}