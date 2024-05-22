package ar.edu.itba.paw.webapp.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import ar.edu.itba.paw.webapp.auth.BasicAuthFilter;
import ar.edu.itba.paw.webapp.auth.JwtFilter;
import ar.edu.itba.paw.webapp.auth.PawUserDetailsService;
import ar.edu.itba.paw.webapp.auth.handlers.HealthSyncAuthenticationEntryPoint;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@ComponentScan({"ar.edu.itba.paw.webapp.auth"})
@Configuration
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

  @Value("classpath:openssl-key")
  private Resource openSSLKey;

  @Autowired private PawUserDetailsService userDetailsService;

  @Autowired
  private JwtFilter jwtFilter;

  @Autowired
  private BasicAuthFilter basicAuthFilter;

  @Bean
  public static PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationEntryPoint authenticationEntryPoint () {
    return new HealthSyncAuthenticationEntryPoint();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }

  // TODO: agregar filtros por tipo de autenticacion clase 2 min 45
  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        // .invalidSessionUrl("/")
        .and()
        .authorizeRequests()
        // .antMatchers(HttpMethod.POST, "/{id:\\d+}/detailed-doctor")
        // .hasRole("PATIENT")
        // .antMatchers("/doctor-dashboard", "/", "/{id:\\d+}/detailed-doctor")
        // .permitAll()
        // .antMatchers("/patient-edit")
        // .hasRole("PATIENT")
        // .antMatchers("/login", "/patient-register", "/doctor-register", "/verify",
        // "/renew-token")
        // .anonymous()
        // .antMatchers("/doctor-edit")
        // .hasRole("DOCTOR")
        .antMatchers("/")
            .authenticated()
        .anyRequest().authenticated()
        .and()
        .exceptionHandling()
        .accessDeniedPage("/errors/403")
        .and()
            .addFilterBefore(basicAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .csrf()
        .disable();
  }

  private String getKey() throws IOException {
    byte[] bytes = FileUtils.readFileToByteArray(openSSLKey.getFile());
    return new String(bytes, StandardCharsets.UTF_8);
  }

  @Override
  public void configure(final WebSecurity web) throws Exception {
    web.ignoring()
        .antMatchers(
            "/css/**",
            "/js/**",
            "/img/**",
            "/icons/**",
            "/errors/**"); // TODO ver cosas con las que no matcheen
  }
}
