import React, { useState } from 'react';
import { Form, Button, Image } from 'react-bootstrap';
import {
  useMapEvent, Marker, TileLayer, MapContainer,
} from 'react-leaflet';
import { LatLng, LatLngExpression, LeafletMouseEvent } from 'leaflet';

import { Photo } from 'model/photo';
import { PhotoExif } from 'model/photo-exif';
import { PhotoCategory, CameraTechnique } from 'model/metadata';

import { ImageUploader } from 'components/PhotoEditor/ImageUploader';
import { PhotoSelector } from 'components/PhotoEditor/PhotoSelector';
import { TagsForm } from 'components/PhotoEditor/PhotoFormGroups/TagsForm';
import { RatingForm } from 'components/PhotoEditor/PhotoFormGroups/RatingForm';

import { toCamelCase } from 'util/string-id-utils';
import { getExif } from 'util/exif-util';

import { StorageApi } from 'api/StorageApi';
import { LocationForm } from './PhotoFormGroups/LocationForm';
import { DateForm } from './PhotoFormGroups/DateForm';

const storageApi = new StorageApi();

const initialExif = {
  cameraGear: {},
  cameraSettings: {
    focalLenght: NaN,
    fNumber: NaN,
    iso: NaN,
    exposureTime: '',
  },
  date: undefined,
};

type LocationProps = {
  location?: LatLngExpression;
  setLocation: (_: LatLng) => void;
}

const LocationMarker: React.FunctionComponent<LocationProps> = ({ location, setLocation }) => {
  const map = useMapEvent(
    'click',
    (e: LeafletMouseEvent) => {
      setLocation(e.latlng);
      map.flyTo(e.latlng, map.getZoom());
    },
  );

  return location === undefined ? null : (
    <Marker position={location} />
  );
};

type Props = {
  onSubmit: () => void;
};

export const PhotoForm: React.FunctionComponent<Props> = ({ onSubmit }) => {
  const [validated, setValidated] = useState(false);
  const [imageFilename, setImageFilename] = useState<string>('IMG_6071.jpg');
  // eslint-disable-next-line no-unused-vars
  const [photo, setPhoto] = useState<Photo>();
  const [exif, setExif] = useState<PhotoExif>(initialExif);
  const [mapLatLng, setMapLatLng] = useState<number[]>();

  const handleSubmit = (event: any): void => {
    event.preventDefault();
    event.stopPropagation();

    const form = event.currentTarget;
    // form.elements.name.value
    if (form.checkValidity()) {
      onSubmit();
    }

    setValidated(true);
  };

  const onImageUploaded = (filename: string): void => {
    setImageFilename(filename);
  };

  const onPhotoSelected = (filename: string): void => {
    setImageFilename(filename);
  };

  const loadExif = (event: any): void => {
    getExif(event.currentTarget, setExif);
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
            {Object.values(PhotoCategory)
              .map((category) => <option key={`category-${category}`}>{category}</option>)}
          </Form.Control>
        </Form.Group>

        <Form.Group>
          <Form.Label>Camera Technique</Form.Label>
          {Object.values(CameraTechnique).map((technique) => (
            <Form.Check
              name={toCamelCase(`cameraTechnique ${technique}`)}
              key={`camera-technique-${technique}`}
              type="checkbox"
              label={technique}
            />
          ))}
        </Form.Group>

        <DateForm exif={exif} />
        <LocationForm mapLatLng={mapLatLng} />
        <TagsForm name="Tags" placeholder="tags separated by space" />
        <RatingForm name="Rating" maxValue={5} />

        <Button variant="primary" type="submit">
          Submit
        </Button>
      </Form>

      <MapContainer
        style={{ height: 500 }}
        center={[50, 10]}
        zoom={3}
      >
        <TileLayer
          attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
          url="http://{s}.tile.osm.org/{z}/{x}/{y}.png"
        />
        <LocationMarker
          location={mapLatLng ? mapLatLng as LatLngExpression : undefined}
          setLocation={(latLng) => setMapLatLng([latLng.lat, latLng.lng])}
        />
      </MapContainer>
    </div>
  );
};
