import { Region } from "typed-countries";

export type Coordinates = {
  longitude: number; // [-180, 180]
  latitude: number;  // [-90, 90]
};

export type Location = {
  name: string;
  region: Region;
  country: string;
  coordinates: Coordinates;
};
