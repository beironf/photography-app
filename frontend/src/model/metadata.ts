/* eslint-disable no-shadow */
/* eslint-disable no-unused-vars */

export enum PhotoCategory {
  ABSTRACT = 'Abstract',
  ANIMAL = 'Animal',
  CITY_AND_ARCHITECTURE = 'City & Architecture',
  LANDSCAPE = 'Landscape',
  NATURE = 'Nature',
  NIGHT = 'Night',
  PEOPLE = 'People',
}

export const toCategory = (s?: string): PhotoCategory | undefined => {
  if (s === undefined) {
    return undefined;
  }
  if (s.includes('Abstract')) {
    return PhotoCategory.ABSTRACT;
  }
  if (s.includes('Animal')) {
    return PhotoCategory.ANIMAL;
  }
  if (s.includes('City & Architecture')) {
    return PhotoCategory.CITY_AND_ARCHITECTURE;
  }
  if (s.includes('Landscape')) {
    return PhotoCategory.LANDSCAPE;
  }
  if (s.includes('Nature')) {
    return PhotoCategory.NATURE;
  }
  if (s.includes('Night')) {
    return PhotoCategory.NIGHT;
  }
  if (s.includes('People')) {
    return PhotoCategory.PEOPLE;
  }
  return undefined;
};

export enum CameraTechnique {
  LONG_EXPOSURE = 'Long Exposure',
  PANORAMA = 'Panorama',
  AERIAL = 'Aerial',
  MACRO = 'Macro',
  ZOOMING = 'Zooming',
  FILTERS = 'Filters',
  MULTIPLE_FOCUS_POINTS = 'Multiple Focus Points',
}

export const toCameraTechnique = (s?: string): CameraTechnique | undefined => {
  if (s === undefined) {
    return undefined;
  }
  if (s.includes('Long Exposure')) {
    return CameraTechnique.LONG_EXPOSURE;
  }
  if (s.includes('Panorama')) {
    return CameraTechnique.PANORAMA;
  }
  if (s.includes('Aerial')) {
    return CameraTechnique.AERIAL;
  }
  if (s.includes('Macro')) {
    return CameraTechnique.MACRO;
  }
  if (s.includes('Zooming')) {
    return CameraTechnique.ZOOMING;
  }
  if (s.includes('Filters')) {
    return CameraTechnique.FILTERS;
  }
  if (s.includes('Multiple Focus Points')) {
    return CameraTechnique.MULTIPLE_FOCUS_POINTS;
  }
  return undefined;
};

export type Metadata = {
  category: PhotoCategory;
  cameraTechniques?: CameraTechnique[];
  tags?: string[];
};
