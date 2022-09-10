import { BaseApi } from 'api/BaseApi';
import { Config } from 'Config';
import { PhotoCategory } from 'model/metadata';
import { Photo, PhotoWithRatio, UpdatePhoto } from 'model/photo';

export namespace PhotoApi {
  const api = new BaseApi(`${Config.photoApi}/v1/`);

  const convertToDate = (photo: Photo): Photo => (
    { ...photo, taken: new Date(photo.taken) }
  );

  export function getPhoto(id: string): Promise<Photo> {
    return api.getData<Photo>(`photos/${id}`)
      .then(convertToDate);
  }

  export function listPhotos(
    category?: PhotoCategory,
    group?: string,
    rating?: number,
    inShowroom?: boolean,
  ): Promise<PhotoWithRatio[]> {
    return api.getData<PhotoWithRatio[]>('photos', {
      category, group, rating, inShowroom,
    })
      .then((photosWithRatio) => photosWithRatio.map(
        (pwr) => ({ photo: convertToDate(pwr.photo), width: pwr.width, height: pwr.height }),
      ));
  }

  export function listPhotoGroups(): Promise<string[]> {
    return api.getData<string[]>('photo-groups');
  }

  export function addPhoto(photo: Photo, callback?: () => void): void {
    api.post('photos', photo)
      .then(callback);
  }

  export function updatePhoto(id: string, update: UpdatePhoto): void {
    api.post(`photos/${id}`, update);
  }

  export function removePhoto(id: string): void {
    api.delete(`photos/${id}`);
  }
}
