package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.User;
import io.jsonwebtoken.*;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  public static final String TOKEN_HEADER = "X-JWT";
  public static final String REFRESH_TOKEN_HEADER = "X-Refresh";

  private static final long accessTokenValidity = 24 * 60 * 60 * 1000; // 24 hours in milliseconds
  private static final long refreshTokenValidity =
      30 * accessTokenValidity; // 30 days in milliseconds

  @Autowired private PawUserDetailsService userDetailsService;
  @Autowired private Key jwtPK;

  public JwtUtil() {}

  /** jws: Json Web Signature (https://datatracker.ietf.org/doc/html/rfc7515) */
  public UserDetails parseToken(String jws) {
    try {
      final Claims claims =
          Jwts.parserBuilder().setSigningKey(jwtPK).build().parseClaimsJws(jws).getBody();

      if (new Date(System.currentTimeMillis()).after(claims.getExpiration())) {
        return null;
      }

      final String username = claims.getSubject();

      return userDetailsService.loadUserByUsername(username);
    } catch (Exception e) {
      return null;
    }
  }

  public String generateAccessToken(User user) {
    Claims claims = Jwts.claims();
    claims.setSubject(user.getEmail());

    UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
    UserRole role =
        userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_DOCTOR"))
            ? UserRole.ROLE_DOCTOR
            : UserRole.ROLE_PATIENT;

    claims.put("id", user.getId().toString());
    claims.put("authorization", role.toString());
    claims.put("refresh", false);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
        .signWith(jwtPK)
        .compact();
  }

  private Claims parseJwtClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(jwtPK).build().parseClaimsJws(token).getBody();
  }

  public boolean validateAccessToken(String token) {
    final Claims claims = parseJwtClaims(token);
    return claims.get("refresh", Boolean.class) != null
        && !(new Date(System.currentTimeMillis()).after(claims.getExpiration()));
  }

  public String generateRefreshToken(User user) {
    Claims claims = Jwts.claims();
    claims.setSubject(user.getEmail());
    claims.put("refresh", true);
    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
        .signWith(jwtPK)
        .compact();
  }

  public boolean isRefreshToken(String token) {
    try {
      final Claims claims =
          Jwts.parserBuilder().setSigningKey(jwtPK).build().parseClaimsJws(token).getBody();
      return claims.get("refresh", Boolean.class);
    } catch (Exception e) {
      return false;
    }
  }
}
