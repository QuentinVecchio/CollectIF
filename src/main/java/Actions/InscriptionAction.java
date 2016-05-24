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
public class InscriptionAction extends Action{
    private Member memb;
    private Services.ConnexionError error;
    
    public InscriptionAction() {
        memb = null;
        error = null;
    }

    @Override
    public boolean execute(HttpServletRequest request) {
        JpaUtil.creerEntityManager();
        boolean res = false;
        String mail = request.getParameter("email");
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String adresse = request.getParameter("adresse");
        if( mail != null && nom != null && prenom != null && adresse != null) {
            ServiceResult<Member, Services.ConnexionError> resultat = Services.Inscription(nom, prenom, mail, adresse);
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
