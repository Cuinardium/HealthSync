package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.*;
import ar.edu.itba.paw.webapp.auth.handlers.ForbiddenHandler;
import ar.edu.itba.paw.webapp.auth.handlers.HealthSyncAuthenticationEntryPoint;
import io.jsonwebtoken.security.Keys;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.Key;
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

        // ------------- Tokens -----------
        // verification
        .antMatchers(HttpMethod.POST, "/api/tokens/verification")
          .anonymous()
        .antMatchers(HttpMethod.PUT, "/api/tokens/verification/{token}")
          .anonymous()
      
        // ------------ Doctors -----------
        .antMatchers(HttpMethod.GET, "/api/doctors")
          .permitAll()
        .antMatchers(HttpMethod.POST, "/api/doctors")
          .permitAll()

        // doctors/{id}
        .antMatchers(HttpMethod.GET, "/api/doctors/{doctorId:\\d+}")
          .permitAll()

        // doctors/{id}/attendinghours
        .antMatchers(HttpMethod.GET, "/api/doctors/{doctorId:\\d+}/attendinghours")
          .permitAll()

        // ------------- Reviews  ----------
        .antMatchers(HttpMethod.POST, "/api/doctors/{doctorId:\\d+}/reviews")
            .hasRole(UserRole.ROLE_PATIENT.getRoleNameWithoutPrefix())

        // ------------- Specialities -------
        .antMatchers(HttpMethod.GET, "/api/specialities")
          .permitAll()
        // specialities/{id}
        .antMatchers(HttpMethod.GET, "/api/specialities/{specialityId:\\d+}")
          .permitAll()

        // ------------- Cities -------------
        .antMatchers(HttpMethod.GET, "/api/cities")
          .permitAll()

        // ------------- Health Insurances --
        .antMatchers(HttpMethod.GET, "/api/health-insurances")
          .permitAll()
        // health-insurances/{id}
        .antMatchers(HttpMethod.GET, "/api/health-insurances/{healthInsuranceId:\\d+}")
          .permitAll()

        // Authenticate all other
        .antMatchers("/api/**")
          .authenticated()
        .and()
      .exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint())
        .accessDeniedHandler(accessDeniedHandler())
        .and()
      .addFilterBefore(basicAuthFilter, UsernamePasswordAuthenticationFilter.class)
      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
      .csrf().disable();
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
