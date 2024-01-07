package ar.edu.itba.paw.webapp.responses;

public class AuthRes {
    private String token;

    public AuthRes(String token){
        this.token= token;
    }

    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token=token;
    }
}
