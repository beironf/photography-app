import { Camera, Lens } from './camera';

export type Exif = {
  camera?: Camera;
  lens?: Lens;
  focalLength?: number;
  fNumber?: number;
  exposureTime?: string;
  iso?: number;
  date?: Date;
};
