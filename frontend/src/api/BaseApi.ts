import * as qs from 'qs';
import axios, { AxiosRequestConfig, AxiosResponse } from 'axios';

const queryStringConfig: qs.IStringifyOptions = {
  arrayFormat: 'comma',
  skipNulls: true,
  serializeDate(d: Date) {
    return d.toISOString().substr(0, 10);
  },
};

const paramsSerializer = (queryParams: any): string => qs.stringify(queryParams, queryStringConfig);

export class BaseApi {
  private apiUrl: string;

  constructor(baseUrl: string) {
    this.apiUrl = baseUrl;
  }

  public urlFor(path: string): string {
    return this.apiUrl + path;
  }

  public get(path: string, params?: any): Promise<AxiosRequestConfig> {
    return axios.get(this.urlFor(path), {
      params,
      paramsSerializer,
    });
  }

  public post(path: string, data?: any, params?: any, headers?: any): Promise<AxiosResponse> {
    return axios.post(this.urlFor(path), data, {
      params,
      paramsSerializer,
      headers,
    });
  }

  public delete(path: string, params?: any, data?: any, headers?: any): Promise<AxiosResponse> {
    return axios.delete(this.urlFor(path), {
      params,
      paramsSerializer,
      data,
      headers,
    });
  }

  public getData<T>(
    path: string,
    params?: any,
    headers?: any,
    isEnveloped = true,
  ): Promise<T> {
    return axios
      .get(this.urlFor(path), {
        params,
        paramsSerializer,
        headers,
      })
      .then((response: any) => BaseApi.mapResponse<T>(response, isEnveloped));
  }

  public postData<T>(
    path: string,
    data?: any,
    headers?: any,
    isEnveloped = true,
  ): Promise<T> {
    return axios
      .post(this.urlFor(path), data, {
        headers,
      })
      .then((response: any) => BaseApi.mapResponse<T>(response, isEnveloped));
  }

  public patchData<T>(path: string, data?: any, isEnveloped = true): Promise<T> {
    return axios
      .patch(this.urlFor(path), data)
      .then((response: any) => BaseApi.mapResponse<T>(response, isEnveloped));
  }

  static mapResponse<T>(response: any, isEnveloped: boolean): T {
    if (isEnveloped) return response.data.data as T;
    return response.data as T;
  }
}
