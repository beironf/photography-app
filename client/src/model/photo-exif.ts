import { Camera, CameraSettings, Lens } from "model/camera";

export type PhotoExif = {
  cameraGear: {
    camera?: Camera;
    lens?: Lens;
  };
  cameraSettings: CameraSettings;
  date?: Date;
};
