import { Location } from 'model/location';
import { CameraGear, CameraSettings } from 'model/camera';
import { Metadata } from 'model/metadata';

export type Judgement = {
  rating: number;
  inShowroom: boolean;
};

export type Photo = {
  imageId: string;
  photographer: string;
  title: string;
  description?: string;
  group?: string;
  taken: Date;
  location: Location;
  gear: CameraGear;
  cameraSettings: CameraSettings;
  metadata: Metadata;
  judgement: Judgement;
};
