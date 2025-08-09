package br.com.fiap.t3_security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HttpController {
 
	@GetMapping("public")
	public String publicRoute() {
		return "<h2> Rota publica</h2>";
	}
	
	@GetMapping("private")
	public String privateRoute() {
		return "<h2> Rota privada</h2>";
	}
}
