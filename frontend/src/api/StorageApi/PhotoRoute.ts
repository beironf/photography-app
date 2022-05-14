import { BaseApi } from 'api/BaseApi';
import { AxiosRequestConfig, AxiosResponse } from 'axios';
import { Config } from 'Config';

// tslint:disable-next-line:no-namespace
export namespace PhotoRoute {
  const api = new BaseApi(`${Config.photoApi}/v1/photos/`);

  export function createOrganization(name: string, domain: string): Promise<AxiosResponse> {
    return api.post('create', {
      name,
      domain,
    });
  }

  export function listOrganizations(): Promise<AxiosRequestConfig> {
    return api.get('list');
  }
}
