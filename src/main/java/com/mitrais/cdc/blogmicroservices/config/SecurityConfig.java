/**
 * <h1>Security Configuration</h1>
 * Class to handle security config that using JWT Token
 * in this project.
 *
 * @author Syarif Hidayat
 * @version 1.0
 * @since 2019-08-20
 * */

package com.mitrais.cdc.blogmicroservices.config;

import com.mitrais.cdc.blogmicroservices.security.jwt.JwtConfigurer;
import com.mitrais.cdc.blogmicroservices.security.jwt.JwtTokenProvider;
import com.mitrais.cdc.blogmicroservices.services.UserDetailsServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@EnableWebSecurity
/*@EnableGlobalMethodSecurity(prePostEnabled = true)*/
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private JwtConfigurer jwtConfigurer;
    PasswordEncoder passwordEncoder;
    UserDetailsServices userDetailsService;

    public SecurityConfig() {

    }

    /**
     * This class will be used to setup Authentication mechanism
     * using DAOAuthentciationProvider and also set UserDetailsServices
     * and BCryptPasswordEncoder into AuthenticationProvider.
     *
     * @return will return DaoAuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider(){
        log.info("Authentication Provider Process.....");
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        return daoAuthenticationProvider;
    }

    /**
     * This project will be used to create BCryptPasswordEncoder Bean
     *
     * @return will return BCryptPasswordEncoder bean
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        log.info("Encode password....");
        return new BCryptPasswordEncoder();
    }

    /**
     * This method will be used to by pass spring security
     * for certain url and to setup JWT Token in this project
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .cors().and()
                .authorizeRequests()
                .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(jwtConfigurer);


    }


    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserDetailsService(UserDetailsServices userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setJwtConfigurer(JwtConfigurer jwtConfigurer) {
        this.jwtConfigurer = jwtConfigurer;
    }
}
