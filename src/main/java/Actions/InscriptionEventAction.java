/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Actions;

import TP1_SI.DAL.JpaUtil;
import TP1_SI.metier.model.Member;
import TP1_SI.metier.service.Services;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author quentinvecchio
 */
public class InscriptionEventAction extends Action {
    private Services.Request_Error error;
    
    public InscriptionEventAction() {
        error = null;
    }
    
    @Override
    public boolean execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String idActivity = request.getParameter("idAct");
        Member member = null;
        member = (Member) session.getAttribute("member");
        if(idActivity != null && member != null) {
            int idUser = member.getId().intValue();
            JpaUtil.creerEntityManager();
            error = Services.JoinEvent(idUser, Integer.parseInt(idActivity));
            JpaUtil.fermerEntityManager();
            if(error == Services.Request_Error.OK)
                return true;
        } else {
            error = Services.Request_Error.WRONG_ACTIVITY_ID;
        }
        return false;
    }

    public Services.Request_Error getError() {
       return error;
    }
}
