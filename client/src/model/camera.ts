export enum Camera {
  CANON_EOS_5D_MARK_II = 'Canon EOS 5D Mark II',
  CANON_EOS_600D = 'Canon EOS 600D',
}

export enum Lens {
  CANON_24_70_L = 'EF24-70mm f/2.8L USM',
  CANON_50 = 'EF50mm f/1.8 II',
  CANON_70_300 = 'EF70-300mm f/4-5.6 IS USM',
  SIGMA_10_20_EX_DC_HSM = 'Sigma 10-20mm f/4-5.6 EX DC HSM',
}

export type CameraGear = {
  camera: Camera;
  lens: Lens;
};

export type CameraSettings = {
  focalLenght: number;
  fNumber: number;
  iso: number;
  exposureTime: string; // ex: 1/50
};
