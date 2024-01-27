import { Config } from 'Config';

// tslint:disable-next-line:no-namespace
export namespace SiteImageRoute {
  export function getSiteImageUrl(filename: string): string {
    return `${Config.api}/v1/site-images/${filename}`;
  }
}
