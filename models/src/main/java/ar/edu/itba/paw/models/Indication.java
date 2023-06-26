package ar.edu.itba.paw.models;

import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "indication")
public class Indication {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "indication_indication_id_seq")
    @SequenceGenerator(
            sequenceName = "indication_indication_id_seq",
            name = "indication_indication_id_seq",
            allocationSize = 1
    )
    @Column(name = "indication_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "indication_date", nullable = false)
    private LocalDate date;

    @Column(name = "indication_description", length = 1000, nullable = false)
    private String description;

    protected Indication() {
        // Solo para hibernate
    }

    public Indication(
            Long id, Appointment appointment, User user, LocalDate date, String description) {
        this.id = id;
        this.appointment=appointment;
        this.user = user;
        this.date = date;
        this.description = description;
    }

    public Indication(Builder builder) {
        this.id = builder.id;
        this.user = builder.user;
        this.date = builder.date;
        this.description = builder.description;
        this.appointment = builder.appointment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Review ["
                + "id="
                + id
                + ", appointment="
                + appointment
                + ", user="
                + user
                + ", date="
                + date
                + ", description='"
                + description
                + ']';
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Indication)) return false;
        Indication other = (Indication) obj;
        return Objects.equals(id, other.id);
    }


    public static class Builder {
        private Appointment appointment;
        private String description;
        private LocalDate date;
        private User user;
        // default
        private Long id = null;

        public Builder(
                Appointment appointment, User user, LocalDate date, String description) {
            this.appointment= appointment;
            this.user= user;
            this.date = date;
            this.description = description;
        }

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Indication build() {
            return new Indication(this);
        }
    }

}
