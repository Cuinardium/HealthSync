package ar.edu.itba.paw.webapp.auth.utils;

import javax.servlet.http.HttpServletRequest;

public class AuthUtils {
    public static String getBaseUrl(HttpServletRequest request){
        return  request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/api";
    }
}
