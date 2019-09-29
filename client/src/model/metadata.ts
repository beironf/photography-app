enum PhotoCategory {
  ABSTRACT = "Abstract",
  ANIMAL = "Animal",
  CITY_AND_ARCHITECTURE = "City & Architecture",
  LANDSCAPE = "Landscape",
  NATURE = "Nature",
  NIGHT = "Night",
  PEOPLE = "People",
}

enum CameraTechnique {
  LONG_EXPOSURE = "Long Exposure",
  PANORAMA = "Panorama",
  AERIAL = "Aerial",
  MACRO = "Macro",
  ZOOMING = "Zooming",
  FILTERS = "Filters",
  MULTIPLE_FOCUS_POINTS = "Multiple Focus Points",
}

type Person = {
  firstName: string;
  lastName: string;
};

export type Metadata = {
  category: PhotoCategory;
  peoples?: Person[];
  cameraTechnique?: CameraTechnique;
  tags?: string[];
};
