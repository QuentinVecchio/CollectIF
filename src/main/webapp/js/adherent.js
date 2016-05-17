$(function() {
    chargeEventDispo();
    chargeEventInscrit();
    
    $( "#dialogEvent" ).dialog({
      autoOpen: false,
      modal: true
    });
    
    $("#addEvent").click(function(){
        $( "#dialogEvent" ).dialog( "open" );
        $(".ui-widget-header").css({ "background": "rgb(69, 69, 69)"});
        $(".ui-widget-header").css({ "border": "rgb(69, 69, 69)"});
        $(".ui-widget-header").css({ "border-radius": "0px"});
        $(".ui-icon ui-icon-closethick").css({ "color": "black"});  
    });
    
    $("#valideEvent").click(function(){
        var activId = $("#formActivite").val(); 
        var date = $("#dateEvent").val();
        $.ajax({
            url: "ServletCollectif?action=creationEvenement&idAct=" + activId + "&date=" + date,
            success: function( result ) {
                if(result[0] == "ok") {
                    $( "#dialogEvent" ).dialog( "close" );
                    chargeEventDispo();
                    chargeEventInscrit();
                } else {
                    alert("Erreur : Event not create");
                }
            }
        });
        $( "#dialogEvent" ).dialog( "close" );
    });
    
    $("#annuleEvent").click(function(){
        $( "#dialogEvent" ).dialog( "close" );
    });

    $("#dateEvent").datepicker();
    
    function n(n){
        return n > 9 ? "" + n: "0" + n;
    }

    function date(d) {
        res = d.split(" ");
        m = 0
        switch(res[0]) {
            case "janvier" : m = 1;break;
            case "février" : m = 2;break;
            case "mars" : m = 3;break;
            case "avril" : m = 4;break;
            case "mai" : m = 5;break;
            case "juin" : m = 6;break;
            case "juillet" : m = 7;break;
            case "août" : m = 8;break;
            case "septembre" : m = 9;break;
            case "octobre" : m = 10;break;
            case "novembre" : m = 11;break;
            case "décembre" : m = 12;break;
        }
        return n(res[1].split(",")[0]) + "/" + n(m) + "/" + res[2];
    }

    //Charge des evenements disponibles
    function chargeEventDispo() {
        $.ajax({
            url: "ServletCollectif?action=listeEvenements",
            success: function( result ) {
                for(r=0;r<result.length;r++){
                    $( "#evtDisponible" ).append("<tr>");
                    $( "#evtDisponible" ).append("<td><strong>" + result[r].activity.name + "</strong></td>");   
                    if(result[r].location == undefined) 
                        $( "#evtDisponible" ).append("<td><strong>Lieu non déterminé</strong></td>");
                    else 
                        $( "#evtDisponible" ).append("<td><strong>" + result[r].location.denomination + "</strong></td>");
                    $( "#evtDisponible" ).append("<td>" + date(result[r].date) + "</td>"); 
                    $( "#evtDisponible" ).append("<td><strong>" + result[r].members.length + "</strong> participants / " + result[r].activity.nbParticipants + "</td>"); 
                    $( "#evtDisponible" ).append("</tr>");
                }
            }
        });
    }

    //Chargement des evenements inscrit
    function chargeEventInscrit() {
        $.ajax({
            url: "ServletCollectif?action=listeEvenementsInscrit",
            success: function( result ) {
                for(r=0;r<result.length;r++){
                    $( "#evt" ).append("<tr>");
                    $( "#evtInscrit" ).append("<td><strong>" + result[r].activity.name + "</strong></td>");   
                    if(result[r].location == undefined) 
                        $( "#evtInscrit" ).append("<td><strong>Lieu non déterminé</strong></td>");
                    else 
                        $( "#evtInscrit" ).append("<td><strong>" + result[r].location.denomination + "</strong></td>");
                    $( "#evtInscrit" ).append("<td>" + date(result[r].date) + "</td>"); 
                    $( "#evtInscrit" ).append("<td><strong>" + result[r].members.length + "</strong> participants / " + result[r].activity.nbParticipants + "</td>"); 
                    $( "#evtInscrit" ).append("</tr>");
                }
            }
        });
    }

    //Chargement des activites disponible
    $.ajax({
        url: "ServletCollectif?action=listeActivites",
        success: function( result ) {
            for(r=0;r<result.length;r++){
                $( "#formActivite" ).append("<option value=\"" + result[r].id + "\">" + result[r].name + "</option>");   
            }
        }
    });
});