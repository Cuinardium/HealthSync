package ar.edu.itba.paw.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "verification_token")
public class VerificationToken {
  private static final int EXPIRATION = 60 * 24;

  @Id private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  @MapsId
  private User user;

  @Column(length = 32, nullable = false)
  private String token;

  @Column(nullable = false)
  private LocalDateTime expiryDateTime;

  protected VerificationToken() {
    // solo para hibernate
  }

  public VerificationToken(User user, String token) {
    this.id = user.getId();
    this.user = user;
    this.token = token;
    this.expiryDateTime = LocalDateTime.now().plusMinutes(EXPIRATION);
  }

  public Long getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public String getToken() {
    return token;
  }

  public LocalDateTime getExpiryDateTime() {
    return expiryDateTime;
  }

  public void setUser(User user) {
    this.id = user.getId();
    this.user = user;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public void setExpiryDateTime(LocalDateTime expiryDateTime) {
    this.expiryDateTime = expiryDateTime;
  }

  public void renewExpiryDateTime() {
    this.expiryDateTime = LocalDateTime.now().plusMinutes(EXPIRATION);
  }

  public boolean isExpired() {
    return LocalDateTime.now().isAfter(this.expiryDateTime);
  }
}
