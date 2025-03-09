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
    for (let i = 0; i < experiences.length; i++) {
        const experience = experiences[i];

        const label = experience.querySelector(".view-on-map .label").textContent;
        const pin = new google.maps.marker.PinElement({
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
            content: pin.element,
        });

        const thisInfoWindow = experience.querySelector(".info-window");
        thisInfoWindow.querySelector("button.view-in-list").addEventListener("click", event => {
            event.stopPropagation();
            event.preventDefault();
            experience.scrollIntoView({
                block: "center",
                behavior: "auto"
            });
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

        experience.querySelector("button.view-on-map").addEventListener("click", event => {
            event.stopPropagation();
            event.preventDefault();
            searchForm.scrollIntoView({behavior: "auto"});
            showInfoWindow(marker, thisInfoWindow, 16);
        });
    }

    if (locationTextInput.value !== "" && locationLatInput.value !== "" && locationLngInput.value !== "") {
        const marker = new google.maps.marker.AdvancedMarkerElement({
            map,
            position: {
                lat: Number(locationLatInput.value),
                lng: Number(locationLngInput.value)
            },
            gmpClickable: true,
            content: new google.maps.marker.PinElement({
                background: "orange"
            }).element
        });

        const infoWindow = document.createElement("span");
        infoWindow.textContent = locationTextInput.value;
        infoWindow.style.fontWeight = "bold";

        marker.addListener("click", (mapMouseEvent) => {
            if (selectedMarker !== marker) {
                showInfoWindow(marker, infoWindow);
            } else {
                infoWindow.close();
                selectedMarker = null;
            }
        });
        markers.push(marker);
    }

    new markerClusterer.MarkerClusterer({ markers, map });

    // https://stackoverflow.com/a/4065006
    google.maps.event.addListenerOnce(map, "idle", () => {
        if (map.getZoom() > 16) map.setZoom(16);
    });
    map.fitBounds(bounds);
}

function extendBounds(bounds, position) {
    if (bounds.north == null || position.lat > bounds.north) bounds.north = position.lat;
    if (bounds.south == null || position.lat < bounds.south) bounds.south = position.lat;
    if (bounds.east == null || position.lng > bounds.east) bounds.east = position.lng;
    if (bounds.west == null || position.lng < bounds.west) bounds.west = position.lng;
}

function showInfoWindow(marker, content, zoom) {
    infoWindow.close();
    map.panTo(marker.position);
    if (zoom != null) map.setZoom(zoom);
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
    if (location == null || formattedAddress == null) {
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
