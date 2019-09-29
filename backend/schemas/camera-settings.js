const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const CameraSettingsSchema = new Schema({
  focalLenght: { type: Number, required: true },
  fNumber: { type: Number, required: true },
  iso: { type: Number, required: true },
  exposureTime: { type: String, required: true },
});

module.exports = CameraSettingsSchema;