type EnvironmentConfig = {
  environment: string;
  photoApi: string;
  imageApi: string;
};

const DEVELOPMENT_CONFIG: EnvironmentConfig = {
  environment: 'development',
  photoApi: 'http://localhost:3001',
  imageApi: 'http://localhost:3002',
};

const PRODUCTION_CONFIG: EnvironmentConfig = {
  environment: 'production',
  photoApi: 'http://photo-api.default.svc.cluster.local:3001',
  imageApi: 'http://image-api.default.svc.cluster.local:3002',
};

const host = window.location.hostname;
const isDevelopment = host.includes('localhost');

export const Config: EnvironmentConfig = isDevelopment
  ? DEVELOPMENT_CONFIG
  : PRODUCTION_CONFIG;
