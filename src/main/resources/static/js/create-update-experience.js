"use strict";

let acAddressInput = document.querySelector("#autocompleteAddress");
let locationLatInput = document.querySelector("#locationLat");
let locationLngInput = document.querySelector("#locationLng");
let addressInput = document.querySelector("#address");
let placeIdInput = document.querySelector("#placeId");

acAddressInput.addEventListener("keydown", (event) => {
    if (event.key === "Enter") {
        event.preventDefault();
    }
});

let autocomplete;
let map;
let infoWindow;
let marker;
function initMap() {
    autocomplete = new google.maps.places.Autocomplete(acAddressInput, {
        fields: ["place_id", "geometry", "formatted_address"]
    });
    autocomplete.addListener("place_changed", autocompletePlaceSelected);

    map = new google.maps.Map(document.querySelector("#map"), {
        mapId: "DEMO_MAP_ID",
        clickableIcons: false,
        draggableCursor: "crosshair"
    });
    map.addListener("click", mapLocationSelected);

    infoWindow = new google.maps.InfoWindow({
        content: document.querySelector("#infowindow"),
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

function updateMap(location, zoom, infoText = null) {
    infoWindow.close();
    marker.hidden = true;
    map.panTo(location);
    map.setZoom(zoom);
    if (infoText) {
        marker.position = location;
        marker.hidden = false;
        let content = document.createElement("div");
        content.innerHTML = infoText;
        content.style.fontWeight = "bold";
        infoWindow.setContent(content);
        infoWindow.open(map, marker);
    }
}

function autocompletePlaceSelected() {
    const place = autocomplete.getPlace();
    let placeId = place.place_id;
    let location = place.geometry?.location;
    let formattedAddress = place.formatted_address;
    if (!placeId || !formattedAddress || !location) {
        acAddressInput.classList.add("is-invalid");
        return;
    }

    acAddressInput.classList.remove("is-invalid")
    acAddressInput.value = formattedAddress;
    locationLatInput.value = location.lat()
    locationLngInput.value = location.lng();
    addressInput.value = formattedAddress;
    placeIdInput.value = placeId;

    updateMap(location, 19, formattedAddress);
}

function mapLocationSelected(mapMouseEvent) {
    if (mapMouseEvent.latLng) {
        let lat = mapMouseEvent.latLng.lat();
        let lng = mapMouseEvent.latLng.lng();
        let latLng = `${lat.toFixed(6)}, ${lng.toFixed(6)}`;
        acAddressInput.classList.remove("is-invalid")
        acAddressInput.value = latLng;
        locationLatInput.value = lat;
        locationLngInput.value = lng;
        addressInput.value = "";
        placeIdInput.value = "";
        updateMap(mapMouseEvent.latLng, 19, latLng);
    }
}

window.initMap = initMap;
