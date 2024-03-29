import { BaseApi } from 'api/BaseApi';
import { Config } from 'Config';
import { PhotoCategory } from 'model/metadata';
import { Photo, PhotoWithRatio, UpdatePhoto } from 'model/photo';

export namespace PhotoApi {
  const api = new BaseApi(`${Config.api}/v1/`);

  const convertToDate = (photo: Photo): Photo => ({
    ...photo,
    taken: new Date(photo.taken),
  });

  export function validatePassword(
    password: string,
    authorizedCallback: () => void,
    unauthorizedCallback: () => void,
  ): Promise<boolean> {
    return api
      .post('auth/validate', undefined, undefined, {
        Authorization: `Bearer ${password}`,
      })
      .then(
        () => {
          authorizedCallback();
          return true;
        },
        () => {
          unauthorizedCallback();
          return false;
        },
      );
  }

  export function getPhoto(id: string): Promise<Photo> {
    return api.getData<Photo>(`photos/${id}`).then(convertToDate);
  }

  export function listPhotos(
    category?: PhotoCategory,
    group?: string,
    rating?: number,
    inShowroom?: boolean,
  ): Promise<PhotoWithRatio[]> {
    return api
      .getData<PhotoWithRatio[]>('photos', {
        category,
        group,
        rating,
        inShowroom,
      })
      .then((photosWithRatio) =>
        photosWithRatio.map((pwr) => ({
          photo: convertToDate(pwr.photo),
          width: pwr.width,
          height: pwr.height,
        })),
      );
  }

  export function listPhotoGroups(): Promise<string[]> {
    return api.getData<string[]>('photo-groups');
  }

  export function addPhoto(
    photo: Photo,
    passwordToken: string,
    callback?: () => void,
  ): void {
    api
      .post('photos', photo, undefined, {
        Authorization: `Bearer ${passwordToken}`,
      })
      .then(callback);
  }

  export function updatePhoto(
    id: string,
    update: UpdatePhoto,
    passwordToken: string,
  ): void {
    api.post(`photos/${id}`, update, undefined, {
      Authorization: `Bearer ${passwordToken}`,
    });
  }
}
