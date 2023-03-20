package edu.school21.restful.config;

import edu.school21.restful.filters.JwtFilter;
import edu.school21.restful.jwt.JwtProvider;
import edu.school21.restful.security.JwtAuthentication;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;

@Configuration
public class WebSecurityConfiguration {

    @Autowired
    private JwtProvider jwtProvider;

//    @Autowired
//    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .authorizeRequests()
                .mvcMatchers("/api/auth/signUp", "/api/v3/*", "/v3/*/*", "/api/swagger-ui/*", "/api/v3/*/*").permitAll()
                .mvcMatchers(HttpMethod.DELETE, "/courses", "/users").hasRole("ADMINISTRATOR")
                .mvcMatchers(HttpMethod.PUT, "/courses", "/users").hasRole("ADMINISTRATOR")
                .mvcMatchers(HttpMethod.POST, "/courses", "/users").hasRole("ADMINISTRATOR")
                .mvcMatchers(HttpMethod.GET, "/courses", "/users").hasAnyRole("STUDENT", "TEACHER", "ADMINISTRATOR")
                .anyRequest().authenticated();
        httpSecurity
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
//                .addFilterAfter(new CustomCsrfFilter(), CsrfFilter.class)
                .addFilterBefore(new JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public UserDetailsService customUserDetailsService() {
//        return this.userDetailsService;
//    }

}
