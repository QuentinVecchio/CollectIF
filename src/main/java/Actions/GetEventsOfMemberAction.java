/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Actions;

import TP1_SI.DAL.JpaUtil;
import TP1_SI.metier.model.Event;
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
public class GetEventsOfMemberAction extends Action{
    private List<Event> events;
    private Services.Request_Error error;
    
    public GetEventsOfMemberAction() {
        events = null;
        error = null;
    }
        
    @Override
    public boolean execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Member memb = (Member) session.getAttribute("member");
        if(memb != null) {
            JpaUtil.creerEntityManager();
            ServiceResult<List<Event>, Services.Request_Error> events_rslt = Services.ListEventsOfMember(memb.getId());
            JpaUtil.fermerEntityManager();
            error = events_rslt.error;
            if(events_rslt.error == Services.Request_Error.OK) {
                events = events_rslt.result;
                return true;
            }   
        } else {
            error = Services.Request_Error.WRONG_MEMBER_ID;
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
