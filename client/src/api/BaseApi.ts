import * as qs from 'qs';
import axios from 'axios';

const queryStringConfig: qs.IStringifyOptions = {
  arrayFormat: 'comma',
  skipNulls: true,
  serializeDate(d: Date) {
    return d.toISOString().substr(0, 10);
  },
};

const paramsSerializer = (queryParams: any) =>
  qs.stringify(queryParams, queryStringConfig);

export class BaseApi {
  private apiUrl: string;

  constructor(baseUrl: string) {
    this.apiUrl = baseUrl;
  }

  public urlFor(path: string) {
    return this.apiUrl + path;
  }

  public get(path: string, params?: any) {
    return axios.get(this.urlFor(path), {
      params,
      paramsSerializer,
    });
  }

  public post(path: string, data?: any, params?: any) {
    return axios.post(this.urlFor(path), data, {
      params,
      paramsSerializer,
    });
  }

  public getData<T>(
    path: string,
    params?: any,
    isEnveloped = true,
    headers?: any,
  ) {
    return axios
      .get(this.urlFor(path), {
        params,
        paramsSerializer,
        headers,
      })
      .then((response: any) => this.mapResponse<T>(response, isEnveloped));
  }

  public postData<T>(
    path: string,
    data?: any,
    isEnveloped = true,
    headers?: any,
  ) {
    return axios
      .post(this.urlFor(path), data, {
        headers,
      })
      .then((response: any) => this.mapResponse<T>(response, isEnveloped));
  }

  public patchData<T>(path: string, data?: any, isEnveloped = true) {
    return axios
      .patch(this.urlFor(path), data)
      .then((response: any) => this.mapResponse<T>(response, isEnveloped));
  }

  private mapResponse<T>(response: any, isEnveloped: boolean) {
    if (isEnveloped) return response.data.data as T;
    else return response.data as T;
  }
}
