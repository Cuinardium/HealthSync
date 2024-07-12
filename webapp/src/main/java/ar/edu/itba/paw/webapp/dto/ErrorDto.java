package ar.edu.itba.paw.webapp.dto;

public class ErrorDto {

    private String message;

    public ErrorDto() {}

    private ErrorDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ErrorDto fromMessage(String message) {
        return new ErrorDto(message);
    }
}
