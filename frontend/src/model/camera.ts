/* eslint-disable no-shadow */
/* eslint-disable no-unused-vars */

export enum Camera {
  CANON_EOS_5D_MARK_II = 'Canon EOS 5D Mark II',
  CANON_EOS_600D = 'Canon EOS 600D',
  FUJI_X100F = 'Fujifilm X100F',
}

export const toCamera = (s?: string): Camera | undefined => {
  if (s === undefined) {
    return undefined;
  } if (s.includes('EOS 5D Mark II')) {
    return Camera.CANON_EOS_5D_MARK_II;
  } if (s.includes('EOS 600D')) {
    return Camera.CANON_EOS_600D;
  } if (s.includes('X100F')) {
    return Camera.FUJI_X100F;
  }
  return undefined;
};

export enum Lens {
  CANON_24_70_L = 'EF24-70mm f/2.8L USM',
  CANON_50 = 'EF50mm f/1.8 II',
  CANON_70_300 = 'EF70-300mm f/4-5.6 IS USM',
  SIGMA_10_20_EX_DC_HSM = 'Sigma 10-20mm f/4-5.6 EX DC HSM',
  FUJINON_35 = 'Fujinon 35mm f:2',
}

export const toLens = (s?: string, isX100F: boolean = false): Lens | undefined => {
  if (isX100F) {
    return Lens.FUJINON_35;
  } if (s === undefined) {
    return undefined;
  } if (s.includes('EF24-70mm f/2.8L USM')) {
    return Lens.CANON_24_70_L;
  } if (s.includes('EF50mm f/1.8 II')) {
    return Lens.CANON_50;
  } if (s.includes('10-20mm')) {
    return Lens.SIGMA_10_20_EX_DC_HSM;
  } if (s.includes('EF70-300mm f/4-5.6 IS USM')) {
    return Lens.CANON_70_300;
  }
  return undefined;
};

export type CameraGear = {
  camera: Camera;
  lens: Lens;
};

export type CameraSettings = {
  focalLength: string; // ex: '24mm'
  aperture: string; // ex: 'f/5.6'
  iso: string; // ex: 'ISO 100'
  exposureTime: string; // ex: '1/50s'
};
