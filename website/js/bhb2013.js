// VARs

//google maps
var map; 
var marker;
var markers = [];

var locpos = new google.maps.LatLng(12.458033,17.226563);

var origin;
var destination;
var waypoints 	= [];
var markertype;

var startdate;
var enddate;

var directionDisplay;
var directionsService = new google.maps.DirectionsService();
var directionsVisible = false;

var styles = [
  {
    stylers: [
      { hue: "#00ffe6" },
      { saturation: -20 }
    ]
  },{
    featureType: "road",
    elementType: "geometry",
    stylers: [
      { lightness: 100 },
      { visibility: "simplified" }
    ]
  },{
    featureType: "road",
    elementType: "labels",
    stylers: [
      { visibility: "off" }
    ]
  }
];

	var myOptionss = {
      zoom: 2,
      center: locpos,
	  styles: styles,
	  disableDefaultUI: true,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
 
 
function initmaps(){
//directionsDisplay = new google.maps.DirectionsRenderer();
map = new google.maps.Map(document.getElementById("map"), myOptionss);
//directionsDisplay.setMap(map);
//getUserInfo();
}

var k = 0;
var picid = 0;
var thimage;

function getUserInfo(){

	$.getJSON('http://www.pegelonline.wsv.de/webservices/rest-api/v2/stations.json?waters=Elbe&includeTimeseries=true&includeCurrentMeasurement=true', function(userdata) {  
		//console.log(userdata);

		$.each(userdata, function(va,key) {
		var loc = new google.maps.LatLng(key.latitude,key.longitude);
		var messwert = key.timeseries[0].currentMeasurement.value/100;
		var loctime = new Date(key.timeseries[0].currentMeasurement.timestamp);
		//loctime = loctime.format("isoDateTime");
		addMarker(loc, key.shortname, messwert +"m" + " " + loctime);
		
		var uuid = key.uuid;
		var shortname = key.shortname;
		var messwert = key.timeseries[0].currentMeasurement.value/100;
		var timestamp = key.timeseries[0].currentMeasurement.timestamp;
		var latitude = key.latitude;
		var longitude = key.longitude;		
		
		addToTable(shortname, messwert, timestamp);
		addToDB(uuid, shortname, messwert, timestamp, latitude, longitude);

		});
		$('#example').dataTable();
	});

}

function addToTable(shortname, messwert, timestamp){


$('#tablebody').append('<tr><td>' + shortname +'</td><td>' + timestamp + '</td><td>' + messwert + '</td></tr>');

}

function addToDB(uuid, shortname, messwert, timestamp, latitude, longitude){

$.post("addToElbe.php", {uuid:uuid, shortname:shortname, messwert:messwert, timestamp:timestamp, latitude:latitude, longitude:longitude}, function(data)
{
$("#results").html(data);
});


}

 function addMarker(loc, image, messwert) {

      marker = new google.maps.Marker({
          position: loc,
          map: map,
      });
	
		google.maps.event.addListener(marker, 'click', function() {
		alert(image + " " + messwert)
		});
			
        //markerssc.push(marker);
 }




