package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "notification")
public class Notification {

    @EmbeddedId private NotificationId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    @MapsId("appointmentId")
    private Appointment appointment;

    protected Notification(){
        //Solo para hibernate
    }

    public Notification(Long userId, Long appointmentId) {
        this.id = new Notification.NotificationId(userId, appointmentId);
    }

    public NotificationId getId() {
        return id;
    }

    public void setId(NotificationId id) {
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
        return "Notification [id=" + id + "]";
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
        if (!(obj instanceof Notification)) return false;
        Notification other = (Notification) obj;
        return Objects.equals(this.id, other.id);
    }

    @Embeddable
    public static class NotificationId implements Serializable{

        @Column(name = "user_id", nullable = false)
        private Long userId;

        @Column(name = "appointment_id", nullable = false)
        private Long appointmentId;

        protected NotificationId(){
            //Solo para hibernate
        }

        public NotificationId(Long userId, Long appointmentId) {
            this.userId = userId;
            this.appointmentId = appointmentId;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getAppointmentId() {
            return appointmentId;
        }

        public void setAppointmentId(Long appointmentId) {
            this.appointmentId = appointmentId;
        }

        @Override
        public String toString() {
            return "NotificationId [userId="
                    + userId
                    + ", appointmentId="
                    + appointmentId
                    + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((userId == null) ? 0 : userId.hashCode());
            result = prime * result + ((appointmentId == null) ? 0 : appointmentId.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Notification.NotificationId)) return false;
            Notification.NotificationId other = (Notification.NotificationId) obj;
            return Objects.equals(this.userId, other.userId)
                    && Objects.equals(this.appointmentId, other.appointmentId);
        }
    }


}
