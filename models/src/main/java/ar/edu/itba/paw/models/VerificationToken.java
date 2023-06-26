package ar.edu.itba.paw.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "verification_token")
public class VerificationToken {
  private static final int EXPIRATION = 60 * 24;

  @Id
  @GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "verification_token_verification_id_seq"
  )
  @SequenceGenerator(
    sequenceName = "verification_token_verification_id_seq",
    name = "verification_token_verification_id_seq",
    allocationSize = 1
  )
  @Column(name = "verification_id")
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(length = 32, nullable = false)
  private String token;

  protected VerificationToken() {
    // solo para hibernate
  }

  public VerificationToken(Long id, User user, String token) {
    this.id = id;
    this.user = user;
    this.token = token;
  }

  public VerificationToken(Builder builder) {
    this.id = builder.id;
    this.user = builder.user;
    this.token = builder.token;
  }

  // TODO: implement this
  private String generateToken() {
    return new String();
  }

  public class Builder {
    private User user;
    // defaults
    private Long id = null;
    private String token = generateToken();

    public Builder(User user) {
      this.user = user;
    }

    public Builder id(long id) {
      this.id = id;
      return this;
    }

    public Builder token(String token) {
      this.token = token;
      return this;
    }

    public VerificationToken build() {
      return new VerificationToken(this);
    }
  }
}
