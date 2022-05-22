import { BaseApi } from 'api/BaseApi';
import { Config } from 'Config';

// tslint:disable-next-line:no-namespace
export namespace ImageRoute {
  const api = new BaseApi(`${Config.imageApi}/v1/images/`);

  export function getImageUrl(filename: string): string {
    return `${Config.imageApi}/v1/images/${filename}`;
  }

  export function uploadImage(formData: FormData, onImageUploaded: () => void): void {
    api
      .post('', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      })
      .then((_: any) => {
        onImageUploaded();
      });
  }
}
