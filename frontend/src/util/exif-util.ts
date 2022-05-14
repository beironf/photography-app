import * as EXIF from 'exif-js';

import { Camera, Lens } from 'model/camera';
import { PhotoExif } from 'model/photo-exif';

// EXIF date time is formated as YYYY:MMY:DD HH:MM:SS
const toDate = (dateTimeOriginal: string): Date => {
  const dateTimeSplit = dateTimeOriginal.split(' ');
  const properDateStr = dateTimeSplit[0].replace(/:/g, '-');
  const properDateTimeStr = `${properDateStr} ${dateTimeSplit[1]}`;

  return new Date(properDateTimeStr);
};

export const getExif = (img: any, setExif: (_: PhotoExif) => any): void => {
  EXIF.getData(img, () => {
    const all = EXIF.getAllTags(img);
    const camera = EXIF.getTag(img, 'Model');
    const lens = Object.values(all)
      .find((v: any) => Object.values(Lens).includes(v)) as Lens | undefined;
    const focalLenght = EXIF.getTag(img, 'FocalLength') as number & {
      numerator: number;
      denominator: number;
    };
    const fNumber = EXIF.getTag(img, 'FNumber') as number & {
      numerator: number;
      denominator: number;
    };
    const iso = EXIF.getTag(img, 'ISO');
    const exposureTime = EXIF.getTag(img, 'ExposureTime') as number & {
      numerator: number;
      denominator: number;
    };
    const date = toDate(EXIF.getTag(img, 'DateTimeOriginal'));
    setExif({
      cameraGear: {
        camera: Object.values(Camera).includes(camera) ? camera : undefined,
        lens,
      },
      cameraSettings: {
        focalLenght,
        fNumber,
        iso,
        exposureTime: `${exposureTime.numerator}/${exposureTime.denominator}`,
      },
      date,
    });
  });
};
