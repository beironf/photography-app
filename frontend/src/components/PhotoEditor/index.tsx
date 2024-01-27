/* eslint-disable no-unused-vars */
/* eslint-disable react-hooks/exhaustive-deps */

import React, { useCallback, useEffect, useState } from 'react';
import { usePromise } from 'hooks';

import { CircularProgress, Divider, Grid } from '@mui/material';

import { getExif } from 'util/exif-util';
import {
  convertStateToPhotoContent,
  stateIsComplete,
} from 'util/photo-editor-utils';
import { PhotoApi } from 'api/PhotoApi';

import { throttle } from 'lodash';

import { Exif } from 'model/exif';
import { PhotoEditorState } from 'model/photo-editor';

import { TagsForm } from 'components/PhotoEditor/PhotoForms/TagsForm';
import { JudgementForm } from 'components/PhotoEditor/PhotoForms/JudgementForm';
import { LocationForm } from 'components/PhotoEditor/PhotoForms/LocationForm';
import { ExifForm } from 'components/PhotoEditor/PhotoForms/ExifForm';
import { BasePhotoForm } from 'components/PhotoEditor/PhotoForms/BasePhotoForm';
import { MapInput } from 'components/PhotoEditor/Map/MapInput';
import { Photo, UpdatePhoto } from 'model/photo';
import { usePasswordContext } from 'contexts/PasswordContext';

const PHOTOGRAPHER = 'Fredrik Beiron';

const initialState: PhotoEditorState = {
  photographer: PHOTOGRAPHER,
  cameraTechniques: [],
  tags: [],
  inShowroom: false,
};
const initialExif: Exif = {};

type Props = {
  imageId: string;
  onNewPhoto: () => void;
};

const throttledUpdatePhoto = throttle(
  (imageId: string, update: UpdatePhoto, passwordToken: string) =>
    PhotoApi.updatePhoto(imageId, update, passwordToken),
  2000,
);

const throttledAddPhoto = throttle(
  (photo: Photo, passwordToken: string, callback: () => void) =>
    PhotoApi.addPhoto(photo, passwordToken, callback),
  5000,
);

export const PhotoEditor: React.FunctionComponent<Props> = ({
  imageId,
  onNewPhoto,
}) => {
  const [state, setState] = useState<PhotoEditorState>(initialState);
  const [exif, setExif] = useState<Exif>(initialExif);
  const [addingPhoto, setAddingPhoto] = useState(false);
  const { password } = usePasswordContext();

  const getPhotoCallback = useCallback(
    () => PhotoApi.getPhoto(imageId),
    [imageId],
  );
  const {
    trigger: getPhoto,
    data: photo,
    loading: getPhotoLoading,
  } = usePromise(getPhotoCallback);

  // Fetch exif and photo info on image change
  useEffect(() => {
    setExif(initialExif);
    setState(initialState);
    getExif(imageId, setExif);
    getPhoto();
  }, [imageId]);

  // On photo change - override values if photo exists
  useEffect(() => {
    if (photo !== undefined) {
      setAddingPhoto(false);
      setState((prev) => ({
        ...prev,
        camera: photo.gear.camera,
        lens: photo.gear.lens,
        focalLength: photo.cameraSettings.focalLength,
        aperture: photo.cameraSettings.aperture,
        exposureTime: photo.cameraSettings.exposureTime,
        iso: photo.cameraSettings.iso,
        date: photo.taken,
        title: photo.title,
        category: photo.metadata.category,
        group: photo.group,
        cameraTechniques: photo.metadata.cameraTechniques ?? [],
        tags: photo.metadata.tags ?? [],
        rating: photo.judgement.rating,
        inShowroom: photo.judgement.inShowroom,
        location: photo.location.name,
        country: photo.location.country,
        coordinates: [
          photo.location.coordinates.latitude,
          photo.location.coordinates.longitude,
        ],
      }));
    }
  }, [photo]);

  // Update photo on change if complete state
  useEffect(() => {
    if (stateIsComplete(state)) {
      const photoContent = convertStateToPhotoContent(state);

      if (photo === undefined && !addingPhoto) {
        setAddingPhoto(true);
        throttledAddPhoto({ imageId, ...photoContent }, password, () => {
          onNewPhoto();
          getPhoto();
        });
      } else if (photo !== undefined) {
        throttledUpdatePhoto(imageId, photoContent, password);
      }
    }
  }, [state]);

  return (
    <div>
      {!getPhotoLoading && (
        <MapInput
          coordinates={state.coordinates}
          setCoordinates={(coordinates) =>
            setState((prev) => ({ ...prev, coordinates }))
          }
        />
      )}
      {getPhotoLoading && <CircularProgress />}
      {!getPhotoLoading && (
        <Grid container spacing={2}>
          <LocationForm
            location={state.location}
            country={state.country}
            coordinates={state.coordinates}
            setLocation={(location) =>
              setState((prev) => ({ ...prev, location }))
            }
            setCountry={(country) => setState((prev) => ({ ...prev, country }))}
          />

          <Grid item xs={12}>
            <Divider variant="middle" />
          </Grid>

          <BasePhotoForm
            title={state.title}
            category={state.category}
            group={state.group}
            cameraTechniques={state.cameraTechniques}
            setTitle={(title) => setState((prev) => ({ ...prev, title }))}
            setCategory={(category) =>
              setState((prev) => ({ ...prev, category }))
            }
            setGroup={(group) => setState((prev) => ({ ...prev, group }))}
            setCameraTechniques={(cameraTechniques) =>
              setState((prev) => ({ ...prev, cameraTechniques }))
            }
          />

          <Grid item xs={12}>
            <Divider variant="middle" />
          </Grid>

          <ExifForm
            exif={exif}
            value={{
              camera: state.camera,
              lens: state.lens,
              focalLength: state.focalLength,
              aperture: state.aperture,
              exposureTime: state.exposureTime,
              iso: state.iso,
              date: state.date,
            }}
            setCamera={(camera) => setState((prev) => ({ ...prev, camera }))}
            setLens={(lens) => setState((prev) => ({ ...prev, lens }))}
            setFocalLength={(focalLength) =>
              setState((prev) => ({ ...prev, focalLength }))
            }
            setAperture={(aperture) =>
              setState((prev) => ({ ...prev, aperture }))
            }
            setExposureTime={(exposureTime) =>
              setState((prev) => ({ ...prev, exposureTime }))
            }
            setIso={(iso) => setState((prev) => ({ ...prev, iso }))}
            setDate={(date) => setState((prev) => ({ ...prev, date }))}
          />

          <Grid item xs={12}>
            <Divider variant="middle" />
          </Grid>

          <JudgementForm
            rating={state.rating}
            inShowroom={state.inShowroom}
            setRating={(rating) => setState((prev) => ({ ...prev, rating }))}
            setInShowroom={(inShowroom) =>
              setState((prev) => ({ ...prev, inShowroom }))
            }
          />

          <TagsForm
            tags={state.tags}
            setTags={(tags) => setState((prev) => ({ ...prev, tags }))}
          />
        </Grid>
      )}
    </div>
  );
};
