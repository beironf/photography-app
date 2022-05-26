import { Camera, Lens } from './camera';

export type Exif = {
  camera?: Camera;
  lens?: Lens;
  focalLength?: number,
  fNumber?: number,
  iso?: number,
  exposureTime?: string,
  date?: Date;
};
