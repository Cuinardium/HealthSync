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

/** https://github.com/jwtk/jjwt https://github.com/Yoh0xFF/java-spring-security-example */
@Component
public class PawUserDetailsService implements UserDetailsService {

  private final UserService userService;
  private final DoctorService doctorService;

  private final PatientService patientService;

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

    UserRole role = getUserRole(user);
    // ROLES
    final Collection<GrantedAuthority> authorities = new HashSet<>();
    authorities.add(new SimpleGrantedAuthority(role.getRoleName()));

    return new PawAuthUserDetails(
        user.getEmail(), user.getPassword(), user.getId(), user.getIsVerified(), authorities);
  }

  private UserRole getUserRole(User user) {
    if (doctorService.getDoctorById(user.getId()).isPresent()) {
      return UserRole.ROLE_DOCTOR;
    }
    if (patientService.getPatientById(user.getId()).isPresent()) {
      return UserRole.ROLE_PATIENT;
    }
    return UserRole.ROLE_NULL;
  }
}
