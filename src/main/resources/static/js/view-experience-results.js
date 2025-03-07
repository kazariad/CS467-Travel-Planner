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
let map;
let infoWindow;
let selectedMarker;
function initGMapsApi() {
    autocomplete = new google.maps.places.Autocomplete(autocompleteInput, {
        fields: ["geometry", "formatted_address"]
    });
    autocomplete.addListener("place_changed", autocompletePlaceSelected);

    map = new google.maps.Map(document.querySelector("#map"), {
        mapId: "DEMO_MAP_ID",
        clickableIcons: false,
        gestureHandling: "cooperative"
    });

    infoWindow = new google.maps.InfoWindow({
        headerDisabled: true
    });
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
            if (selectedMarker !== marker) {
                showInfoWindow(marker, thisInfoWindow);
            } else {
                infoWindow.close();
                selectedMarker = null;
            }
        });
        markers.push(marker);

        experience.querySelector(".view-on-map").addEventListener("click", event => {
            searchForm.scrollIntoView({behavior: "smooth"});
            showInfoWindow(marker, thisInfoWindow, 16);
        });
    }
    new markerClusterer.MarkerClusterer({ markers, map });

    // https://stackoverflow.com/a/4065006
    google.maps.event.addListenerOnce(map, "idle", function() {
        if (!map.getZoom() || map.getZoom() > 16) map.setZoom(16);
    });
    map.fitBounds(bounds);
}

function extendBounds(bounds, position) {
    if (!bounds.north || position.lat > bounds.north) bounds.north = position.lat;
    if (!bounds.south || position.lat < bounds.south) bounds.south = position.lat;
    if (!bounds.east || position.lng > bounds.east) bounds.east = position.lng;
    if (!bounds.west || position.lng < bounds.west) bounds.west = position.lng;
}

function showInfoWindow(marker, content, zoom) {
    infoWindow.close();
    map.setCenter(marker.position);
    if (zoom !== undefined) map.setZoom(zoom);
    infoWindow.setContent(content);
    infoWindow.open({
        anchor: marker,
        map: map,
        shouldFocus: false
    });
    selectedMarker = marker;
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
