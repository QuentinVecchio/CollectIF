/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import TP1_SI.DAL.JpaUtil;
import TP1_SI.metier.model.Activity;
import TP1_SI.metier.model.Event;
import TP1_SI.metier.model.Member;
import TP1_SI.metier.service.ServiceResult;
import TP1_SI.metier.service.Services;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.List;
import java.sql.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
            case "activite":
                String id = request.getParameter("id");
                if(id != null) {
                    ServletActivite(response, Integer.parseInt(id));
                }
                break;
            case "listeEvenements" :
                ServletListeEvenements(response);
                break;
            case "listeEvenementsInscrit" :
                ServletListeEvenementsInscrit(response);
                break;
            case "listeActivites" :
                ServletListeActivites(response);
                break;
            case "creationEvenement" :
                String idActivity = request.getParameter("idAct");
                Member memb = null;
                memb = session.getParameter("member");
                String date = request.getParameter("date");
                if(idActivity != null && date != null && memb != null) {
                    int year = Integer.parseInt(date.split("/")[2]);
                    int month = Integer.parseInt(date.split("/")[0]);
                    int day = Integer.parseInt(date.split("/")[1]);
                    Long idUser = memb.getId();
                    ServletCreationEvent(response, idUser, Integer.parseInt(idActivity), year, month, day);
                }
                break;
        }
    }

    private void ServletActivite(HttpServletResponse response, int id) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new GsonBuilder().create();
        ServiceResult<List<Activity>, Services.Request_Error> activities_rslt = Services.ListActivities();
        if(activities_rslt.error == Services.Request_Error.OK) {
            List<Activity> activities = activities_rslt.result;
            for(Activity activite : activities) {
                if(activite.getId() == id) {
                    response.getWriter().write(new Gson().toJson(activite));   
                    break;
                }
            }
        }
    }
    
    private void ServletListeEvenements(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JpaUtil.creerEntityManager();
        Gson gson = new GsonBuilder().create();
        ServiceResult<List<Event>, Services.Request_Error> available_events_rslt = Services.ListAllEvents();
        if(available_events_rslt.error == Services.Request_Error.OK) {
            List<Event> available_events = available_events_rslt.result;
            response.getWriter().write(new Gson().toJson(available_events));   
        }
        JpaUtil.fermerEntityManager();
    }
    
    private void ServletListeEvenementsInscrit(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JpaUtil.creerEntityManager();
        Gson gson = new GsonBuilder().create();
        int idNumber = 0;//response.
        ServiceResult<List<Event>, Services.Request_Error> available_events_rslt = Services.ListEventsOfMember(idNumber);
        if(available_events_rslt.error == Services.Request_Error.OK) {
            List<Event> available_events = available_events_rslt.result;
            response.getWriter().write(new Gson().toJson(available_events));   
        }
        JpaUtil.fermerEntityManager();
    }
    
    private void ServletListeActivites(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JpaUtil.creerEntityManager();
        Gson gson = new GsonBuilder().create();
        ServiceResult<List<Activity>, Services.Request_Error> activities_rslt = Services.ListActivities();
        if(activities_rslt.error == Services.Request_Error.OK) {
            List<Activity> activities = activities_rslt.result;
            response.getWriter().write(new Gson().toJson(activities));   
        }
        JpaUtil.fermerEntityManager();
    }
    
    private void ServletCreationEvent(HttpServletResponse response, int idUser, int idActivity, int year, int month, int day) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JpaUtil.creerEntityManager();
        Gson gson = new GsonBuilder().create();
        Date date = new Date(year,month,day);
        ServiceResult<Event, Services.Request_Error> rep = Services.CreateEvent(idUser, idActivity,date);
        String reponse = "error";
        if(rep.error == Services.Request_Error.OK) {
            reponse = "ok";
        }
        response.getWriter().write(new Gson().toJson(reponse));   
        JpaUtil.fermerEntityManager();
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
    }// </editor-fold>

}
