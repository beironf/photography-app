import { BaseApi } from 'api/BaseApi';
import { Config } from 'Config';
import { Photo } from 'model/photo';

export namespace PhotoApi {
  const api = new BaseApi(`${Config.photoApi}/v1/photos/`);

  export function getPhoto(id: string): Promise<Photo> {
    return api.getData(`${id}`);
  }

  export function listPhotos(): Promise<Photo[]> {
    return api.getData('');
  }

  export function addPhoto(photo: Photo): void {
    api.post('', photo);
  }

  export function removePhoto(id: string): void {
    api.post(`${id}`);
  }
}
