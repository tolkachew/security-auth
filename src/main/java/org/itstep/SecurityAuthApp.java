package org.itstep;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Scanner;

@SpringBootApplication
public class SecurityAuthApp implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(SecurityAuthApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        MyAuthenticationManager authenticationManager = new MyAuthenticationManager();
        while (true) {
            System.out.println("Enter your username");
            String name = scanner.nextLine();
            System.out.println("Enter your password");
            String password = scanner.nextLine();
            try {
                Authentication authentication = new UsernamePasswordAuthenticationToken(name, password);
                Authentication result = authenticationManager.authenticate(authentication);
                SecurityContextHolder.getContext().setAuthentication(result);
                break;
            } catch (AuthenticationException e) {
                System.out.println("Authentication failed: " + e.getMessage());
            }
        }
        System.out.println("Successfully authenticated. Security context contains: " +
                SecurityContextHolder.getContext().getAuthentication());
    }
}

class MyAuthenticationManager implements AuthenticationManager {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.getName().equals("root") && authentication.getCredentials().equals("root")) {
            return new UsernamePasswordAuthenticationToken(authentication.getName(),
                    authentication.getCredentials(), Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("ROLE_ROOT")));
        }
        throw new BadCredentialsException("Bad Credentials");
    }
}
