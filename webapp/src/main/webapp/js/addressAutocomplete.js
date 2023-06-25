/**
 * @license
 * Copyright 2019 Google LLC. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
function initMap() {
    const input = document.getElementById("addr-input");
    const cityInput = document.getElementById("city-input");
    cityInput.disabled = true;
    const options = {
        fields: ["address_components"],
        strictBounds: false,
        types: ["address"],
    };

    const autocomplete = new google.maps.places.Autocomplete(input, options);

    autocomplete.setComponentRestrictions({
        country: ["ar"],
    });

    // Add event listener to the input field
    input.addEventListener("input", () => {
        cityInput.value = "";
    });

    autocomplete.addListener("place_changed", () => {
        const place = autocomplete.getPlace();

        if (place.address_components) {
            let address = "";
            let city = "";
            let streetNumber = "";

            for (let i = 0; i < place.address_components.length; i++) {
                const component = place.address_components[i];

                if (component.types.includes("route")) {
                    address += component.long_name + " ";
                } else if (component.types.includes("locality")) {
                    city = component.long_name;
                } else if (component.types.includes("street_number")) {
                    streetNumber = component.long_name;
                }
            }

            // Combine street number and address
            if (streetNumber) {
                address =  address + streetNumber;
            }

            input.value = address;
            cityInput.value = city;
        }
    });
}

window.initMap = initMap;

