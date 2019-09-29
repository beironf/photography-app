const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const LocationSchema = require('./location');
const CameraGearSchema = require('./camera-gear');
const CameraSettingsSchema = require('./camera-settings');
const MetadataSchema = require('./metadata');

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

module.exports = mongoose.model('Photo', PhotoSchema);
