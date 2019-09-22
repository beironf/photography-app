import { Schema } from "mongoose";

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

const PersonSchema = new Schema({
  firstName: { type: String, required: true },
  lastName: { type: String, required: true },
});

export type Metadata = {
  category: PhotoCategory;
  peoples?: Person[];
  cameraTechnique?: CameraTechnique;
  tags?: string[];
};

export const MetadataSchema = new Schema({
  category: { type: String, enum: Object.values(PhotoCategory), required: true },
  peoples: [PersonSchema],
  cameraTechnique: { type: String, enum: Object.values(CameraTechnique) },
  tags: [String],
});
