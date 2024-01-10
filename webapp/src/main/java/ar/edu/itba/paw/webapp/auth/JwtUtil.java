package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  private static final long accessTokenValidity = 24 * 60 * 60 * 1000;
  private final JwtParserBuilder jwtParserBuilder;
  private final Key SECRET_KEY;
  private final String TOKEN_HEADER = "Authorization";
  private final String TOKEN_PREFIX = "Bearer ";

  public JwtUtil() {
    final String secretKeyBase = "CFGJ"; // Cuini Franco Gonza Juan
    this.SECRET_KEY = Keys.hmacShaKeyFor(secretKeyBase.getBytes(StandardCharsets.UTF_8));
    this.jwtParserBuilder = Jwts.parserBuilder().setSigningKey(this.SECRET_KEY);
  }

  public String generateAccessToken(User user) {
    return Jwts.builder()
        .setSubject(String.format("%s,%s", user.getId(), user.getEmail()))
        .setIssuer("paw-2023a-02")
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
        .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
        .compact();
  }

  private Claims parseJwtClaims(String token) {
    return jwtParserBuilder.build().parseClaimsJws(token).getBody();
  }

  public Claims resolveClaims(HttpServletRequest req) {

    String token = resolveToken(req);
    if (token != null) {
      return parseJwtClaims(token);
    }
    return null;
  }

  public String resolveToken(HttpServletRequest request) {

    String bearerToken = request.getHeader(TOKEN_HEADER);
    if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
      return bearerToken.substring(TOKEN_PREFIX.length());
    }
    return null;
  }

  public boolean validateClaims(Claims claims) {
    return claims.getExpiration().after(new Date());
  }

  public boolean isTokenRefresh(String token) {
    return getClaimFromToken(token, c -> c.get("refresh", Boolean.class) != null);
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = parseClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims parseClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
  }
}
