const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const cameras = [
  'Canon EOS 600D',
  'Canon EOS 5D Mark II',
];

const lenses = [
  'Sigma 10-20mm f/4-5.6 EX DC HSM',
  'Canon 50mm f:1.8',
  'Canon 70-300mm f:4-5.6',
  'Canon 24-70mm 2.8 L',
];

const CameraGearSchema = new Schema({
  camera: { type: String, enum: cameras, required: true },
  lens: { type: String, enum: lenses, required: true },
});

module.exports = CameraGearSchema;
