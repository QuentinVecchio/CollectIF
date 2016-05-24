/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Actions;

import TP1_SI.DAL.JpaUtil;
import TP1_SI.metier.model.Member;
import TP1_SI.metier.service.ServiceResult;
import TP1_SI.metier.service.Services;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author quentinvecchio
 */
public class ConnexionAction extends Action {
    private Member memb;
    private Services.ConnexionError error;
    
    public ConnexionAction() {
        memb = null;
        error = null;
    }
    
    @Override
    public boolean execute(HttpServletRequest request) {
        JpaUtil.creerEntityManager();
        boolean res = false;
        String mail = request.getParameter("email");
        if(mail != null) {
            ServiceResult<Member, Services.ConnexionError> resultat = Services.Connexion(mail);
            error = resultat.error;
            if(resultat.error == Services.ConnexionError.OK) {
                res = true;
                memb = resultat.result;
            }
            JpaUtil.fermerEntityManager();
            return res;
        } else {
            JpaUtil.fermerEntityManager();
            return false;
        }
    }
        
   public Member getMembre() {
       return memb;
   }
   
   public Services.ConnexionError getError() {
       return error;
   }
}
