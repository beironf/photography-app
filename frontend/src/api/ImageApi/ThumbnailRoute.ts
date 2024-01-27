import { BaseApi } from 'api/BaseApi';
import { Config } from 'Config';

// tslint:disable-next-line:no-namespace
export namespace ThumbnailRoute {
  // eslint-disable-next-line no-unused-vars
  const api = new BaseApi(`${Config.api}/v1/thumbnails/`);

  export function getThumbnailUrl(filename: string): string {
    return `${Config.api}/v1/thumbnails/${filename}`;
  }
}
