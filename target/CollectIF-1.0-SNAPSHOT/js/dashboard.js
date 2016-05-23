/* global google */

markers = [];
markersLocation = [];
var pinColor = "FE7569";
var pinImage = new google.maps.MarkerImage("http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|" + pinColor,
    new google.maps.Size(21, 34),
    new google.maps.Point(0,0),
    new google.maps.Point(10, 34));

$(function() {
     $("#deconnexion").click(function(){
        $.ajax({
            url: "ServletCollectif?action=deconnection",
            success: function( result ) {
                document.location.href = "./index.html";
            }
        }); 
    });
    $("#dateEvent").datepicker();
    $("#dateEvent").change(function() {
        $(".event").remove();
        var date = $("#dateEvent").val();
        $.ajax({
            url: "ServletCollectif?action=listeEvenementsByDate&date=" + date,
            success: function( result ) {
                for(var r=0;r<result.length;r++){
                    $.ajax({
                        url: "ServletCollectif?action=distanceMoyenne&idEvent=" + result[r].id,
                        async: false, 
                        success: function( res ) {
                            res = res[1];
                            var div = "<div class=\"nomActivity\">" + result[r].activity.name +  "</div>";
                            if(res == "not location")
                                div += "<div class=\"distance\">DISTANCE MOYENNE</br>? Km</div>";
                            else
                                div += "<div class=\"distance\">DISTANCE MOYENNE</br>" + res + " Km</div>";              
                            div += "<div class=\"nbParticipant\">" + result[r].members.length + "/" + result[r].activity.nbParticipants + "</div>";
                            div += "<ul class=\"listeParticipants\">";
                            for(var i=0;i<result[r].members.length;i++){
                                div += "<li>" + result[r].members[i].prenom + " " + result[r].members[i].nom + "</li>";  
                            }
                            div += "</ul>";
                            $( "#listeEvenements" ).append("<li style=\"z\"id=\"" + result[r].id + "\" class=\"event\" onclick=\"selected(" + result[r].id + ")\">" + div + "</li>");                      
                            $( "#" + result[r].id).css({"z-index" : "999999", "position" : "relative"}); 
                            $( "#" + result[r].id).draggable({                                
                                revert: true,
                                stop: function(event, ui) 
                                {   
                                }
                            });
                        }
                    });
                }
            }
        });
    });
    var now = new Date();
    $('#dateEvent').val((now.getMonth() + 1)+ "/" + (now.getDate()-1) + "/" + now.getFullYear());
    $('#dateEvent').trigger( "change" );
});
    
function initialize() {
  mapProp = {
    center: new google.maps.LatLng(45.763699, 4.835636),
    zoom:13,
    mapTypeId:google.maps.MapTypeId.ROADMAP
  };
  map=new google.maps.Map(document.getElementById("map"),mapProp);
  $.ajax({
        url: "ServletCollectif?action=listeLocations",
        success: function( result ) {
            for(r=0;r<result.length;r++){
                //Placement events       
                var LatLng = {lat: result[r].latitude, lng: result[r].longitude};
                var marker = new google.maps.Marker({
                    position: LatLng,
                    animation: google.maps.Animation.DROP,
                    map: map,
                    title: result[r].description,
                    icon : 'http://maps.google.com/mapfiles/ms/icons/red-dot.png'
                });
                marker.addListener('click', function() {
                    if($(".activitySelected").size() != 0) {
                        if (confirm("Assigner le lieu " + this.title + " à l'évènement " + $(".activitySelected .nomActivity").text())) {
                            var idAct = $(".activitySelected").attr("id");
                            $.ajax({
                                url: "ServletCollectif?action=assignLocation",
                                data:{location: this.title, idAct : idAct},
                                success: function( result ) {
                                    if(result == "OK") {
                                        $(".activitySelected").attr("class", "event");
                                        alert("Opération réalisée avec succès");
                                        $('#dateEvent').trigger( "change" );
                                    } else {
                                        alert(result);
                                    }
                                },
                                error: function() {
                                    alert("Erreur ajax");
                                }
                            }); 
                        } else {
                            $(".activitySelected").attr("class", "event");
                        }
                    } else {
                        var infowindow = new google.maps.InfoWindow();
                        infowindow.open(map, this);
                        infowindow.setPosition (this.position);
                        var date = $("#dateEvent").val();
                        var div = "<div><h3>" + this.title + "</h3></div>";
                        infowindow.setContent(div);
                        $.ajax({
                            url: "ServletCollectif?action=getEventAtLocation",
                            data:{location: this.title, date : date},
                            success: function( result ) {
                                if(result.length > 0) {
                                    var div = "<div class=\"afficheInfo\"><h3>" + result[0].location.description + "</h3>";
                                    div += "<ul>"
                                    for(var r = 0;r<result.length;r++) {
                                        div += "<li>";
                                        div += "<div class=\"nomActivity\">" + result[r].activity.name +  "</div>";
                                        div += "<div class=\"distance\" style=\"font-size: 10px\">DISTANCE MOYENNE</br><span id=\"distance" + result[r].id + "\"></span></div>";
                                        div += "<div class=\"nbParticipant\">" + result[r].members.length + "/" + result[r].activity.nbParticipants + "</div>";
                                        div += "</li>";
                                    }
                                    div += "</ul>";
                                    div += "</div>";
                                    infowindow.setContent(div);
                                    for(var r = 0;r<result.length;r++) {
                                        $.ajax({
                                            url: "ServletCollectif?action=distanceMoyenne&idEvent=" + result[r].id,
                                            async: false, 
                                            success: function( res ) {
                                                $("#distance" + res[0]).text(res[1] + " Km");
                                            }
                                        });
                                    }
                                }
                            }
                        }); 
                    }
                });
            }
        }
    });

}
google.maps.event.addDomListener(window, 'load', initialize);

function selected(id) {
    $(".activitySelected").attr("class", "event");
    $("#map").css("z-index","-1");
    for(var i = 0;i<markers.length;i++) {
        markers[i].setMap(null);
    }
    
    if($("#" + id).attr("class") == "event") {
        $("#" + id).attr("class","event activitySelected");
        $.ajax({
            //url: "ServletCollectif?action=listeEvenementsByDate&date=" + date,
            url: "ServletCollectif?action=listeEvenements",
            success: function( result ) {
                for(var r=0;r<result.length;r++){
                    if(result[r].id == id) {
                        //Placement membres
                        for(var i=0;i<result[r].members.length;i++){
                            var LatLng = {lat: result[r].members[i].lat, lng: result[r].members[i].lng};
                            markers.push(new google.maps.Marker({
                                position: LatLng,
                                animation: google.maps.Animation.DROP,
                                map: map,
                                title: result[r].members[i].prenom + " " + result[r].members[i].nom,
                                icon : 'http://maps.google.com/mapfiles/ms/icons/blue-dot.png'
                            }));
                            markers[i].addListener('click', function() {
                                var infowindow = new google.maps.InfoWindow({
                                    content: this.title
                                });
                                infowindow.open(map, this);
                                infowindow.setPosition (this.position);
                            });
                        }
                    }
                }
            }
        });
    } else {
        $("#" + id).attr("class", "event");
    }
}