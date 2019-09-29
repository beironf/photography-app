const EXIF = require('exif-js');

const getExif = (url) => {
  console.log(url);
  EXIF.getData(url, () => {
    const make = EXIF.getTag(this, "Make");
    console.log(make);
  });
};

module.exports = getExif;