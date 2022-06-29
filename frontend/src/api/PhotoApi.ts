import { BaseApi } from 'api/BaseApi';
import { Config } from 'Config';
import { Photo, UpdatePhoto } from 'model/photo';

export namespace PhotoApi {
  const api = new BaseApi(`${Config.photoApi}/v1/photos/`);

  export function getPhoto(id: string): Promise<Photo> {
    return api.getData<Photo>(`${id}`)
      .then((photo) => ({ ...photo, taken: new Date(photo.taken) }));
  }

  export function listPhotos(): Promise<Photo[]> {
    return api.getData('');
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
