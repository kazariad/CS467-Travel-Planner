"use strict";

let searchForm = document.querySelector("#searchForm");
let acLocationInput = document.querySelector("#acLocationInput");
let locationTextInput = document.querySelector("[name='locationText']");
let locationLatInput = document.querySelector("[name='locationLat']");
let locationLngInput = document.querySelector("[name='locationLng']");

searchForm.addEventListener("submit", event => {
    if (acLocationInput.value.trim() === "") {
        locationTextInput.value = "";
        locationLatInput.value = "";
        locationLngInput.value = "";
    }
});

acLocationInput.addEventListener("keydown", (event) => {
    if (event.key === "Enter") {
        event.preventDefault();
    }
});

let autocomplete;
function initGMapsApi() {
    autocomplete = new google.maps.places.Autocomplete(acLocationInput, {
        fields: ["geometry", "formatted_address"]
    });
    autocomplete.addListener("place_changed", autocompletePlaceSelected);
}

function autocompletePlaceSelected() {
    const place = autocomplete.getPlace();
    let location = place.geometry?.location;
    let formattedAddress = place.formatted_address;
    if (!formattedAddress || !location) {
        // acLocationInput.classList.add("is-invalid");
        return;
    }

    // acLocationInput.classList.remove("is-invalid")
    acLocationInput.value = formattedAddress;
    locationTextInput.value = formattedAddress;
    locationLatInput.value = location.lat();
    locationLngInput.value = location.lng();
}

window.initGMapsApi = initGMapsApi;
