/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Actions;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author quentinvecchio
 */
public class ConnexionAdminAction extends Action{
    
    public ConnexionAdminAction() {
        
    }
    
    @Override
    public boolean execute(HttpServletRequest request) {
        String mail = request.getParameter("email");
        if(mail != null) {
            if(mail.equals("admin.collectif@insa-lyon.fr"))
                return true;
        }
        return false;
    }
}
