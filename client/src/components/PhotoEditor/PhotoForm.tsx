import React, { useState } from "react";
import { Form, Button, Image, Row, Col } from "react-bootstrap";
import { Map, Marker, TileLayer } from "react-leaflet";
import { LatLngExpression } from "leaflet";

import { ImageUploader } from "./ImageUploader";
import { PhotoSelector } from "./PhotoSelector";
import { getExif, PhotoExif } from "./getExif";

import { Photo } from "../../model/photo";
import { PhotoCategory, CameraTechnique } from "../../model/metadata";
import { Camera, Lens } from "../../model/camera";
import { TagsFormGroup } from "./TagsFormGroup";
import { RatingFormGroup } from "./RatingFormGroup";
import { formatDate } from "../../util/date-util";
import { StorageApi } from "../../api/StorageApi";
import { toCamelCase } from "../../util/string-id-utils";

const storageApi = new StorageApi();

const initialExif = {
  cameraGear: {},
  cameraSettings: {
    focalLenght: NaN,
    fNumber: NaN,
    iso: NaN,
    exposureTime: "",
  },
  date: undefined,
};

type Props = {
  onSubmit: () => void;
};

export const PhotoForm: React.FunctionComponent<Props> = ({ onSubmit }) => {
  const [validated, setValidated] = useState(false);
  const [imageFilename, setImageFilename] = useState<string>("IMG_6071.jpg");
  const [photo, setPhoto] = useState<Photo>();
  const [exif, setExif] = useState<PhotoExif>(initialExif);
  const [mapLatLng, setMapLatLng] = useState<number[]>();

  const handleSubmit = (event: any) => {
    event.preventDefault();
    event.stopPropagation();

    const form = event.currentTarget;
    // form.elements.name.value
    if (form.checkValidity()) {
      onSubmit();
    }

    setValidated(true);
  };

  const onImageUploaded = (filename: string) => {
    setImageFilename(filename);
  };

  const onPhotoSelected = (filename: string) => {
    setImageFilename(filename);
  };

  const loadExif = (event: any) => {
    getExif(event.currentTarget, setExif);
  };

  const handleMapClick = (event: any) => {
    setMapLatLng([event.latlng.lat, event.latlng.lng]);
  };

  if (imageFilename === undefined) {
    return (
      <div>
        <ImageUploader onImageUploaded={onImageUploaded} />
        <PhotoSelector onPhotoSelected={onPhotoSelected} />
      </div>
    );
  }

  return (
    <div>
      <Image
        style={{ height: 200 }}
        id="image"
        src={storageApi.getImageUrl(imageFilename)}
        onLoad={loadExif}
      />
      <Form noValidate validated={validated} onSubmit={handleSubmit}>
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

        <Form.Group>
          <Form.Label>Title</Form.Label>
          <Form.Control
            name="title"
            type="text"
            placeholder="Title of Photo"
            required
          />
        </Form.Group>

        <Form.Group>
          <Form.Label>Category</Form.Label>
          <Form.Control name="category" as="select" required>
            {Object.values(PhotoCategory).map((category) => {
              return <option key={"category-" + category}>{category}</option>;
            })}
          </Form.Control>
        </Form.Group>

        <Form.Group>
          <Form.Label>Camera Technique</Form.Label>
          {Object.values(CameraTechnique).map((technique) => {
            return (
              <Form.Check
                name={toCamelCase(`cameraTechnique ${technique}`)}
                key={"camera-technique-" + technique}
                type="checkbox"
                label={technique}
              />
            );
          })}
        </Form.Group>

        <Form.Group as={Row}>
          <Form.Label column sm="2">
            Date
          </Form.Label>
          <Col sm="10">
            <Form.Control
              name="date"
              type="text"
              plaintext={exif.date !== undefined}
              disabled={exif.date !== undefined}
              placeholder={
                exif.date ? formatDate(exif.date) : "YYYY-MM-DD HH:MM:SS"
              }
              required
            />
          </Col>
        </Form.Group>

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

        <TagsFormGroup name="Tags" placeholder="tags separated by space" />
        <RatingFormGroup name="Rating" maxValue={5} />

        <Button variant="primary" type="submit">
          Submit
        </Button>
      </Form>

      <Map
        style={{ height: 500 }}
        center={[50, 10]}
        zoom={3}
        onclick={handleMapClick}
      >
        <TileLayer
          attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
          url="http://{s}.tile.osm.org/{z}/{x}/{y}.png"
        />
        {mapLatLng && <Marker position={mapLatLng as LatLngExpression} />}
      </Map>
    </div>
  );
};
