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
import java.sql.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author quentinvecchio
 */
public class CreationEventAction extends Action {
    private Event event;
    private Services.Request_Error error;
    
    public CreationEventAction() {
        event = null;
        error = null;
    }
    
    @Override
    public boolean execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        int idActivity = Integer.parseInt(request.getParameter("idAct"));
        Member memb = null;
        memb = (Member) session.getAttribute("member");
        String date = request.getParameter("date");
        if(date != null && memb != null) {
            int year = Integer.parseInt(date.split("/")[2]);
            int month = Integer.parseInt(date.split("/")[0]) - 1;
            int day = Integer.parseInt(date.split("/")[1]);
            int idUser = memb.getId().intValue();
            JpaUtil.creerEntityManager();
            Date d = new Date(year-1900,month,day);
            ServiceResult<Event, Services.Request_Error> rep = Services.CreateEvent(idUser, idActivity, d);
            JpaUtil.fermerEntityManager();
            error = rep.error;
            if(rep.error == Services.Request_Error.OK) {
                event = rep.result;
                return true;
            }
        } else {
            error = Services.Request_Error.DATABASE_ERROR;
        }
        return false;
    }
    
    public Event getEvent() {
        return event;
    }
    
    public Services.Request_Error getError() {
       return error;
    }
}
