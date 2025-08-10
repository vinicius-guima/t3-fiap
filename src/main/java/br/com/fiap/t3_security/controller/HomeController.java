package br.com.fiap.t3_security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	@GetMapping("/admin")
	public String adminPage() {
		return "<h2> Rota ADMIN </h2>";
	}
	
	@GetMapping("/user")
	public String userPage() {
		return "<h2> Rota USER</h2>";
	}
	
	@GetMapping("/public")
	public String loginPublic() {
		return "<h2>Rota Publica</h2>";
	}
	
	@GetMapping("/login")
	public String loginPage() {
		return "<h2>LOGIN</h2>";
	}
	
}
