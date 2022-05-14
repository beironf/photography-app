import { BaseApi } from 'api/BaseApi';
import { AxiosRequestConfig, AxiosResponse } from 'axios';
import { Config } from 'Config';

export class PhotoApi {
  private api = new BaseApi(`${Config.photoApi}/v1/photos/`);

  public createOrganization(name: string, domain: string): Promise<AxiosResponse> {
    return this.api.post('create', {
      name,
      domain,
    });
  }

  public listOrganizations(): Promise<AxiosRequestConfig> {
    return this.api.get('list');
  }
}
