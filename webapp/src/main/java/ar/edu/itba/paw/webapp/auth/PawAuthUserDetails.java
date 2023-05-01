package ar.edu.itba.paw.webapp.auth;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
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
}
