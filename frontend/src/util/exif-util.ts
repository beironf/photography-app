import { ImageApi } from 'api/ImageApi';
import { Camera, toCamera, toLens } from 'model/camera';
import { Exif } from 'model/exif';

export const getExif = (
  imgFilename: string,
  setExif: (_: Exif) => any,
): void => {
  ImageApi.ImageRoute.getExif(imgFilename).then((exif) =>
    setExif({
      camera: toCamera(exif.cameraModel),
      lens: toLens(exif.lens, toCamera(exif.cameraModel) === Camera.FUJI_X100F, toCamera(exif.cameraModel) === Camera.DJI_MINI_3),
      focalLength: exif.focalLength,
      fNumber: exif.fNumber,
      iso: exif.iso,
      exposureTime: exif.exposureTime,
      date: exif.date !== undefined ? new Date(exif.date) : undefined,
    }),
  );
};
