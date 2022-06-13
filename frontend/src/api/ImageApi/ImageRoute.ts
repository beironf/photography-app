import { BaseApi } from 'api/BaseApi';
import { Config } from 'Config';
import { ExifDto, ImageDto } from 'api/ImageApi/model';
import { AxiosResponse } from 'axios';

// tslint:disable-next-line:no-namespace
export namespace ImageRoute {
  const api = new BaseApi(`${Config.imageApi}/v1/images/`);

  export function getImageUrl(filename: string): string {
    return `${Config.imageApi}/v1/images/${filename}`;
  }

  export function listImages(): Promise<ImageDto[]> {
    return api.getData('');
  }

  export function getExif(filename: string): Promise<ExifDto> {
    return api.getData(`${filename}/exif`);
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

  export function removeImage(imageId: string): Promise<AxiosResponse<any, any>> {
    return api.delete(`${imageId}`);
  }
}
