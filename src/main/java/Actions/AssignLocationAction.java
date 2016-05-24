/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Actions;

import TP1_SI.DAL.JpaUtil;
import TP1_SI.metier.model.Event;
import TP1_SI.metier.model.Location;
import TP1_SI.metier.service.ServiceResult;
import TP1_SI.metier.service.Services;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author quentinvecchio
 */
public class AssignLocationAction extends Action {
    private Services.Request_Error error;
 
    public AssignLocationAction() {
        error = null;
    }
    
    @Override
    public boolean execute(HttpServletRequest request) {
        String nameLocation = request.getParameter("location");
        int id = Integer.parseInt(request.getParameter("idAct"));
        if( nameLocation != null) {
            JpaUtil.creerEntityManager();
            ServiceResult<List<Event>, Services.Request_Error> available_events_rslt = Services.ListAllEvents();
            ServiceResult<List<Location>, Services.Request_Error> available_locations_rslt = Services.ListLocations();
            if(available_events_rslt.error == Services.Request_Error.OK && available_locations_rslt.error == Services.Request_Error.OK) {
                for(Event e: available_events_rslt.result) {
                    if(e.getId() == id) {
                        for(Location l: available_locations_rslt.result) {
                            if(l.getDescription().equals(nameLocation)) {
                                error = Services.AssignLocationToEvent(l.getId(), e.getId());
                                return true;
                            }
                        }
                    }
                }
            } else {
                error = Services.Request_Error.DATABASE_ERROR;
            }   
            JpaUtil.fermerEntityManager();
        } else {
            error = Services.Request_Error.DATABASE_ERROR;
        }
        return false;
    }
    
    public Services.Request_Error getError() {
       return error;
    }
}
