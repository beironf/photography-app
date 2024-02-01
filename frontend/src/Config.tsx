type EnvironmentConfig = {
  environment: string;
  api: string;
};

const DEVELOPMENT_CONFIG: EnvironmentConfig = {
  environment: 'development',
  api: 'http://localhost:3001',
};

const PRODUCTION_CONFIG: EnvironmentConfig = {
  environment: 'production',
  api: 'https://api.photography.beiron.se',
};

const host = window.location.hostname;
const isDevelopment = host.includes('localhost');

export const Config: EnvironmentConfig = isDevelopment
  ? DEVELOPMENT_CONFIG
  : PRODUCTION_CONFIG;
