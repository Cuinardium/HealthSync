package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.*;
import ar.edu.itba.paw.webapp.auth.handlers.ForbiddenHandler;
import ar.edu.itba.paw.webapp.auth.handlers.HealthSyncAuthenticationEntryPoint;
import io.jsonwebtoken.security.Keys;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.FileCopyUtils;

@EnableWebSecurity
@ComponentScan({"ar.edu.itba.paw.webapp.auth"})
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

  @Value("classpath:openssl-key")
  private Resource openSSLKey;

  @Value("classpath:jwtPK")
  private Resource jwtPKRes;

  @Autowired private PawUserDetailsService userDetailsService;

  @Autowired private JwtFilter jwtFilter;

  @Autowired private BasicAuthFilter basicAuthFilter;

  @Bean
  public static PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthorizationFunctions authorizationFunctions() {
    return new AuthorizationFunctions();
  }

  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    return new ForbiddenHandler();
  }

  @Bean
  public AuthenticationEntryPoint authenticationEntryPoint() {
    return new HealthSyncAuthenticationEntryPoint();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean(name = "jwtPK")
  public Key jwtKey() throws IOException {
    return Keys.hmacShaKeyFor(
        FileCopyUtils.copyToString(new InputStreamReader(jwtPKRes.getInputStream()))
            .getBytes(StandardCharsets.UTF_8));
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }

  // TODO: agregar filtros por tipo de autenticacion clase 2 min 45
  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http
      .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
      .authorizeRequests()

        // ------------- Vacations ---------
        .antMatchers(HttpMethod.POST, "/api/doctors/{doctorId:\\d+}/vacations")
          .authenticated()
        .antMatchers(HttpMethod.DELETE, "/api/doctors/{doctorId:\\d+}/vacations/{vacationId:\\d+}")
          .authenticated()

        // ------------- Reviews  ----------
        .antMatchers(HttpMethod.POST, "/api/doctors/{doctorId:\\d+}/reviews")
            .hasRole(UserRole.ROLE_PATIENT.getRoleNameWithoutPrefix())

        // ------------- Indications --------
        .antMatchers(HttpMethod.GET, "/api/appointments/{appointmentId:\\d+}/indications")
          .authenticated()
        .antMatchers(HttpMethod.POST, "/api/appointments/{appointmentId:\\d+}/indications")
          .authenticated()

        // indications/{id}
        .antMatchers(
            HttpMethod.GET,
            "/api/appointments/{appointmentId:\\d+}/indications/{indicationId:\\d+}")
          .authenticated()

        // ------------- Files --------------
        .antMatchers(HttpMethod.GET, "/api/appointments/{appointmentId:\\d+}/files/{fileId:\\d+}")
          .authenticated()

        // ------------- Notifications -----
        .antMatchers(HttpMethod.GET, "/api/notifications")
          .authenticated()

        // notifications/{id}
        .antMatchers(HttpMethod.GET, "/api/notifications/{notificationId:\\d+}")
          .authenticated()
        .antMatchers(HttpMethod.DELETE, "/api/notifications/{notificationId:\\d+}")
          .authenticated()

        // Permit all other
        .antMatchers("/api/**")
          .permitAll()
        .and()
      .exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint())
        .accessDeniedHandler(accessDeniedHandler())
        .and()
      .addFilterBefore(basicAuthFilter, UsernamePasswordAuthenticationFilter.class)
      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
      .csrf().disable();
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
