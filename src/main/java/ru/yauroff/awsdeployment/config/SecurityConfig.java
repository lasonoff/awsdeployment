package ru.yauroff.awsdeployment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.yauroff.awsdeployment.security.jwt.JwtConfigure;

@EnableWebSecurity(debug = true)
public class SecurityConfig {

    @Configuration
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    @Order(1)
    public static class SecurityAPIConfig extends WebSecurityConfigurerAdapter {
        private final JwtConfigure jwtConfigure;

        @Value("${jwt.login.matchers}")
        private String loginMatchers;

        public SecurityAPIConfig(JwtConfigure jwtConfigure) {
            this.jwtConfigure = jwtConfigure;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/api/**")
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(loginMatchers)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .apply(jwtConfigure);
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Bean
        protected PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder(12);
        }
    }


    @Configuration
    @Order(2)
    public static class SecurityGoogleConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .authorizeRequests()
                .antMatchers("/")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login();
        }
    }
}