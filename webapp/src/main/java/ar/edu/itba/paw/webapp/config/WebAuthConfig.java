package ar.edu.itba.paw.webapp.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@ComponentScan({"ar.edu.itba.paw.webapp.auth"})
@Configuration
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

  @Value("classpath:openssl-key")
  private Resource openSSLKey;

  @Autowired private UserDetailsService userDetailsService;

  @Bean
  public static PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http.sessionManagement()
        .invalidSessionUrl("/")
        .and()
        .authorizeRequests()
            .antMatchers("/appointment", "/patient-edit")
            .hasRole("PATIENT")
        .antMatchers("/doctorDashboard","/", "detailed_doctor")
        .permitAll()
        .antMatchers("/login", "/patient-register", "/doctor-register")
        .anonymous()
        .antMatchers("/doctor-edit").hasRole("DOCTOR")
        .antMatchers("/**")
        .authenticated()
            .and()
        .formLogin()
        .loginPage("/login")
        .usernameParameter("email")
        .passwordParameter("password")
        .defaultSuccessUrl("/", false)
        .and()
        .rememberMe()
        .rememberMeParameter("rememberme")
        .userDetailsService(userDetailsService)
        .key(getKey())
        .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30))
        .and()
        .logout()
        .logoutUrl("/logout")
        .logoutSuccessUrl("/login")
        .and()
        .exceptionHandling()
        .accessDeniedPage("/errors/403")
        .and()
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
