package ar.edu.itba.paw.webapp.mediaType;

public enum VndType {;

    // ----- Patient ------------
    public static final String APPLICATION_PATIENT = "application/vnd.patient.v1+json";

    // ----- Vacation -----------
    public static final String APPLICATION_VACATION = "application/vnd.vacation.v1+json";
    public static final String APPLICATION_VACATION_LIST = "application/vnd.vacation-list.v1+json";
    public static final String APPLICATION_VACATION_FORM =  "application/vnd.vacation-form.v1+json";

    // ----- Indications --------
    public static final String APPLICATION_INDICATION = "application/vnd.indication.v1+json";
    public static final String APPLICATION_INDICATION_LIST = "application/vnd.indication-list.v1+json";

    // ----- Notifications ------
    public static final String APPLICATION_NOTIFICATION = "application/vnd.notification.v1+json";
    public static final String APPLICATION_NOTIFICATIONS_LIST = "application/vnd.notifications-list.v1+json";

    // ----- Reviews ------------
    public static final String APPLICATION_REVIEW = "application/vnd.review.v1+json";
    public static final String APPLICATION_REVIEWS_LIST = "application/vnd.reviews-list.v1+json";

    // ----- Health Insurances --
    public static final String APPLICATION_HEALTH_INSURANCE = "application/vnd.health-insurance.v1+json";
    public static final String APPLICATION_HEALTH_INSURANCE_LIST = "application/vnd.health-insurance-list.v1+json";

    // ----- Error --------------
    public static final String APPLICATION_ERROR = "application/vnd.error.v1+json";
    public static final String APPLICATION_VALIDATION_ERROR_LIST = "application/vnd.validation-error-list.v1+json";
}
