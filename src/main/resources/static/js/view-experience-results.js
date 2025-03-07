"use strict";

const searchForm = document.querySelector("#searchForm");
const autocompleteInput = searchForm.querySelector("#autocomplete");
const locationTextInput = searchForm.querySelector("[name='locationText']");
const locationLatInput = searchForm.querySelector("[name='locationLat']");
const locationLngInput = searchForm.querySelector("[name='locationLng']");

searchForm.addEventListener("submit", event => {
    if (autocompleteInput.value.trim() === "") {
        locationTextInput.value = "";
        locationLatInput.value = "";
        locationLngInput.value = "";
    }
});

autocompleteInput.addEventListener("keydown", (event) => {
    if (event.key === "Enter") {
        event.preventDefault();
    }
});

let autocomplete;
function initGMapsApi() {
    autocomplete = new google.maps.places.Autocomplete(autocompleteInput, {
        fields: ["geometry", "formatted_address"]
    });
    autocomplete.addListener("place_changed", autocompletePlaceSelected);

    const map = new google.maps.Map(document.querySelector("#map"), {
        mapId: "DEMO_MAP_ID",
        clickableIcons: false,
        gestureHandling: "cooperative"
    });

    const infoWindow = new google.maps.InfoWindow({
        headerDisabled: true
    });
    let selectedMarker;
    map.addListener("click", (mapMouseEvent) => {
        infoWindow.close();
        selectedMarker = null;
    });

    const experiences = document.querySelectorAll(".experience");
    const markers = [];
    const bounds = {}
    const labels = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    for (let i = 0; i < experiences.length; i++) {
        const experience = experiences[i];

        const label = labels[i % labels.length];
        const pinGlyph = new google.maps.marker.PinElement({
            glyph: label,
            glyphColor: "white",
        });

        const position = {
            lat: Number(experience.querySelector("[name='locationLat']").value),
            lng: Number(experience.querySelector("[name='locationLng']").value)
        };
        extendBounds(bounds, position);

        const marker = new google.maps.marker.AdvancedMarkerElement({
            map,
            position,
            gmpClickable: true,
            content: pinGlyph.element,
        });

        const thisInfoWindow = experience.querySelector(".info-window");
        thisInfoWindow.querySelector(".content").addEventListener("click", event => {
            experience.scrollIntoView({behavior: "smooth"});
        });

        marker.addListener("click", (mapMouseEvent) => {
            infoWindow.close();
            if (selectedMarker !== marker) {
                infoWindow.setContent(thisInfoWindow);
                infoWindow.open(marker.map, marker);
                selectedMarker = marker;
            } else {
                selectedMarker = null;
            }
        });

        markers.push(marker);
    }
    new markerClusterer.MarkerClusterer({ markers, map });

    // https://stackoverflow.com/a/4065006
    google.maps.event.addListenerOnce(map, "idle", function() {
        if (!map.getZoom() || map.getZoom() > 16) map.setZoom(16);
    });
    map.fitBounds(bounds);
}

function extendBounds(bounds, latlng) {
    if (!bounds.north || latlng.lat > bounds.north) bounds.north = latlng.lat;
    if (!bounds.south || latlng.lat < bounds.south) bounds.south = latlng.lat;
    if (!bounds.east || latlng.lng > bounds.east) bounds.east = latlng.lng;
    if (!bounds.west || latlng.lng < bounds.west) bounds.west = latlng.lng;
}

function autocompletePlaceSelected() {
    const place = autocomplete.getPlace();
    let location = place.geometry?.location;
    let formattedAddress = place.formatted_address;
    if (!formattedAddress || !location) {
        // autocompleteInput.classList.add("is-invalid");
        return;
    }

    // autocompleteInput.classList.remove("is-invalid")
    autocompleteInput.value = formattedAddress;
    locationTextInput.value = formattedAddress;
    locationLatInput.value = location.lat();
    locationLngInput.value = location.lng();
}

window.initGMapsApi = initGMapsApi;
