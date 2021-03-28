package com.example.believers_network_backend.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
//Enabling security checking at the method level with annotation support
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception
    {
        http.authorizeRequests() //authorize all graphQL queries as we will filter request at the resolvers level
                .antMatchers("/graphql").permitAll()
                //authorize requests from graphql related apps that we will need
                .antMatchers("/graphiql").permitAll()
                .antMatchers("/schema.json").permitAll()
                .antMatchers("/vendor/**").permitAll()

                //any other requests should be authenticated
                .anyRequest().authenticated();
    }
}
