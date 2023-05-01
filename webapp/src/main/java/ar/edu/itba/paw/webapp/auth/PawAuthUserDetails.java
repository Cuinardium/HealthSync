package ar.edu.itba.paw.webapp.auth;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class PawAuthUserDetails extends User {

  private static final long serialVersionUID = 5465377571748379930L;

  private final long id;

  public PawAuthUserDetails(
      String username,
      String password,
      long id,
      Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);
    this.id = id;
  }

  public PawAuthUserDetails(
      String username,
      String password,
      long id,
      boolean enabled,
      boolean accountNonExpired,
      boolean credentialsNonExpired,
      boolean accountNonLocked,
      Collection<? extends GrantedAuthority> authorities) {
    super(
        username,
        password,
        enabled,
        accountNonExpired,
        credentialsNonExpired,
        accountNonLocked,
        authorities);
    this.id = id;
  }

  public long getId() {
    return id;
  }

  public static UserRoles getRole(){
    if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PATIENT"))){
      return UserRoles.ROLE_PATIENT;
    }
    if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_DOCTOR"))){
      return UserRoles.ROLE_DOCTOR;
    }
    return UserRoles.ROLE_NULL;
  }
}
