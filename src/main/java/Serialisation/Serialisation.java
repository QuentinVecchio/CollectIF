/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Serialisation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author quentinvecchio
 */
public class Serialisation {
    private Gson gson;
    private HttpServletResponse response;
    private Object obj;
    
    public Serialisation(HttpServletResponse resp, Object o) {
        gson = new GsonBuilder().create();
        obj = o;
        response = resp;
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }
    
    public boolean execute() {
        if(obj != null && response != null) {
            try {
                response.getWriter().write(gson.toJson(obj));
                return true;
            } catch (IOException ex) {
                Logger.getLogger(Serialisation.class.getName()).log(Level.SEVERE, null, ex);     
            }
        }
        return false;
    }
    
    public HttpServletResponse getResponse() {
        return response;
    }
    
    public Object getObject() {
        return obj;
    }
    
    public void setResponse(HttpServletResponse resp) {
        response = resp;
    }
    
    public void setObject(Object o) {
        obj = o;
    }
    
}
