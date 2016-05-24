/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Actions;

import TP1_SI.DAL.JpaUtil;
import TP1_SI.metier.model.Event;
import TP1_SI.metier.service.ServiceResult;
import TP1_SI.metier.service.Services;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author quentinvecchio
 */
public class GetEventsByDateAction extends Action {
    
    private List<Event> events;
    private Services.Request_Error error;
    
    public GetEventsByDateAction() {
        events = null;
        error = null;
    }
        
    @Override
    public boolean execute(HttpServletRequest request) {
        String date = request.getParameter("date");
        if(date != null) {
            events = new LinkedList<Event>();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            try {
                java.util.Date d = sdf.parse(date);
                java.sql.Date sqlDate = new Date(d.getTime());
                JpaUtil.creerEntityManager();
                ServiceResult<List<Event>, Services.Request_Error> events_rslt = Services.ListAllEvents();
                JpaUtil.fermerEntityManager();
                for(Event ev : events_rslt.result) {
                    if(ev.getDate().equals(sqlDate)) {
                        events.add(ev);
                    }
                }
                return true;  
            } catch (ParseException ex) {
                error = Services.Request_Error.DATABASE_ERROR; 
            }
        } else {
            error = Services.Request_Error.DATABASE_ERROR;
        }
        return false;
    }
    
    public List<Event> getEvents() {
        return events;
    }
    
    public Services.Request_Error getError() {
       return error;
   }
    
}
