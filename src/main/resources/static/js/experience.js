"use strict";

let acAddressInput = document.querySelector("#autocomplete-address");
acAddressInput.addEventListener("keydown", (event) => {
    if (event.key === "Escape") {
        acAddressInput.value = addressInput.value;
    }
});
acAddressInput.addEventListener("keydown", (event) => {
    if (event.key === "Enter") {
        event.preventDefault();
    }
});

let addressInput = document.querySelector("#address");
let form = document.querySelector("form");
form.addEventListener("submit", (event) => {
    if (acAddressInput.value !== addressInput.value) {
        acAddressInput.classList.add("is-invalid")
        event.preventDefault();
    }
});

let autocomplete;
function initAutocomplete() {
    autocomplete = new google.maps.places.Autocomplete(acAddressInput);
    autocomplete.setFields(["formatted_address", "geometry.location"]);
    autocomplete.addListener("place_changed", updateAddressLocation);
}

let locationLatInput = document.querySelector("#locationLat");
let locationLngInput = document.querySelector("#locationLng");
function updateAddressLocation() {
    const place = autocomplete.getPlace();

    let formattedAddress = place.formatted_address;
    let location = place.geometry?.location;
    if (formattedAddress === undefined || location === undefined) {
        acAddressInput.classList.add("is-invalid")
    } else {
        acAddressInput.value = formattedAddress;
        addressInput.value = formattedAddress;
        locationLatInput.value = location.lat()
        locationLngInput.value = location.lng();
        acAddressInput.classList.remove("is-invalid")
    }
}

window.initAutocomplete = initAutocomplete;
