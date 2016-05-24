/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Actions;

import TP1_SI.DAL.JpaUtil;
import TP1_SI.metier.model.Activity;
import TP1_SI.metier.service.ServiceResult;
import TP1_SI.metier.service.Services;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author quentinvecchio
 */
public class GetActivityByIdAction extends Action {
    
    private Activity activity;
    private Services.Request_Error error;

    public GetActivityByIdAction() {
        activity = null;
        error = null;
    }
    
    @Override
    public boolean execute(HttpServletRequest request) {
        JpaUtil.creerEntityManager();
        ServiceResult<List<Activity>, Services.Request_Error> activities_rslt = Services.ListActivities();
        JpaUtil.fermerEntityManager();
        int id = Integer.parseInt(request.getParameter("id"));
        error = activities_rslt.error;
        if(activities_rslt.error == Services.Request_Error.OK) {
            for(Activity act : activities_rslt.result) {
                if(act.getId() == id) {
                    activity = act;
                }
            }
            if(activity == null) {
                error = Services.Request_Error.WRONG_ACTIVITY_ID;
                return false;
            }
            return true;
        }
        return false;
    }
    
    public Activity getActivity() {
        return activity;
    }
    
    public Services.Request_Error getError() {
       return error;
   }
}
