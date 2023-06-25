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

            let cityCandidate1 = "", cityCandidate2 = "";
            for (const component of place.address_components) {
                console.log(component);

                if (component.types.includes("route")) {
                    address += component.long_name;
                } else if (component.types.includes("political")) {
                    if(component.types.includes("locality"))
                        cityCandidate1 = component.short_name;
                    if (component.types.includes("administrative_area_level_1"))
                        cityCandidate2 = component.short_name;
                } else if (component.types.includes("street_number")) {
                    streetNumber = component.long_name;
                }
                else if (component.types.includes("postal_code")){
                    if(cityCandidate1.startsWith(component.long_name))
                        cityCandidate1 = "";
                }
            }

            if(cityCandidate1 !== "")
                city = cityCandidate1;
            else
                city = cityCandidate2;

            // Combine street number and address
            if (streetNumber) {
                address =  address + " " + streetNumber;
            }

            input.value = address;
            cityInput.value = city;
        }
    });
}

window.initMap = initMap;

