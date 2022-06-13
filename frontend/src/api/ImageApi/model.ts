export type ExifDto = {
  cameraMake?: string;
  cameraModel?: string;
  lens?: string;
  focalLength?: number;
  fNumber?: number;
  iso?: number;
  exposureTime?: string;
  date?: string;
};

export type ImageDto = {
  id: string;
  width: number;
  height: number;
};
