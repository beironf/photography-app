import _ from 'lodash';

export const toCamelCase = (id: string): string => _.camelCase(id);
