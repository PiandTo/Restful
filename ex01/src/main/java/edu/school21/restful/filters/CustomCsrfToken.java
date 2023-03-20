package edu.school21.restful.filters;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomCsrfToken extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		CsrfToken csrfToken = (CsrfToken) servletRequest.getAttribute("_csrf");
		servletResponse.setHeader("CSRF_TOKEN", csrfToken.getToken());
		filterChain.doFilter(servletRequest, servletResponse);
	}
}