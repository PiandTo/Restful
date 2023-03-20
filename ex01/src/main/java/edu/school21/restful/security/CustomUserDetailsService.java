package edu.school21.restful.security;

import edu.school21.restful.models.User;
import edu.school21.restful.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Hello");
        User user = userRepository.findByLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException("User with login " + username + " not found");
        }
        return new CustomUserDetails(user);
    }
}
