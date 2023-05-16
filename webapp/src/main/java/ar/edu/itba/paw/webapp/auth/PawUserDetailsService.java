package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import java.util.Collection;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class PawUserDetailsService implements UserDetailsService {

  private UserService userService;
  private DoctorService doctorService;

  private PatientService patientService;

  @Autowired
  public PawUserDetailsService(
      final UserService us, final DoctorService ds, final PatientService ps) {
    this.userService = us;
    this.doctorService = ds;
    this.patientService = ps;
  }

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    final User user =
        userService
            .getUserByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("No user for email " + username));

    String role = getUserRole(user);
    // ROLES
    final Collection<GrantedAuthority> authorities = new HashSet<>();
    authorities.add(new SimpleGrantedAuthority(role));

    return new PawAuthUserDetails(user.getEmail(), user.getPassword(), user.getId(), authorities);
  }

  private String getUserRole(User user) {
    if (doctorService.getDoctorById(user.getId()).isPresent()) {
      return "ROLE_DOCTOR";
    }
    if (patientService.getPatientById(user.getId()).isPresent()) {
      return "ROLE_PATIENT";
    }
    return null;
  }
}
