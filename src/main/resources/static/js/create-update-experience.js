"use strict";

let acAddressInput = document.querySelector("#autocomplete-address");
let addressInput = document.querySelector("#address");
let locationLatInput = document.querySelector("#locationLat");
let locationLngInput = document.querySelector("#locationLng");

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
        fields: ["formatted_address", "geometry.location"]
    });
    autocomplete.addListener("place_changed", placeChanged);

    map = new google.maps.Map(document.querySelector("#map"), {
        clickableIcons: false,
        mapId: "DEMO_MAP_ID"
    });

    infoWindow = new google.maps.InfoWindow({
        content: document.querySelector("#infowindow"),
        headerDisabled: true
    });

    marker = new google.maps.marker.AdvancedMarkerElement({
        map: map
    });

    if (addressInput.value && locationLatInput.value && locationLngInput.value) {
        let location = {
            lat: Number(locationLatInput.value),
            lng: Number(locationLngInput.value)
        }
        updateMap(location, 19, addressInput.value)
    } else {
        updateMap({lat: 0, lng: 0}, 1);
    }
}

function updateMap(location, zoom, formattedAddress = null) {
    infoWindow.close();
    marker.hidden = true;
    map.setCenter(location);
    map.setZoom(zoom);
    if (formattedAddress) {
        marker.position = location;
        marker.hidden = false;
        infoWindow.setContent(formattedAddress);
        infoWindow.open(map, marker);
    }
}

function placeChanged() {
    const place = autocomplete.getPlace();
    let formattedAddress = place.formatted_address;
    let location = place.geometry?.location;
    if (!formattedAddress || !location) {
        acAddressInput.classList.add("is-invalid");
        return;
    }

    acAddressInput.classList.remove("is-invalid")
    acAddressInput.value = formattedAddress;
    addressInput.value = formattedAddress;
    locationLatInput.value = location.lat()
    locationLngInput.value = location.lng();

    updateMap(location, 19, formattedAddress);
}

window.initMap = initMap;
