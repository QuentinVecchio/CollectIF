/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Actions;

import TP1_SI.DAL.JpaUtil;
import TP1_SI.metier.model.Event;
import TP1_SI.metier.model.Location;
import TP1_SI.metier.model.Member;
import TP1_SI.metier.service.ServiceResult;
import TP1_SI.metier.service.Services;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author quentinvecchio
 */
public class GetLocationsAction extends Action {
    private List<Location> locations;
    private Services.Request_Error error;
    
    public GetLocationsAction() {
        locations = null;
        error = null;
    }
        
    @Override
    public boolean execute(HttpServletRequest request) {
        JpaUtil.creerEntityManager();
        ServiceResult<List<Location>, Services.Request_Error> locations_rslt = Services.ListLocations();
        JpaUtil.fermerEntityManager();
        error = locations_rslt.error;
        if(locations_rslt.error == Services.Request_Error.OK) {
            locations = locations_rslt.result;
            return true;
        }
        return false;
    }
    
    public List<Location> getLocations() {
        return locations;
    }
    
    public Services.Request_Error getError() {
       return error;
   }
}
