 /**
        * @fileoverview Sample showing capturing a KML file click
        * and displaying the contents in a side panel instead of
        * an InfoWindow
        */

        var map;
        //var src = window.location.origin + '/search/kml?query=kml';

        /**
         * Initializes the map and calls the function that creates polylines.
         */
        function initialize(src) {
            if (! map) {
               map = new google.maps.Map(document.getElementById('map-canvas'), {
                   center: new google.maps.LatLng(0.257753, 0.823688),
                   zoom: 2,
                   mapTypeId: google.maps.MapTypeId.TERRAIN
               });
            }
            loadKmlLayer(src, map);
        }

        /**
         * Adds a KMLLayer based on the URL passed. Clicking on a marker
         * results in the balloon content being loaded into the right-hand div.
         * @param {string} src A URL for a KML file.
         */
        function loadKmlLayer(src, map) {
            var kmlRef = new google.maps.KmlLayer(src, {
                suppressInfoWindows: true,
                preserveViewport: false,
                map: map
            });
            google.maps.event.addListener(kmlRef, 'click', function(event) {
                var content = event.featureData.infoWindowHtml;
                var testimonial = document.getElementById('capture');
                testimonial.innerHTML = content;
            });
        }

//         google.maps.event.addDomListener(window, 'load', initialize);