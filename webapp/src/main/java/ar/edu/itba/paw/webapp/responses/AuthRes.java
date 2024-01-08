package ar.edu.itba.paw.webapp.responses;

public class AuthRes {
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String token;

    private String email;

    public AuthRes(String token, String email){
        this.token= token;
        this.email=email;
    }

    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token=token;
    }
}
