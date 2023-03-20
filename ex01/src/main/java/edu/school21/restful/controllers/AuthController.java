package edu.school21.restful.controllers;

import edu.school21.restful.exceptions.user.UserError;
import edu.school21.restful.jwt.JwtRefreshToken;
import edu.school21.restful.jwt.JwtRequest;
import edu.school21.restful.jwt.JwtResponse;
import edu.school21.restful.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@Tag(name = "JWT Authorization Controller", description = "REST controller provide services to generate JWT token")
public class AuthController {
	private AuthService authService;

	@Autowired
	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/signUp")
	public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest) {
		JwtResponse jwtResponse = authService.login(jwtRequest);
		if (jwtResponse == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return ResponseEntity.ok(jwtResponse);
	}

	@PostMapping("/token")
	public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody JwtRefreshToken jwtRequest) {
		JwtResponse jwtResponse = authService.getAccessToken(jwtRequest.getRefreshToken());
		return ResponseEntity.ok(jwtResponse);
	}

	@PostMapping("/refresh")
	public ResponseEntity<JwtResponse> refresh (@RequestBody JwtRefreshToken jwtRequest) {
		JwtResponse jwtResponse = authService.refresh(jwtRequest.getRefreshToken());
		return ResponseEntity.ok(jwtResponse);
	}

	@PostMapping("/error")
	public ResponseEntity<?> error() {
		UserError userError = new UserError(HttpStatus.FORBIDDEN, "Try to request JWT", "error");
		return ResponseEntity
				.status(userError.getStatus())
				.body(userError);
	}
}