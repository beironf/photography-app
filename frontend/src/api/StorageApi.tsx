import axios from 'axios';

export class StorageApi {
  private API_URL = 'http://localhost:3001/api';

  public getImageUrl(filename: string): string {
    return `${this.API_URL}/images?filename=${filename}`;
  }

  public getThumbnailUrl(filename: string): string {
    return `${this.API_URL}/thumbnails?filename=${filename}`;
  }

  public uploadImage(formData: FormData, onImageUploaded: () => void): void {
    axios
      .post(`${this.API_URL}/images/upload`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      })
      .then((_: any) => {
        onImageUploaded();
      });
  }
}
