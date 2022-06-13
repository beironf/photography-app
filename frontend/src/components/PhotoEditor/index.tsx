import React, { useEffect, useState } from 'react';
import { Divider, Grid } from '@mui/material';
import {
  useMapEvent, Marker, TileLayer, MapContainer,
} from 'react-leaflet';
import { LatLng, LatLngExpression, LeafletMouseEvent } from 'leaflet';
import { Exif } from 'model/exif';
import {
  PhotoCategory, CameraTechnique, toCategory, toCameraTechnique,
} from 'model/metadata';

import { TagsForm } from 'components/PhotoEditor/PhotoFormGroups/TagsForm';
import { RatingForm } from 'components/PhotoEditor/PhotoFormGroups/RatingForm';

import { getExif } from 'util/exif-util';

import { Camera, Lens } from 'model/camera';
import { SelectField } from 'components/SelectField';
import { MultiSelectField } from 'components/MultiSelectField';
import { InputTextField } from 'components/InputTextField';
import { LocationForm } from './PhotoFormGroups/LocationForm';
import { ExifForm } from './PhotoFormGroups/ExifForm';

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
  imageId: string;
};

export const PhotoEditor: React.FunctionComponent<Props> = ({ imageId }) => {
  const [exif, setExif] = useState<Exif>({} as Exif);

  const [camera, setCamera] = useState<Camera>();
  const [lens, setLens] = useState<Lens>();
  const [focalLength, setFocalLength] = useState<string>();
  const [aperture, setAperture] = useState<string>();
  const [exposureTime, setExposureTime] = useState<string>();
  const [iso, setIso] = useState<string>();
  const [date, setDate] = useState<Date>();
  const [title, setTitle] = useState<string>();
  const [category, setCategory] = useState<PhotoCategory>();
  const [cameraTechniques, setCameraTechniques] = useState<CameraTechnique[]>([]);
  const [rating, setRating] = useState<number>();
  const [tags, setTags] = useState<string[]>();
  const [location, setLocation] = useState<string>();
  const [coordinates, setCoordinates] = useState<number[]>(); // [lat, long]

  useEffect(() => {
    getExif(imageId, setExif);
  }, [imageId, setExif]);

  return (
    <div>
      <Grid container spacing={2}>
        <ExifForm
          exif={exif}
          value={{
            camera, lens, focalLength, aperture, exposureTime, iso, date,
          }}
          setCamera={(c) => setCamera(c)}
          setLens={(l) => setLens(l)}
          setFocalLength={(fL) => setFocalLength(fL)}
          setAperture={(a) => setAperture(a)}
          setExposureTime={(eT) => setExposureTime(eT)}
          setIso={(i) => setIso(i)}
          setDate={(d) => setDate(d)}
        />

        <Grid item xs={12}>
          <Divider variant="middle" />
        </Grid>

        <Grid item>
          <InputTextField
            id="title"
            label="Title"
            value={title}
            onChange={(s) => setTitle(s)}
            required
          />
        </Grid>

        <Grid item>
          <SelectField
            id="category"
            label="Category"
            options={
              Object.values(PhotoCategory)
                .map((c) => ({ value: c, label: c }))
            }
            value={category}
            onChange={(s) => setCategory(toCategory(s as string))}
            required
          />
        </Grid>

        <Grid item>
          <MultiSelectField
            id="camera-techniques"
            label="Camera Techniques"
            options={
              Object.values(CameraTechnique)
                .map((cT) => ({ value: cT, label: cT }))
            }
            values={cameraTechniques.map((cT) => cT.toString())}
            onChange={(ss) => setCameraTechniques(ss.map((s) => toCameraTechnique(s)))}
          />
        </Grid>

        <TagsForm tags={tags} setTags={(ss) => setTags(ss)} />

        <Grid item xs={12}>
          <Divider variant="middle" />
        </Grid>

        <RatingForm rating={rating} setRating={(r) => setRating(r)} />

        <Grid item xs={12}>
          <Divider variant="middle" />
        </Grid>

        <LocationForm
          location={location}
          coordinates={coordinates}
          setLocation={(s) => setLocation(s)}
        />
      </Grid>

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
          location={coordinates ? coordinates as LatLngExpression : undefined}
          setLocation={(latLng) => setCoordinates([latLng.lat, latLng.lng])}
        />
      </MapContainer>
    </div>
  );
};
