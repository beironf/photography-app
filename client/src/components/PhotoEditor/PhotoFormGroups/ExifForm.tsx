import { Camera, Lens } from "model/camera";
import { PhotoExif } from "model/photo-exif";
import React from "react";
import { Row, Form, Col } from "react-bootstrap";

type Props = {
  exif: PhotoExif;
};

export const ExifForm: React.FunctionComponent<Props> = ({ exif }) => {
  return (
    <>
      <Form.Group as={Row}>
        <Form.Label column sm="2">
          Camera
        </Form.Label>
        <Col sm="10">
          <Form.Control
            name="camera"
            as="select"
            required
            disabled={exif.cameraGear.camera != null}
          >
            {exif.cameraGear.camera != null && (
              <option key={"camera-" + exif.cameraGear.camera}>
                {exif.cameraGear.camera}
              </option>
            )}
            {exif.cameraGear.camera == null &&
              Object.values(Object.values(Camera)).map((camera) => {
                return <option key={"camera-" + camera}>{camera}</option>;
              })}
          </Form.Control>
        </Col>
      </Form.Group>

      <Form.Group as={Row}>
        <Form.Label column sm="2">
          Lens
        </Form.Label>
        <Col sm="10">
          <Form.Control
            name="lens"
            as="select"
            required
            disabled={exif.cameraGear.lens != null}
          >
            {exif.cameraGear.lens != null && (
              <option key={"lens-" + exif.cameraGear.lens}>
                {exif.cameraGear.lens}
              </option>
            )}
            {exif.cameraGear.lens == null &&
              Object.values(Object.values(Lens)).map((lens) => {
                return <option key={"lens-" + lens}>{lens}</option>;
              })}
          </Form.Control>
        </Col>
      </Form.Group>

      <Form.Group as={Row}>
        <Form.Label column sm="2">
          Exposure Time
        </Form.Label>
        <Col sm="10">
          <Form.Control
            name="exposureTime"
            type="text"
            plaintext={exif.cameraSettings.exposureTime !== ""}
            disabled={exif.cameraSettings.exposureTime !== ""}
            placeholder={exif.cameraSettings.exposureTime}
            required
          />
        </Col>
      </Form.Group>

      <Form.Group as={Row}>
        <Form.Label column sm="2">
          Aperture
        </Form.Label>
        <Col sm="10">
          <Form.Control
            name="aperture"
            type="text"
            plaintext={!isNaN(exif.cameraSettings.fNumber)}
            disabled={!isNaN(exif.cameraSettings.fNumber)}
            placeholder={
              !isNaN(exif.cameraSettings.fNumber)
                ? exif.cameraSettings.fNumber.toString()
                : ""
            }
            required
          />
        </Col>
      </Form.Group>

      <Form.Group as={Row}>
        <Form.Label column sm="2">
          ISO
        </Form.Label>
        <Col sm="10">
          <Form.Control
            name="iso"
            type="text"
            plaintext={!isNaN(exif.cameraSettings.iso)}
            disabled={!isNaN(exif.cameraSettings.iso)}
            placeholder={
              !isNaN(exif.cameraSettings.iso)
                ? exif.cameraSettings.iso.toString()
                : ""
            }
            required
          />
        </Col>
      </Form.Group>
    </>
  );
};
