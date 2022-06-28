import React, { useEffect, useState } from 'react';
import { Divider, Grid } from '@mui/material';
import { Exif } from 'model/exif';
import {
  PhotoCategory, CameraTechnique,
} from 'model/metadata';

import { TagsForm } from 'components/PhotoEditor/PhotoForms/TagsForm';
import { RatingForm } from 'components/PhotoEditor/PhotoForms/RatingForm';

import { getExif } from 'util/exif-util';

import { Camera, Lens } from 'model/camera';
import { Photo } from 'model/photo';
import { LocationForm } from './PhotoForms/LocationForm';
import { ExifForm } from './PhotoForms/ExifForm';
import { BasePhotoForm } from './PhotoForms/BasePhotoForm';
import { MapInput } from './Map/MapInput';

type Props = {
  imageId: string;
  photo?: Photo;
  onPhotoUpdated: (_: Photo) => void;
};

// eslint-disable-next-line no-unused-vars
export const PhotoEditor: React.FunctionComponent<Props> = ({ imageId, photo, onPhotoUpdated }) => {
  const [camera, setCamera] = useState<Camera>(photo?.gear.camera);
  const [lens, setLens] = useState<Lens>(photo?.gear.lens);
  const [focalLength, setFocalLength] = useState<string>(photo?.cameraSettings.focalLenght);
  const [aperture, setAperture] = useState<string>(photo?.cameraSettings.aperture);
  const [exposureTime, setExposureTime] = useState<string>(photo?.cameraSettings.exposureTime);
  const [iso, setIso] = useState<string>(photo?.cameraSettings.iso);
  const [date, setDate] = useState<Date>(photo?.taken);
  const [title, setTitle] = useState<string>(photo?.title);
  const [category, setCategory] = useState<PhotoCategory>(photo?.metadata.category);
  const [cameraTechniques, setCameraTechniques] = useState<CameraTechnique[]>(
    photo?.metadata.cameraTechniques ?? [],
  );
  const [rating, setRating] = useState<number>(photo?.judgement.rating);
  const [tags, setTags] = useState<string[]>(photo?.metadata.tags ?? []);
  const [location, setLocation] = useState<string>(photo?.location.name);
  const [country, setCountry] = useState<string>(photo?.location.country);
  const [coordinates, setCoordinates] = useState<number[]>(photo?.location !== undefined
    ? [photo!.location.coordinates.latitude, photo!.location.coordinates.longitude]
    : undefined); // [lat, long]

  const [exif, setExif] = useState<Exif>({} as Exif);

  // Fetch exif on image change
  useEffect(() => {
    getExif(imageId, setExif);
  }, [imageId, setExif]);

  useEffect(() => {
    if (exif.camera === undefined) {
      setCamera(undefined);
    }
    if (exif.lens === undefined) {
      setLens(undefined);
    }
    if (exif.focalLength === undefined) {
      setFocalLength(undefined);
    }
    if (exif.fNumber === undefined) {
      setAperture(undefined);
    }
    if (exif.exposureTime === undefined) {
      setExposureTime(undefined);
    }
    if (exif.iso === undefined) {
      setIso(undefined);
    }
    if (exif.date === undefined) {
      setDate(undefined);
    }
  }, [exif, setCamera, setLens, setFocalLength, setAperture, setExposureTime, setIso, setDate]);

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

        <BasePhotoForm
          title={title}
          category={category}
          cameraTechniques={cameraTechniques}
          setTitle={(t) => setTitle(t)}
          setCategory={(c) => setCategory(c)}
          setCameraTechniques={(cTs) => setCameraTechniques(cTs)}
        />

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
          country={country}
          coordinates={coordinates}
          setLocation={(s) => setLocation(s)}
          setCountry={(s) => setCountry(s)}
        />
      </Grid>
      <MapInput coordinates={coordinates} setCoordinates={(c) => setCoordinates(c)} />
    </div>
  );
};
