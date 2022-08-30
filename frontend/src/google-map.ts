import { html, LitElement } from 'lit';

class GoogleMap extends LitElement {

    render() {
        return html`
            <div id = "map"></div>
            <script>
                 function initMap(){
                  map = new google.maps.Map(document.getElementById("map"),{center: { lat:51.21, lng:-0.129761 },zoom: 17});
                 }
              </script>
              <script
                      src="https://maps.googleapis.com/maps/api/js?key=AIzaSyC7Q3-PD4pYBdAlDx9O_gxFAEUewPVGOeE&callback=initMap"
                      async defer>
              </script>`;
    }
}

customElements.define('google-map', GoogleMap);