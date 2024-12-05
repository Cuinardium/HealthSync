import React, { useRef, useEffect, useState } from "react";
import { useMapsLibrary } from "@vis.gl/react-google-maps";
import { useTranslation } from "react-i18next";
import { Form } from "react-bootstrap";

interface PlaceAutocompleteProps {
  onPlaceSelect: (address: string, city: string) => void;
  error?: string;
  defaultValue?: string;
}

const PlaceAutocomplete: React.FC<PlaceAutocompleteProps> = ({
  onPlaceSelect,
  error,
  defaultValue,
}) => {
  const [placeAutocomplete, setPlaceAutocomplete] =
    useState<google.maps.places.Autocomplete | null>(null);
  const inputRef = useRef<HTMLInputElement>(null);
  const places = useMapsLibrary("places");

  const { t } = useTranslation();

  useEffect(() => {
    if (!places || !inputRef.current) return;

    const options = {
      fields: ["address_components"],
      strictBounds: false,
      types: ["address"],
      componentRestrictions: { country: "AR" },
    };

    setPlaceAutocomplete(new places.Autocomplete(inputRef.current, options));
  }, [places]);

  useEffect(() => {
    if (!placeAutocomplete) return;

    placeAutocomplete.addListener("place_changed", () => {
      const place = placeAutocomplete.getPlace();
      let city = "";
      let address = "";
      let streetNumber = "";

      let cityCandidate1 = "",
        cityCandidate2 = "";

      if (place.address_components) {
        for (const component of place.address_components) {
          if (component.types.includes("route")) {
            address += component.long_name;
          } else if (component.types.includes("political")) {
            if (component.types.includes("locality")) {
              cityCandidate1 = component.short_name;
            }
            if (component.types.includes("administrative_area_level_1")) {
              cityCandidate2 = component.short_name;
            }
          } else if (component.types.includes("street_number")) {
            streetNumber = component.long_name;
          } else if (component.types.includes("postal_code")) {
            if (cityCandidate1.startsWith(component.long_name)) {
              cityCandidate1 = "";
            }
          }
        }
      }

      if (cityCandidate1 !== "") {
        city = cityCandidate1;
      } else {
        city = cityCandidate2;
      }

      const fullAddress = `${address} ${streetNumber}`;

      if (inputRef.current) {
        inputRef.current.value = fullAddress;
      }

      onPlaceSelect(fullAddress, city);
    });
  }, [onPlaceSelect, placeAutocomplete]);

  return (
    <div className="autocomplete-container">
      <Form.Control
        name="address"
        placeholder={t("form.address")}
        ref={inputRef}
        defaultValue={defaultValue}
        isInvalid={!!error}
      />
      <Form.Control.Feedback type="invalid">
        {error && t(error ?? "")}
      </Form.Control.Feedback>
    </div>
  );
};

export default PlaceAutocomplete;
