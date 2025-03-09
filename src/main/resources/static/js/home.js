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
