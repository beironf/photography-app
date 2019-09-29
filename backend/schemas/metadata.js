const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const categories = [
  'Abstract',
  'Animal',
  'City & Architecture',
  'Landscape',
  'Nature',
  'Night',
  'People',
];

const cameraTechniques = [
  'Long Exposure',
  'Panorama',
  'Aerial',
  'Macro',
  'Zooming',
  'Filters',
  'Multiple Focus Points',
];

const PersonSchema = new Schema({
  firstName: { type: String, required: true },
  lastName: { type: String, required: true },
});

const MetadataSchema = new Schema({
  category: { type: String, enum: categories, required: true },
  peoples: [PersonSchema],
  cameraTechnique: { type: String, enum: cameraTechniques },
  tags: [String],
});

module.exports = MetadataSchema;
