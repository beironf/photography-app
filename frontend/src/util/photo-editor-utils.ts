import { Photo, UpdatePhoto } from 'model/photo';
import { PhotoEditorState } from 'model/photo-editor';

export const stateIsComplete = (state: PhotoEditorState): boolean => state.camera !== undefined
  && state.lens !== undefined
  && state.focalLength !== undefined && state.focalLength !== ''
  && state.aperture !== undefined && state.aperture !== ''
  && state.exposureTime !== undefined && state.exposureTime !== ''
  && state.iso !== undefined && state.iso !== ''
  && state.date !== undefined
  && state.title !== undefined && state.title !== ''
  && state.category !== undefined
  && state.rating !== undefined
  && state.location !== undefined && state.location !== ''
  && state.country !== undefined && state.country !== ''
  && state.coordinates !== undefined;

export const convertStateToPhotoContent = (state: PhotoEditorState): UpdatePhoto => ({
  photographer: state.photographer,
  title: state.title,
  taken: state.date,
  location: {
    name: state.location,
    country: state.country,
    coordinates: {
      latitude: state.coordinates[0],
      longitude: state.coordinates[1],
    },
  },
  gear: { camera: state.camera, lens: state.lens },
  cameraSettings: {
    focalLength: state.focalLength,
    aperture: state.aperture,
    exposureTime: state.exposureTime,
    iso: state.iso,
  },
  metadata: {
    category: state.category,
    cameraTechniques: state.cameraTechniques,
    tags: state.tags,
  },
  judgement: { rating: state.rating, inShowroom: false },
});

export const photoNeedsUpdate = (
  photo: Photo,
  newContent: UpdatePhoto,
): boolean => photo.gear.camera !== newContent.gear.camera
  || photo.gear.lens !== newContent.gear.lens
  || photo.cameraSettings.focalLength !== newContent.cameraSettings.focalLength
  || photo.cameraSettings.aperture !== newContent.cameraSettings.aperture
  || photo.cameraSettings.exposureTime !== newContent.cameraSettings.exposureTime
  || photo.cameraSettings.iso !== newContent.cameraSettings.iso
  || photo.taken.getMilliseconds() !== newContent.taken.getMilliseconds()
  || photo.title !== newContent.title
  || photo.metadata.category !== newContent.metadata.category
  || photo.metadata.cameraTechniques !== newContent.metadata.cameraTechniques
  || photo.metadata.tags !== newContent.metadata.tags
  || photo.judgement.rating !== newContent.judgement.rating
  || photo.location.name !== newContent.location.name
  || photo.location.country !== newContent.location.country
  || photo.location.coordinates.latitude !== newContent.location.coordinates.latitude
  || photo.location.coordinates.longitude !== newContent.location.coordinates.longitude;
