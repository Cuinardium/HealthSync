package ar.edu.itba.paw.webapp.config;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
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

  @Autowired 
  private UserDetailsService userDetailsService;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http.sessionManagement()
        .invalidSessionUrl("/login")
        .and()
        .authorizeRequests()
        .antMatchers("/", "/login", "/register")
        .anonymous()
            .antMatchers("/doctorDashboard").hasRole("USER")
            //.antMatchers("").hasRole("DOCTOR") TODO SETEAR PAGINAS PARA SOLO DOCTORS
        .antMatchers("/**")
        .authenticated()
        .and()
        .formLogin()
        .loginPage("/login")
        .usernameParameter("email")
        .passwordParameter("password")
        .defaultSuccessUrl("/doctorDashboard", false)
        .and()
        .rememberMe()
        .rememberMeParameter("rememberme")
        .userDetailsService(userDetailsService)
        .key("NO HAGAS ESTO")
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
