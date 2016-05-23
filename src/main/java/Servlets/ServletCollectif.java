/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import TP1_SI.DAL.JpaUtil;
import TP1_SI.metier.model.Activity;
import TP1_SI.metier.model.Event;
import TP1_SI.metier.model.Location;
import TP1_SI.metier.model.Member;
import TP1_SI.metier.service.ServiceResult;
import static TP1_SI.metier.service.ServiceTechnique.Distance;
import TP1_SI.metier.service.Services;
import static TP1_SI.metier.service.Services.Connexion;
import TP1_SI.metier.service.Services.ConnexionError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.model.LatLng;
import java.io.IOException;
import java.util.List;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            case "listeEvenementsDispo" :
                ServletListeEvenementsDispo(response);
                break;
            case "listeEvenementsInscrit" :
                ServletListeEvenementsInscrit(session,response);
                break;
            case "listeActivites" :
                ServletListeActivites(response);
                break;
            case "creationEvenement" :
                String idActivity = request.getParameter("idAct");
                Member memb = null;
                memb = (Member) session.getAttribute("member");
                String date = request.getParameter("date");
                if(idActivity != null && date != null && memb != null) {
                    int year = Integer.parseInt(date.split("/")[2]);
                    int month = Integer.parseInt(date.split("/")[0]) - 1;
                    int day = Integer.parseInt(date.split("/")[1]);
                    int idUser = memb.getId().intValue();
                    System.out.println(year + " " + month + " " + day);
                    ServletCreationEvent(response, idUser, Integer.parseInt(idActivity), year, month, day);
                } else {
                    Error(response, "Parametre incorrect");
                }
                break;
            case "inscriptionEvenement" :
                String idActivity2 = request.getParameter("idAct");
                Member memb2 = null;
                memb2 = (Member) session.getAttribute("member");
                if(idActivity2 != null && memb2 != null) {
                    int idUser = memb2.getId().intValue();
                    ServletInscriptionEvent(response, idUser, Integer.parseInt(idActivity2));
                } else {
                    Error(response, "Parametre incorrect");
                }
                break;
            case "connection":
                String email = request.getParameter("email");
                if(email != null){
                    ServletConnection(session, response, email);
                } else {
                    Error(response, "Parametre incorrect");
                }
                break;
            case "isConnected":
                ServletIsConnected(session, response);
                break;
            case "deconnection":
                ServletDeconnection(session, response);
                break;
            case "singin":
                String sEmail = request.getParameter("email");
                String sNom = request.getParameter("nom");
                String sPrenom = request.getParameter("prenom");
                String sAdresse = request.getParameter("adresse");
                if( sEmail != null && sNom != null && sPrenom != null && sAdresse != null) {
                    ServletSingin(session, response, sEmail, sNom, sPrenom, sAdresse);
                } else {
                    Error(response, "Parametre incorrect");
                }
                break;
            case "listeLocations":
                ServletListeLocations(response);
                break;
            case "listeEvenementsByDate":
                String date2 = request.getParameter("date");
                if(date2 != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    try {
                        java.util.Date d = sdf.parse(date2);
                        java.sql.Date sqlDate = new Date(d.getTime());
                        ServletListeEvenementsByDate(response, sqlDate);
                    } catch (ParseException ex) {
                        Logger.getLogger(ServletCollectif.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    Error(response, "Parametre incorrect");
                }
                break;
            case "distanceMoyenne" :
                String idActivite = request.getParameter("idEvent");
                if( idActivite != null ) {
                    int idEvent = Integer.parseInt(idActivite);
                    ServletDistanceMoyenne(response, idEvent);
                } else {
                    Error(response, "Parametre incorrect");
                }
                break;
            case "getEventAtLocation" :
                String nameLocation = request.getParameter("location");
                String date3 = request.getParameter("date");
                if( nameLocation != null && date3 != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    try {
                        java.util.Date d = sdf.parse(date3);
                        java.sql.Date sqlDate = new Date(d.getTime());
                        ServletGetEventAtLocation(response, nameLocation, sqlDate);
                    } catch (ParseException ex) {
                        Logger.getLogger(ServletCollectif.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    Error(response, "Parametre incorrect");
                }
                break;
            case "assignLocation" :
                String nameLocation2 = request.getParameter("location");
                String idAct2 = request.getParameter("idAct");
                if( nameLocation2 != null && idAct2 != null) {
                    ServletAssignLocation(response, nameLocation2, Integer.parseInt(idAct2));
                } else {
                    Error(response, "Parametre incorrect");
                }
                break;
            default:
                response.getWriter().write("page not found");
                break;
        }
    }

    private void Error(HttpServletResponse response, String error) throws IOException {
       response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8"); 
        Gson gson = new GsonBuilder().create();
        response.getWriter().write(gson.toJson(error));
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
                    response.getWriter().write(gson.toJson(activite));   
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
            response.getWriter().write(gson.toJson(available_events));   
        }
        JpaUtil.fermerEntityManager();
    }
    
    private void ServletListeEvenementsDispo(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JpaUtil.creerEntityManager();
        Gson gson = new GsonBuilder().create();
        ServiceResult<List<Event>, Services.Request_Error> available_events_rslt = Services.ListAvailableEvents();
        if(available_events_rslt.error == Services.Request_Error.OK) {
            List<Event> available_events = available_events_rslt.result;
            response.getWriter().write(gson.toJson(available_events));   
        }
        JpaUtil.fermerEntityManager();
    }
    
    private void ServletListeEvenementsInscrit(HttpSession session ,HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JpaUtil.creerEntityManager();
        Gson gson = new GsonBuilder().create();
        int idNumber = 0;//response.
        Member memb = (Member) session.getAttribute("member");
        if(memb != null ) {
            idNumber = memb.getId().intValue();
        }
        ServiceResult<List<Event>, Services.Request_Error> available_events_rslt = Services.ListEventsOfMember(idNumber);
        if(available_events_rslt.error == Services.Request_Error.OK) {
            List<Event> available_events = available_events_rslt.result;
            response.getWriter().write(gson.toJson(available_events));   
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
            response.getWriter().write(gson.toJson(activities));   
        }
        JpaUtil.fermerEntityManager();
    }
    
    private void ServletListeLocations(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JpaUtil.creerEntityManager();
        Gson gson = new GsonBuilder().create();
        ServiceResult<List<Location>, Services.Request_Error> locations_rslt = Services.ListLocations();
        if(locations_rslt.error == Services.Request_Error.OK) {
            List<Location> activities = locations_rslt.result;
            response.getWriter().write(gson.toJson(activities));   
        }
        JpaUtil.fermerEntityManager();
    }
    
    private void ServletCreationEvent(HttpServletResponse response, int idUser, int idActivity, int year, int month, int day) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JpaUtil.creerEntityManager();
        Date date = new Date(year-1900,month,day);
        Gson gson = new GsonBuilder().create();
        ServiceResult<Event, Services.Request_Error> rep = Services.CreateEvent(idUser, idActivity,date);
        String reponse = "error";
        if(rep.error == Services.Request_Error.OK) {
            reponse = "ok";
        }
        response.getWriter().write(gson.toJson(reponse));   
        JpaUtil.fermerEntityManager();
    }
    
     private void ServletInscriptionEvent(HttpServletResponse response, int idUser, int idActivity) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JpaUtil.creerEntityManager();
        Gson gson = new GsonBuilder().create();
        Services.Request_Error rep = Services.JoinEvent(idUser, idActivity);
        String reponse = rep.toString();
        response.getWriter().write(gson.toJson(reponse));   
        JpaUtil.fermerEntityManager();
    }
     
    private void ServletConnection ( HttpSession session , HttpServletResponse response, String Email) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        session.setAttribute("isAdmin",false );
        Gson gson = new GsonBuilder().create();
        if(Email.equals("admin.collectif@insa-lyon.fr")) {
            response.getWriter().write(gson.toJson("admin"));
            session.setAttribute("isAdmin",true );
        } else {
            JpaUtil.creerEntityManager();
            ServiceResult<Member, ConnexionError>  ConectionRseult;
            ConectionRseult = Connexion( Email); 
            if (ConectionRseult.error == ConnexionError.OK )
            {
                session.setAttribute("member",ConectionRseult.result );
            }       
            String jison =  gson.toJson(ConectionRseult); 
            response.getWriter().println(jison);        
            JpaUtil.fermerEntityManager();  
        }
    }
    
    private void ServletSingin ( HttpSession session , HttpServletResponse response, String mail, String nom, String prenom, String adresse ) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JpaUtil.creerEntityManager();
        ServiceResult<Member, ConnexionError>  ConectionRseult;
        ConectionRseult = Services.Inscription( nom,  prenom,  mail,  adresse); 
        System.out.print(ConectionRseult);
        session.setAttribute("member",ConectionRseult.result );     
        Gson gson = new GsonBuilder().create();
        String jison =  gson.toJson(ConectionRseult); 
        response.getWriter().println(jison);
        session.setAttribute("isAdmin",false );
        JpaUtil.fermerEntityManager();  
    }
     
    private void ServletDeconnection (HttpSession session ,HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String reponse = "ok";
        session.setAttribute("member",null ); 
        Gson gson = new GsonBuilder().create();
        session.setAttribute("isAdmin",false );
        response.getWriter().write(gson.toJson(reponse)); 
    }
    
    private void ServletIsConnected (HttpSession session ,HttpServletResponse response) throws IOException, ServletException {
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
    
    private void ServletListeEvenementsByDate (HttpServletResponse response, Date date) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JpaUtil.creerEntityManager();
        Gson gson = new GsonBuilder().create();
        ServiceResult<List<Event>, Services.Request_Error> available_events_rslt = Services.ListAllEvents();
        if(available_events_rslt.error == Services.Request_Error.OK) {
            List<Event> available_events = new LinkedList<Event>();
            for(Event e : available_events_rslt.result) {
                if(e.getDate().equals(date)) {
                    
                    available_events.add(e);
                }
            }
            response.getWriter().write(gson.toJson(available_events));   
        } else {
            response.getWriter().write(gson.toJson(available_events_rslt.error.toString()));
        }
        JpaUtil.fermerEntityManager();
    }
    
    private void ServletDistanceMoyenne(HttpServletResponse response, int id) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JpaUtil.creerEntityManager();
        Gson gson = new GsonBuilder().create();
        boolean trouve = false;
        ServiceResult<List<Event>, Services.Request_Error> available_events_rslt = Services.ListAllEvents();
        if(available_events_rslt.error == Services.Request_Error.OK) {
            List<Event> available_events = available_events_rslt.result;
            for(Event e : available_events) {
                if(e.getId() == id) {
                    trouve = true;
                    if(e.getLocation() != null) {
                        double disMoyenne = 0;
                        LatLng l1 = new LatLng(e.getLocation().getLatitude(),e.getLocation().getLongitude());
                        for(Member m : e.getMembers()) {
                            LatLng l2 = new LatLng(m.getLatitude(), m.getLongitude());
                            try {
                                System.out.println(l1 + " " + l2);
                                disMoyenne += Distance(l1,l2);
                            } catch (Exception ex) {
                                Logger.getLogger(ServletCollectif.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } 
                        disMoyenne /= e.getMembers().size();
                        disMoyenne /= 1000;
                        DecimalFormat df = new DecimalFormat("#.##"); 
                        List<String> rep = new LinkedList<String>();
                        String disFormat = df.format(disMoyenne);
                        rep.add(Integer.toString(id));
                        rep.add(disFormat);
                        response.getWriter().write(gson.toJson(rep));
                    } else {
                        response.getWriter().write(gson.toJson("not location"));
                    }
                }
            }
            if(!trouve) {
                response.getWriter().write(gson.toJson("not found"));
            }
        } else {
            response.getWriter().write(gson.toJson(available_events_rslt.error.toString()));
        }
        JpaUtil.fermerEntityManager();
    }
    
    private void ServletGetEventAtLocation(HttpServletResponse response, String nameLocation, Date d) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JpaUtil.creerEntityManager();
        Gson gson = new GsonBuilder().create();
        ServiceResult<List<Event>, Services.Request_Error> available_events_rslt = Services.ListAllEvents();
        if(available_events_rslt.error == Services.Request_Error.OK) {
            List<Event> available_events = new LinkedList<Event>();
            for(Event e : available_events_rslt.result) {
                if(e.getLocation() != null) {
                    if(e.getLocation().getDescription().equals(nameLocation) && e.getDate().equals(d)) {
                        available_events.add(e);
                    }
                }
            }
            response.getWriter().write(gson.toJson(available_events));
        } else {
            response.getWriter().write(gson.toJson(available_events_rslt.error.toString()));
        }
        JpaUtil.fermerEntityManager();
    }
    
    private void ServletAssignLocation(HttpServletResponse response, String nameLocation, int id) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JpaUtil.creerEntityManager();
        Gson gson = new GsonBuilder().create();
        ServiceResult<List<Event>, Services.Request_Error> available_events_rslt = Services.ListAllEvents();
        ServiceResult<List<Location>, Services.Request_Error> available_locations_rslt = Services.ListLocations();
        if(available_events_rslt.error == Services.Request_Error.OK && available_locations_rslt.error == Services.Request_Error.OK) {
            for(Event e: available_events_rslt.result) {
                if(e.getId() == id) {
                    for(Location l: available_locations_rslt.result) {
                        if(l.getDescription().equals(nameLocation)) {
                            Services.Request_Error res = Services.AssignLocationToEvent(l.getId(), e.getId());
                            response.getWriter().write(gson.toJson(res.toString()));
                            JpaUtil.fermerEntityManager();
                            return;
                        }
                    }
                }
            }
            response.getWriter().write(gson.toJson("error"));
        } else {
            response.getWriter().write(gson.toJson(available_events_rslt.error.toString()));
        }
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
