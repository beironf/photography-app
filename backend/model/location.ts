import { Region, countries, regions } from "typed-countries";
import { Schema } from "mongoose";

function countryValidator(countryName: string) {
  const country = countries.find((c) => c.name === countryName);

  return (country) ? this.region === country.region : false;
};

type Coordinates = {
  longitude: number; // [-180, 180]
  latitude: number;  // [-90, 90]
};

const CoordinatesSchema = new Schema({
  longitude: { type: Number, min: -180, max: 180, required: true },
  latitude: { type: Number, min: -90, max: 90, required: true }
});

export type Location = {
  name: string;
  region: Region;
  country: string;
  coordinates: Coordinates;
};

export const LocationSchema = new Schema({
  name: { type: String, required: true },
  region: { type: String, enum: regions, required: true },
  country: { type: String, required: true, validate: [countryValidator, "Bad country name or country doesn't belong to this region!"] },
  coordinates: { type: CoordinatesSchema, required: true },
});
