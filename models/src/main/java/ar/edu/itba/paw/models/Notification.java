package ar.edu.itba.paw.models;

import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "notification")
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_id_seq")
  @SequenceGenerator(
      sequenceName = "notification_id_seq",
      name = "notification_id_seq",
      allocationSize = 1)
  @Column(name = "notification_id")
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "appointment_id", nullable = false)
  private Appointment appointment;

  protected Notification() {
    // Solo para hibernate
  }

  public Notification(Long id, User user, Appointment appointment) {
    this.id = id;
    this.user = user;
    this.appointment = appointment;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Appointment getAppointment() {
    return appointment;
  }

  public void setAppointment(Appointment appointment) {
    this.appointment = appointment;
  }

  @Override
  public String toString() {
    return "Notification [" + "id=" + id + ", user=" + user + ", appointment=" + appointment + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, user, appointment);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Notification)) return false;
    Notification other = (Notification) obj;
    return Objects.equals(id, other.id);
  }
}
