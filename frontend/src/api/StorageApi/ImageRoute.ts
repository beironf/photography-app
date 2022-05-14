import { BaseApi } from 'api/BaseApi';
import { AxiosRequestConfig, AxiosResponse } from 'axios';
import { Config } from 'Config';

// tslint:disable-next-line:no-namespace
export namespace ImageRoute {
  const api = new BaseApi(`${Config.imageApi}/v1/images/`);

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
