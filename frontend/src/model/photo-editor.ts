import { Camera, Lens } from 'model/camera';
import { CameraTechnique, PhotoCategory } from 'model/metadata';

export type PhotoEditorState = {
  photographer: string;
  camera?: Camera;
  lens?: Lens;
  focalLength?: string;
  aperture?: string;
  exposureTime?: string;
  iso?: string;
  date?: Date;
  title?: string;
  category?: PhotoCategory;
  group?: string;
  cameraTechniques: CameraTechnique[];
  tags: string[];
  rating?: number;
  inShowroom: boolean;
  location?: string;
  country?: string;
  coordinates?: number[]; // [lat, long]
};
