import { BaseApi } from 'api/BaseApi';
import { Config } from 'Config';
import { Photo, PhotoWithRatio, UpdatePhoto } from 'model/photo';

export namespace PhotoApi {
  const api = new BaseApi(`${Config.photoApi}/v1/photos/`);

  const convertToDate = (photo: Photo): Photo => (
    { ...photo, taken: new Date(photo.taken) }
  );

  export function getPhoto(id: string): Promise<Photo> {
    return api.getData<Photo>(`${id}`)
      .then(convertToDate);
  }

  export function listPhotos(): Promise<PhotoWithRatio[]> {
    return api.getData<PhotoWithRatio[]>('')
      .then((photosWithRatio) => photosWithRatio.map(
        (pwr) => ({ photo: convertToDate(pwr.photo), width: pwr.width, height: pwr.height }),
      ));
  }

  export function addPhoto(photo: Photo, callback?: () => void): void {
    api.post('', photo)
      .then(callback);
  }

  export function updatePhoto(id: string, update: UpdatePhoto): void {
    api.post(`${id}`, update);
  }

  export function removePhoto(id: string): void {
    api.delete(`${id}`);
  }
}
