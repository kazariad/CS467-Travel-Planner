"use strict";

let autocompleteInput = document.querySelector("#autocomplete");
let locationLatInput = document.querySelector("#locationLat");
let locationLngInput = document.querySelector("#locationLng");
let addressInput = document.querySelector("#address");
let placeIdInput = document.querySelector("#placeId");

autocompleteInput.addEventListener("keydown", (event) => {
    if (event.key === "Enter") {
        event.preventDefault();
    }
});

let autocomplete;
let map;
let infoWindow;
let marker;
function initGMapsApi() {
    autocomplete = new google.maps.places.Autocomplete(autocompleteInput, {
        fields: ["place_id", "geometry", "formatted_address"]
    });
    autocomplete.addListener("place_changed", autocompletePlaceSelected);

    map = new google.maps.Map(document.querySelector("#map"), {
        mapId: "DEMO_MAP_ID",
        clickableIcons: false,
        gestureHandling: "cooperative",
        draggableCursor: "crosshair"
    });
    map.addListener("click", mapLocationSelected);

    infoWindow = new google.maps.InfoWindow({
        headerDisabled: true
    });

    marker = new google.maps.marker.AdvancedMarkerElement({
        map: map
    });

    if (locationLatInput.value && locationLngInput.value) {
        let lat = Number(locationLatInput.value);
        let lng = Number(locationLngInput.value);
        updateMap({lat: lat, lng: lng}, 19, addressInput.value ? addressInput.value : `${lat.toFixed(6)}, ${lng.toFixed(6)}`)
    } else {
        updateMap({lat: 0, lng: 0}, 1);
    }
}

function updateMap(location, zoom, infoText) {
    infoWindow.close();
    marker.hidden = true;
    map.panTo(location);
    map.setZoom(zoom);
    if (infoText != null) {
        marker.position = location;
        marker.hidden = false;
        let content = document.createElement("div");
        content.innerHTML = infoText;
        content.style.fontWeight = "bold";
        infoWindow.setContent(content);
        infoWindow.open({
            anchor: marker,
            map: map,
            shouldFocus: false
        });
    }
}

function autocompletePlaceSelected() {
    const place = autocomplete.getPlace();
    let placeId = place.place_id;
    let location = place.geometry?.location;
    let formattedAddress = place.formatted_address;
    if (placeId == null || formattedAddress == null || location == null) {
        autocompleteInput.classList.add("is-invalid");
        autocompleteInput.value = "";
        locationLatInput.value = "";
        locationLngInput.value = "";
        addressInput.value = "";
        placeIdInput.value = "";
        updateMap({lat: 0, lng: 0}, 1);
    } else {
        autocompleteInput.classList.remove("is-invalid")
        autocompleteInput.value = formattedAddress;
        locationLatInput.value = location.lat();
        locationLngInput.value = location.lng();
        addressInput.value = formattedAddress;
        placeIdInput.value = placeId;
        updateMap(location, 19, formattedAddress);
    }
}

function mapLocationSelected(mapMouseEvent) {
    if (mapMouseEvent.latLng != null) {
        let lat = mapMouseEvent.latLng.lat();
        let lng = mapMouseEvent.latLng.lng();
        let latLng = `${lat.toFixed(6)}, ${lng.toFixed(6)}`;
        autocompleteInput.classList.remove("is-invalid")
        autocompleteInput.value = latLng;
        locationLatInput.value = lat;
        locationLngInput.value = lng;
        addressInput.value = "";
        placeIdInput.value = "";
        updateMap(mapMouseEvent.latLng, 19, latLng);
    }
}

window.initGMapsApi = initGMapsApi;
