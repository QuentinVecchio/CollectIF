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
public abstract class Action {
    public abstract boolean execute(HttpServletRequest request);
    
    public Action(){
        
    }
}
