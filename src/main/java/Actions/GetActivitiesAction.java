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
public class GetActivitiesAction extends Action{
    private List<Activity> activities;
    private Services.Request_Error error;
    
    public GetActivitiesAction() {
        activities = null;
        error = null;
    }
        
    @Override
    public boolean execute(HttpServletRequest request) {
        JpaUtil.creerEntityManager();
        ServiceResult<List<Activity>, Services.Request_Error> activities_rslt = Services.ListActivities();
        JpaUtil.fermerEntityManager();
        error = activities_rslt.error;
        if(activities_rslt.error == Services.Request_Error.OK) {
            activities = activities_rslt.result;
            return true;
        }
        return false;
    }
    
    public List<Activity> getActivities() {
        return activities;
    }
    
    public Services.Request_Error getError() {
       return error;
   }
}
