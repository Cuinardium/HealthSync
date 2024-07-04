package ar.edu.itba.paw.webapp.auth;

import java.util.Collection;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
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
      boolean enabled,
      Collection<? extends GrantedAuthority> authorities) {
    super(username, password, enabled, true, true, true, authorities);
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

  public static long getCurrentUserId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && !(auth instanceof AnonymousAuthenticationToken) && auth.isAuthenticated()) {
      PawAuthUserDetails user = (PawAuthUserDetails) auth.getPrincipal();
      return user.getId();
    }
    return -1;
  }

  public static PawAuthUserDetails getCurrentUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && !(auth instanceof AnonymousAuthenticationToken) && auth.isAuthenticated()) {
      return (PawAuthUserDetails) auth.getPrincipal();
    }
    return null;
  }

  public static UserRole getRole() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth != null && !(auth instanceof AnonymousAuthenticationToken) && auth.isAuthenticated()) {
      Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
      if (authorities.contains(new SimpleGrantedAuthority("ROLE_PATIENT"))) {
        return UserRole.ROLE_PATIENT;
      }
      if (authorities.contains(new SimpleGrantedAuthority("ROLE_DOCTOR"))) {
        return UserRole.ROLE_DOCTOR;
      }
    }
    return UserRole.ROLE_NULL;
  }

  public long getId() {
    return id;
  }
}
