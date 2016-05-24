/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Actions;

import Servlets.ServletCollectif;
import TP1_SI.DAL.JpaUtil;
import TP1_SI.metier.model.Event;
import TP1_SI.metier.model.Member;
import TP1_SI.metier.service.ServiceResult;
import static TP1_SI.metier.service.ServiceTechnique.Distance;
import TP1_SI.metier.service.Services;
import com.google.maps.model.LatLng;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author quentinvecchio
 */
public class GetDistanceMoyenneAction extends Action {
    private Services.Request_Error error;
    private List<String> reponse;
    
    public GetDistanceMoyenneAction() {
        error = null;
    }
    
    @Override
    public boolean execute(HttpServletRequest request) {
        String idActivite = request.getParameter("idEvent");
        if( idActivite != null ) {
            int id = Integer.parseInt(idActivite);
            boolean trouve = false;
            JpaUtil.creerEntityManager();
            ServiceResult<List<Event>, Services.Request_Error> available_events_rslt = Services.ListAllEvents();
            error = available_events_rslt.error;
            if(available_events_rslt.error == Services.Request_Error.OK) {
                List<Event> available_events = available_events_rslt.result;
                for(Event e : available_events) {
                    if(e.getId() == id) {
                        trouve = true;
                        if(e.getLocation() != null) {
                            double disMoyenne = 0;
                            LatLng l1 = new LatLng(e.getLocation().getLatitude(),e.getLocation().getLongitude());
                            for(Member m : e.getMembers()) {
                                LatLng l2 = new LatLng(m.getLatitude(), m.getLongitude());
                                try {
                                    System.out.println(l1 + " " + l2);
                                    disMoyenne += Distance(l1,l2);
                                } catch (Exception ex) {
                                    Logger.getLogger(ServletCollectif.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } 
                            disMoyenne /= e.getMembers().size();
                            disMoyenne /= 1000;
                            DecimalFormat df = new DecimalFormat("#.##"); 
                            List<String> rep = new LinkedList<String>();
                            String disFormat = df.format(disMoyenne);
                            rep.add(Integer.toString(id));
                            rep.add(disFormat);
                            reponse = rep;
                            return true;
                        } else {
                            error = Services.Request_Error.WRONG_LIEU_ID;
                        }
                    }
                }
            }
            JpaUtil.fermerEntityManager();
        } else {
            error = Services.Request_Error.WRONG_ACTIVITY_ID;
        }
        return false;
    }

    public List<String> getAnswer() {
        return reponse;
    }
    
    public Services.Request_Error getError() {
       return error;
    }
}
