import * as EXIF from "exif-js";

import { Camera, Lens, CameraGear, CameraSettings } from "../model/camera";

export type PhotoExif = {
  cameraGear: {
    camera?: Camera,
    lens?: Lens,
  },
  cameraSettings: CameraSettings,
};

export const getExif = (img: any, setExif: (exif: PhotoExif) => any) => {
  EXIF.getData(img, () => {
    const all = EXIF.getAllTags(img);
    const camera = EXIF.getTag(img, "Model");
    const lens = Object.values(all).find((v: any) => Object.values(Lens).includes(v)) as Lens | undefined;
    const focalLenght = EXIF.getTag(img, "FocalLength") as number & {numerator: number, denominator: number};
    const fNumber = EXIF.getTag(img, "FNumber") as number & {numerator: number, denominator: number};
    const iso = EXIF.getTag(img, "ISO");
    const exposureTime = EXIF.getTag(img, "ExposureTime") as number & {numerator: number, denominator: number};
    setExif({
      cameraGear: {
        camera: Object.values(Camera).includes(camera) ? camera : undefined,
        lens,
      },
      cameraSettings: {
        focalLenght,
        fNumber,
        iso,
        exposureTime: exposureTime.numerator + "/" + exposureTime.denominator,
      },
    });
  });
};
