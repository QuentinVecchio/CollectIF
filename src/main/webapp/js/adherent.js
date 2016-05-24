$(function() {
    chargeEventDispo();
    chargeEventInscrit();
    
    $( "#dialogEvent" ).dialog({
      autoOpen: false,
      modal: true
    });
    
    $( "#dialogInscription" ).dialog({
      autoOpen: false,
      modal: true,
      resizable: false
    });
    
    
    $("#deconnexion").click(function(){
        $.ajax({
            url: "ServletCollectif?action=deconnection",
            success: function( result ) {
                document.location.href = "./index.html";
            }
        }); 
    });
    
    
    $("#addEvent").click(function(){
        $( "#dialogEvent" ).css("visibility","visible");
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
                if(result == "OK") {
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
    
    $("#valideInscriptionEvent").click(function(){
        var activId = $("#idAct").val(); 
        $.ajax({
            url: "ServletCollectif?action=inscriptionEvenement&idAct=" + activId,
            success: function( result ) {
                if(result == "OK") {
                    $( "#dialogEvent" ).dialog( "close" );
                    chargeEventDispo();
                    chargeEventInscrit();
                }
            }
        });
        $( "#dialogInscription" ).dialog( "close" );
    });
    
    $("#annuleInscriptionEvent").click(function(){
        $( "#dialogInscription" ).dialog( "close" );
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
        $("tr").remove();
        $("td").remove();
        $.ajax({
            url: "ServletCollectif?action=listeEvenementsDispo",
            success: function( result ) {
                for(r=0;r<result.length;r++){
                    $( "#evtDisponible" ).append("<tr id=\"trDispo" + result[r].id + "\" onclick=\"inscriptionEvent(" + result[r].id + ")\"></tr>");
                    $( "#trDispo" + result[r].id ).append("<td><strong>" + result[r].activity.name + "</strong></td>");   
                    if(result[r].location == undefined) 
                        $( "#trDispo" + result[r].id ).append("<td><strong>Lieu non déterminé</strong></td>");
                    else 
                        $( "#trDispo" + result[r].id ).append("<td><strong>" + result[r].location.description + "</strong></td>");
                    $( "#trDispo" + result[r].id ).append("<td>" + date(result[r].date) + "</td>"); 
                    $( "#trDispo" + result[r].id ).append("<td><strong>" + result[r].members.length + "</strong> participants / " + result[r].activity.nbParticipants + "</td>"); 
                }
            }
        });
    }

    //Chargement des evenements inscrit
    function chargeEventInscrit() {
        $("tr").remove();
        $("td").remove();
        $.ajax({
            url: "ServletCollectif?action=listeEvenementsInscrit",
            success: function( result ) {
                for(r=0;r<result.length;r++){
                    $( "#evtInscrit" ).append("<tr id=\"trIns" + result[r].id + "\"></tr>");
                    $( "#trIns" + result[r].id ).append("<td><strong>" + result[r].activity.name + "</strong></td>");   
                    if(result[r].location == undefined) 
                        $( "#trIns" + result[r].id ).append("<td><strong>Lieu non déterminé</strong></td>");
                    else 
                        $( "#trIns" + result[r].id ).append("<td><strong>" + result[r].location.description + "</strong></td>");
                    $( "#trIns" + result[r].id ).append("<td>" + date(result[r].date) + "</td>"); 
                    $( "#trIns" + result[r].id ).append("<td><strong>" + result[r].members.length + "</strong> participants / " + result[r].activity.nbParticipants + "</td>"); 
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