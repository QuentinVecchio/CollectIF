/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Actions.AssignLocationAction;
import Actions.ConnexionAction;
import Actions.ConnexionAdminAction;
import Actions.CreationEventAction;
import Actions.GetActivitiesAction;
import Actions.GetActivityByIdAction;
import Actions.GetDistanceMoyenneAction;
import Actions.GetEventAtLocationAction;
import Actions.GetEventsAction;
import Actions.GetEventsByDateAction;
import Actions.GetEventsOfMemberAction;
import Actions.GetLocationsAction;
import Actions.InscriptionAction;
import Actions.InscriptionEventAction;
import TP1_SI.DAL.JpaUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import Serialisation.Serialisation;

/**
 *
 * @author quentinvecchio
 */
public class ServletCollectif extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        
        switch(action) {
            case "activite": Activity(request, response);break;
            case "listeEvenements" : Events(request, response);break;
            case "listeEvenementsDispo" : AvailableEvents(request, response);break;
            case "listeEvenementsInscrit" : EventsOfMember(request, response);break;
            case "listeActivites" : Activities(request, response);break;
            case "creationEvenement" : CreationEvent(request, response);break;
            case "inscriptionEvenement" : InscriptionEvent(request, response);break;
            case "connection": Connection(request, response);break;
            case "isConnected": IsConnected(session, response);break;
            case "deconnection": Deconnection(session, response);break;
            case "singin": SignIn(request, response);break;
            case "listeLocations": Locations(request, response);break;
            case "listeEvenementsByDate": EventsByDate(request, response);break;
            case "distanceMoyenne" : DistanceMoyenne(request, response);break;
            case "getEventAtLocation" : GetEventAtLocation(request, response);break;
            case "assignLocation" : AssignLocation(request, response);break;
            default: Error(response, "404 - page not found");break;
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private void Error(HttpServletResponse response, String error) throws IOException {
        Gson gson = new GsonBuilder().create();
        response.getWriter().write(gson.toJson(error));
        Serialisation serial = new Serialisation(response, error);
        serial.execute();
    }
    
    private void Activities(HttpServletRequest request, HttpServletResponse response) throws IOException {
        GetActivitiesAction action = new GetActivitiesAction();
        Serialisation serial = new Serialisation(response, null);
        if(action.execute(request)){
            serial.setObject(action.getActivities());
            serial.execute();
        } else {
            serial.setObject(action.getError());
            serial.execute();
        }
    }
    
    private void Activity(HttpServletRequest request, HttpServletResponse response) throws IOException {
        GetActivityByIdAction action = new GetActivityByIdAction();
        Serialisation serial = new Serialisation(response, null);
        if(action.execute(request)){
            serial.setObject(action.getActivity());
            serial.execute();
        } else {
            serial.setObject(action.getError());
            serial.execute();
        }
    }
    
    private void Events(HttpServletRequest request, HttpServletResponse response) throws IOException {
        GetEventsAction action = new GetEventsAction();
        Serialisation serial = new Serialisation(response, null);
        if(action.execute(request)){
            serial.setObject(action.getEvents());
            serial.execute();
        } else {
            serial.setObject(action.getError());
            serial.execute();
        }
    }
    
    private void AvailableEvents(HttpServletRequest request, HttpServletResponse response) throws IOException {
        GetEventsAction action = new GetEventsAction();
        Serialisation serial = new Serialisation(response, null);
        if(action.execute(request)){
            serial.setObject(action.getEvents());
            serial.execute();
        } else {
            serial.setObject(action.getError());
            serial.execute();
        }
    }
    
    private void EventsOfMember(HttpServletRequest request ,HttpServletResponse response) throws IOException {
        GetEventsOfMemberAction action = new GetEventsOfMemberAction();
        Serialisation serial = new Serialisation(response, null);
        if(action.execute(request)){
            serial.setObject(action.getEvents());
            serial.execute();
        } else {
            serial.setObject(action.getError());
            serial.execute();
        }
    }
    
    private void Locations(HttpServletRequest request, HttpServletResponse response) throws IOException {
        GetLocationsAction action = new GetLocationsAction();
        Serialisation serial = new Serialisation(response, null);
        if(action.execute(request)){
            serial.setObject(action.getLocations());
            serial.execute();
        } else {
            serial.setObject(action.getError());
            serial.execute();
        }
    }
    
    private void CreationEvent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CreationEventAction action = new CreationEventAction();
        Serialisation serial = new Serialisation(response, null);
        if(action.execute(request)){
            serial.setObject(action.getError());
            serial.execute();
        } else {
            serial.setObject(action.getError());
            serial.execute();
        }
    }
    
     private void InscriptionEvent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        InscriptionEventAction action = new InscriptionEventAction();
        Serialisation serial = new Serialisation(response, null);
        if(action.execute(request)){
            serial.setObject(action.getError());
            serial.execute();
        } else {
            serial.setObject(action.getError());
            serial.execute();
        }
    }
     
    private void Connection ( HttpServletRequest request , HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new GsonBuilder().create();       
        ConnexionAction action = new ConnexionAction();
        ConnexionAdminAction actionAdmin = new ConnexionAdminAction();
        String answer = "error";
        HttpSession session = request.getSession();
        if(actionAdmin.execute(request)){
            session.setAttribute("isAdmin",true );
            answer = "admin";
        } else if(action.execute(request)){
            session.setAttribute("member",action.getMembre() );
            session.setAttribute("isAdmin",false );
            answer = action.getError().toString();
        } else {
            session.setAttribute("isAdmin",false);
            answer = action.getError().toString();
        }
        response.getWriter().write(gson.toJson(answer));  
    }
    
    private void SignIn ( HttpServletRequest request , HttpServletResponse response ) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        InscriptionAction action = new InscriptionAction();
        HttpSession session = request.getSession();
        Serialisation serial = new Serialisation(response, null);
        if(action.execute(request)){
            session.setAttribute("member",action.getMembre() );
            session.setAttribute("isAdmin",false );
            serial.setObject(action.getError());
            serial.execute();
        } else {
            session.setAttribute("isAdmin",false);
            serial.setObject(action.getError());
            serial.execute();
        }
    }
     
    private void Deconnection (HttpSession session ,HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String reponse = "OK";
        Gson gson = new GsonBuilder().create();
        session.setAttribute("member",null ); 
        session.setAttribute("isAdmin",false );
        response.getWriter().write(gson.toJson(reponse)); 
    }
    
    private void IsConnected (HttpSession session ,HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String reponse = "ok";
        if(session.getAttribute("member") == null ) {
            reponse = "pas ok";
        }
        Gson gson = new GsonBuilder().create();
        if(session.getAttribute("isAdmin") != null) {
            boolean isAdmin = (boolean) session.getAttribute("isAdmin");
            if(isAdmin){
                reponse = "admin";
            }
        }
        response.getWriter().write(gson.toJson(reponse));
    }
    
    private void EventsByDate ( HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        GetEventsByDateAction action = new GetEventsByDateAction();
        Serialisation serial = new Serialisation(response, null);
        if(action.execute(request)){
            serial.setObject(action.getEvents());
            serial.execute();
        } else {
            serial.setObject(action.getError());
            serial.execute();
        }
    }
    
    private void DistanceMoyenne(HttpServletRequest request, HttpServletResponse response) throws IOException {
        GetDistanceMoyenneAction action = new GetDistanceMoyenneAction();
        Serialisation serial = new Serialisation(response, null);
        if(action.execute(request)){
            serial.setObject(action.getAnswer());
            serial.execute();
        } else {
            serial.setObject(action.getError());
            serial.execute();
        }
    }
    
    private void GetEventAtLocation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JpaUtil.creerEntityManager();
        GetEventAtLocationAction action = new GetEventAtLocationAction();
        Serialisation serial = new Serialisation(response, null);
        if(action.execute(request)){
            serial.setObject(action.getEvents());
            serial.execute();
        } else {
            serial.setObject(action.getError());
            serial.execute();
        }
    }
    
    private void AssignLocation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AssignLocationAction action = new AssignLocationAction();
        Serialisation serial = new Serialisation(response, null);
        if(action.execute(request)){
            serial.setObject(action.getError());
            serial.execute();
        } else {
            serial.setObject(action.getError());
            serial.execute();
        }
    }
}
