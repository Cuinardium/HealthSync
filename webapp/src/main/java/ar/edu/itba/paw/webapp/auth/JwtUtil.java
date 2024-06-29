package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  private static final long accessTokenValidity = 24 * 60 * 60 * 1000; // 24 hours in milliseconds
  private static final long refreshTokenValidity =
      30 * accessTokenValidity; // 30 days in milliseconds

  @Autowired private PawUserDetailsService userDetailsService;
  private final JwtParserBuilder jwtParserBuilder;
  private final Key SECRET_KEY;

  public JwtUtil() {
    // Cuini Franco Gonza Juan
    final String secretKeyBase =
        "CFGJCFGJCFGJCFGJCFGJCFGJCFGJCFGJ"; // 32 characters * 8 bits per character = 256 bits
    this.SECRET_KEY = Keys.hmacShaKeyFor(secretKeyBase.getBytes(StandardCharsets.UTF_8));
    this.jwtParserBuilder = Jwts.parserBuilder().setSigningKey(this.SECRET_KEY);
  }

  public String generateAccessToken(User user) {
    return Jwts.builder()
        .setSubject(user.getEmail())
        .setIssuer("paw-2023a-02")
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
        .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
        .compact();
  }
  /** jws: Json Web Signature (https://datatracker.ietf.org/doc/html/rfc7515) */
  public UserDetails parseToken(String jws) {
    try {
      final Claims claims =
          Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(jws).getBody();

      if (new Date(System.currentTimeMillis()).after(claims.getExpiration())) {
        return null;
      }

      final String username = claims.getSubject();

      return userDetailsService.loadUserByUsername(username);
    } catch (Exception e) {
      return null;
    }
  }

  public String generateAccessToken(User user, String baseUrl) {
    Claims claims = Jwts.claims();
    claims.setSubject(user.getEmail());
    claims.put("userURL", baseUrl + "/users/" + user.getId());
    claims.put(
        "authorization", userDetailsService.loadUserByUsername(user.getEmail()).getAuthorities());
    claims.put("refresh", false);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
        .signWith(SECRET_KEY)
        .compact();
  }

  private Claims parseJwtClaims(String token) {
    return jwtParserBuilder.build().parseClaimsJws(token).getBody();
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
        .signWith(SECRET_KEY)
        .compact();
  }

  public boolean isRefreshToken(String token) {
    try {
      final Claims claims =
          Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
      return claims.get("refresh", Boolean.class);
    } catch (Exception e) {
      return false;
    }
  }
}
