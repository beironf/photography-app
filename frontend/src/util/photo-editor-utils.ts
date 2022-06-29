import { UpdatePhoto } from 'model/photo';
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
  && state.tags.find((_) => _.trim() === '') === undefined
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
