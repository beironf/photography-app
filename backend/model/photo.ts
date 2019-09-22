import { Document, Schema, model } from "mongoose";
import { Location, LocationSchema } from "./location";
import { CameraGear, CameraGearSchema, CameraSettings, CameraSettingsSchema } from "./camera";
import { Metadata, MetadataSchema } from "./metadata";

type IPhoto = Document & {
  imageId: string;
  photographer: string;
  title: string;
  description?: string;
  taken: any; // Timestamp
  location: Location;
  gear: CameraGear;
  cameraSettings: CameraSettings;
  metadata: Metadata;
};

const PhotoSchema = new Schema({
  imageId: { type: String, required: true, unique: true },
  photographer: { type: String, required: true },
  title: { type: String, required: true },
  description: String,
  taken: { type: Date, required: true },
  location: { type: LocationSchema, required: true },
  gear: { type: CameraGearSchema, required: true },
  cameraSettings: { type: CameraSettingsSchema, required: true },
  metadata: { type: MetadataSchema, required: true },
});


export const Photo = model<IPhoto>("Metadata", PhotoSchema);
