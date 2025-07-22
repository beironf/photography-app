/* eslint-disable no-shadow */
/* eslint-disable no-unused-vars */

export enum Camera {
  CANON_EOS_5D_MARK_II = 'Canon EOS 5D Mark II',
  CANON_EOS_600D = 'Canon EOS 600D',
  FUJI_X100F = 'Fujifilm X100F',
  DJI_MINI_3 = 'DJI Mini 3',
  FUJI_X_T4 = 'Fujifilm X-T4',
}

export const toCamera = (s?: string): Camera | undefined => {
  if (s === undefined) {
    return undefined;
  }
  if (s.includes('EOS 5D Mark II')) {
    return Camera.CANON_EOS_5D_MARK_II;
  }
  if (s.includes('EOS 600D')) {
    return Camera.CANON_EOS_600D;
  }
  if (s.includes('X100F')) {
    return Camera.FUJI_X100F;
  }
  if (s.includes('FC3582')) {
    return Camera.DJI_MINI_3;
  }
  if (s.includes('X-T4')) {
    return Camera.FUJI_X_T4;
  }
  return undefined;
};

export enum Lens {
  CANON_24_70_L = 'EF24-70mm f/2.8L USM',
  CANON_50 = 'EF50mm f/1.8 II',
  CANON_70_300 = 'EF70-300mm f/4-5.6 IS USM',
  SIGMA_10_20_EX_DC_HSM = 'Sigma 10-20mm f/4-5.6 EX DC HSM',
  FUJINON_35 = 'Fujinon 35mm f:2',
  DJI_MINI_3 = 'DJI 6.7mm f/1.7',
  FUJINON_16 = 'Fujinon 16mm f/1.4 R WR',
  FUJINON_33 = 'Fujinon 33mm f/1.4 R LM WR',
  FUJINON_90 = 'Fujinon 90mm f/2 R LM WR',
}

export const toLens = (
  s?: string,
  isX100F: boolean = false,
  isDjiMini3: boolean = false,
): Lens | undefined => {
  if (isX100F) {
    return Lens.FUJINON_35;
  }
  if (isDjiMini3) {
    return Lens.DJI_MINI_3;
  }
  if (s === undefined) {
    return undefined;
  }
  if (s.includes('EF24-70mm f/2.8L USM')) {
    return Lens.CANON_24_70_L;
  }
  if (s.includes('EF50mm f/1.8 II')) {
    return Lens.CANON_50;
  }
  if (s.includes('10-20mm')) {
    return Lens.SIGMA_10_20_EX_DC_HSM;
  }
  if (s.includes('EF70-300mm f/4-5.6 IS USM')) {
    return Lens.CANON_70_300;
  }
  if (s.includes('XF16mmF1.4 R WR')) {
    return Lens.FUJINON_16;
  }
  if (s.includes('XF33mmF1.4 R LM WR')) {
    return Lens.FUJINON_33;
  }
  if (s.includes('XF90mmF2 R LM WR')) {
    return Lens.FUJINON_90;
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
