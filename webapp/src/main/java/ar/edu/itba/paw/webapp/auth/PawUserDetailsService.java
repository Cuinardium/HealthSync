package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;

@Component
public class PawUserDetailsService implements UserDetailsService {

    private UserService us;

    
    @Autowired
    public PawUserDetailsService(final UserService us){
        this.us=us;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException{
        final User user= us.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("No user for email " + username));

        //ROLES
        final Collection<GrantedAuthority> authorities= new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        authorities.add(new SimpleGrantedAuthority("ROLE_DOCTOR"));

        return new PawAuthUserDetails(user.getEmail(), user.getPassword(), authorities);
    }
}
