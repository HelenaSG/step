/** Add a map to the page. */
function initMap() {

  var map = new google.maps.Map(document.getElementById('map-container'), {
    zoom: 2,
    center: {lat:57.3994249,lng:163.7829049} 
  });

  // Create an array of alphabetical characters used to label the markers.
  var labels = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';

  // Add some markers to the map.
  var markers = locations.map(function(location, i) {
    return new google.maps.Marker({
      position: location,
      label: labels[i % labels.length]
    });
  });

  // Add a marker clusterer to manage the markers.
  var markerCluster = new MarkerClusterer(map, markers,{imagePath: 'm'});
}

var locations = [
  {lat:40.024788,lng: 116.411786},
  {lat:31.2246325,lng: 121.1965776},
  {lat:22.3529808,lng: 113.987618},
  {lat:23.4842615,lng: 118.9632602},
  {lat:31.3286758,lng: 118.3241257},
  {lat:32.0994829,lng: 118.5976691},
  {lat:34.2595844,lng: 108.8271274},
  {lat:35.6588675,lng: 139.7028676},
  {lat:35.0984403,lng: 135.5787925},
  {lat:34.6777642,lng: 135.4160251},
  {lat:34.6580727,lng: 135.8220512},
  {lat:43.4355797,lng:141.4549418},
  {lat:42.3163031,lng: -72.6424982},
  {lat:25.7824806,lng: -80.2644778},
  {lat:40.0026084,lng: -75.1880675},
  {lat:42.3144556,lng: -71.0403232},
  {lat:40.6976637,lng: -74.119762},
  {lat:37.7577627,lng: -122.4726193},
  {lat:32.8208751,lng: -96.871624},
  {lat:4.352595,lng: 73.6084838},
  {lat:7.8213349,lng: 98.298036}
]
