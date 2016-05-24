/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Actions;

import Servlets.ServletCollectif;
import TP1_SI.DAL.JpaUtil;
import TP1_SI.metier.model.Event;
import TP1_SI.metier.service.ServiceResult;
import TP1_SI.metier.service.Services;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author quentinvecchio
 */
public class GetEventAtLocationAction extends Action {
    private List<Event> events;
    private Services.Request_Error error;
 
    public GetEventAtLocationAction() {
        events = null;
        error = null;
    }
    
    @Override
    public boolean execute(HttpServletRequest request) {
        String nameLocation = request.getParameter("location");
        String date = request.getParameter("date");
        if( nameLocation != null && date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            try {
                java.util.Date d = sdf.parse(date);
                java.sql.Date sqlDate = new Date(d.getTime());
                JpaUtil.creerEntityManager();
                ServiceResult<List<Event>, Services.Request_Error> available_events_rslt = Services.ListAllEvents();
                JpaUtil.fermerEntityManager();
                if(available_events_rslt.error == Services.Request_Error.OK) {
                    events = new LinkedList<Event>();
                    for(Event e : available_events_rslt.result) {
                        if(e.getLocation() != null) {
                            if(e.getLocation().getDescription().equals(nameLocation) && e.getDate().equals(d)) {
                                events.add(e);
                            }
                        }
                    }
                    return true;
                } else {
                    error = available_events_rslt.error;
                } 
            } catch (ParseException ex) {
                Logger.getLogger(ServletCollectif.class.getName()).log(Level.SEVERE, null, ex);
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