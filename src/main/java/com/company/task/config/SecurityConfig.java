package com.company.task.config;

import com.company.task.security.JwtAuthenticationFilter;
import com.company.task.security.JwtVerifierFilter;
import com.company.task.service.UserPrincipalService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserPrincipalService userPrincipalService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(UserPrincipalService userPrincipalService,
                          PasswordEncoder passwordEncoder,
                          AuthenticationEntryPoint authenticationEntryPoint,
                          AccessDeniedHandler accessDeniedHandler) {
        this.userPrincipalService = userPrincipalService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/v2/api-docs",
                "/swagger-resources/**",
                "/swagger-ui.html/**",
                "/webjars/**",
                "/h2-console/**"
        );
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth){
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtVerifierFilter(authenticationManager(), userPrincipalService))
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/users").hasAnyRole("admin")
                .antMatchers("/tasks").hasAnyRole("teacher")
                .antMatchers("/tasks/users/**").hasAnyRole("student")
                .antMatchers("/tasks/complete-status/**").hasAnyRole("student")
                .anyRequest().authenticated();
    }



    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userPrincipalService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean(value = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
