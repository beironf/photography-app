// tslint:disable-next-line:no-namespace
export namespace PhotoRoute {
  const api = new BaseApi(Config.userApi + '/v1/organization/');

  export function createOrganization(name: string, domain: string) {
    return api.post('create', {
      name,
      domain,
    });
  }

  export function listOrganizations() {
    return api.get('list');
  }
}
