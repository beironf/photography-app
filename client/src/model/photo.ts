import { Location } from "./location";
import { CameraGear, CameraSettings } from "./camera";
import { Metadata } from "./metadata";

export type Photo = {
  imageId: string;
  photographer: string;
  title: string;
  description?: string;
  taken: any; // Timestamp
  location: Location;
  gear: CameraGear;
  cameraSettings: CameraSettings;
  metadata: Metadata;
};
