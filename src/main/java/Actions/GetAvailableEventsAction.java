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
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author quentinvecchio
 */
public class GetAvailableEventsAction extends Action {
    private List<Event> events;
    private Services.Request_Error error;
    
    public GetAvailableEventsAction() {
        events = null;
        error = null;
    }
        
    @Override
    public boolean execute(HttpServletRequest request) {
        JpaUtil.creerEntityManager();
        ServiceResult<List<Event>, Services.Request_Error> events_rslt = Services.ListAvailableEvents();
        JpaUtil.fermerEntityManager();
        error = events_rslt.error;
        if(events_rslt.error == Services.Request_Error.OK) {
            events = events_rslt.result;
            return true;
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
