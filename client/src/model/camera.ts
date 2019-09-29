enum Camera {
  CANON_EOS_600D = "Canon EOS 600D",
  CANON_EOS_5D_MARK_II = "Canon EOS 5D Mark II",
}

enum Lens {
  SIGMA_10_20_EX_DC_HSM = "Sigma 10-20mm f/4-5.6 EX DC HSM",
  CANON_50 = "Canon 50mm f:1.8",
  CANON_70_300 = "Canon 70-300mm f:4-5.6",
  CANON_24_70_L = "Canon 24-70mm 2.8 L",
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
