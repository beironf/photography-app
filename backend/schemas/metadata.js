const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const PersonSchema = require('./person');

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

const MetadataSchema = new Schema({
  category: { type: String, enum: categories, required: true },
  peoples: [PersonSchema],
  cameraTechnique: { type: String, enum: cameraTechniques },
  tags: [String],
});

module.exports = MetadataSchema;
