import React from "react";
import { Row, Form, Col } from "react-bootstrap";

type Props = {
  mapLatLng?: number[];
};

export const LocationForm: React.FunctionComponent<Props> = ({ mapLatLng }) => {
  return (
    <>
      <Form.Group>
        <Form.Label>Location</Form.Label>
        <Form.Control
          name="location"
          type="text"
          placeholder="Name of Location"
          required
        />
      </Form.Group>

      <Form.Group as={Row}>
        <Form.Label column sm="2">
          Coordinates
        </Form.Label>
        <Col sm="10">
          <Form.Control
            name="coordinates"
            type="text"
            plaintext
            disabled
            placeholder={
              mapLatLng
                ? "Latitude: " +
                  Math.round(mapLatLng[0] * 1000) / 1000 +
                  "  Longitude: " +
                  Math.round(mapLatLng[1] * 1000) / 1000
                : "Click on the map to choose"
            }
            required
          />
        </Col>
      </Form.Group>
    </>
  );
};
