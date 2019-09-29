const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const TypedCountries = require('typed-countries');

function countryValidator(countryName) {
  const country = TypedCountries.countries.find((c) => c.name === countryName);

  return (country) ? this.region === country.region : false;
};

const CoordinatesSchema = new Schema({
  longitude: { type: Number, min: -180, max: 180, required: true },
  latitude: { type: Number, min: -90, max: 90, required: true }
});

const LocationSchema = new Schema({
  name: { type: String, required: true },
  region: { type: String, enum: TypedCountries.regions, required: true },
  country: { type: String, required: true, validate: [countryValidator, "Bad country name or country doesn't belong to this region!"] },
  coordinates: { type: CoordinatesSchema, required: true },
});

module.exports = LocationSchema;
