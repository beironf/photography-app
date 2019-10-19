const mongoose = require('mongoose');
const express = require('express');
var cors = require('cors');
const bodyParser = require('body-parser');
const logger = require('morgan');
const Photo = require('./schemas/photo');
const formidable = require('formidable')
const fs = require('fs');
const path = require("path");
const imageThumbnail = require('image-thumbnail');
const imageSize = require('image-size');

const API_PORT = 3001;
const app = express();
app.use(cors());
const router = express.Router();

const dbRoute = 'mongodb://127.0.0.1:27017/photography-app';

mongoose.connect(dbRoute, { useNewUrlParser: true });

let db = mongoose.connection;

db.once('open', () => console.log('connected to the database'));
db.on('error', console.error.bind(console, 'MongoDB connection error:'));

// (optional) only made for logging and
// bodyParser, parses the request body to be a readable json format
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
app.use(logger('dev'));

/**
 * /api/photos - Get all photos
 */
router.get('/photos', (req, res) => {
  Photo.find((err, photos) => {
    if (err) return res.json({ success: false, error: err });
    return res.json({ success: true, photos: photos });
  });
});

/**
 * /api/photos/update - Update existing photo
 */
router.post('/photos/update', (req, res) => {
  const { id, update } = req.body;
  Photo.findByIdAndUpdate(id, update, (err) => {
    if (err) return res.json({ success: false, error: err });
    return res.json({ success: true });
  });
});

/**
 * /api/photos/remove - Remove photo
 */
router.delete('/photos/remove', (req, res) => {
  const { id } = req.body;
  Photo.findByIdAndRemove(id, (err) => {
    if (err) return res.send(err);
    return res.json({ success: true });
  });
});

/**
 * /api/photos/add - Add new photo
 */
router.post('/photos/add', (req, res) => {
  const { id, photo } = req.body;

  const newPhoto = new Photo(photo);
  newPhoto.save((err) => {
    if (err) return res.json({ success: false, error: err });
    return res.json({ success: true });
  });
});

/**
 * /api/images - Get image
 */
router.get('/images', (req, res) => {
  const pathToImage = path.join(__dirname, "/../storage/images/", req.query.filename);
  res.sendFile(pathToImage);
});

/**
 * /api/thumbnails - Get thumbnail
 */
router.get('/thumbnails', (req, res) => {
  const pathToImage = path.join(__dirname, "/../storage/thumbnails/", req.query.filename);
  res.sendFile(pathToImage);
});

/**
 * /api/images/upload - Upload new image and create a thumbnail
 */
router.post('/images/upload', (req, res) => {
  new formidable.IncomingForm()
    .on('fileBegin', (name, file) => {
      file.path = __dirname + '/../storage/images/' + file.name
    })
    .on('file', function (name, file) {
      console.log('Uploaded ' + file.name);
    })
    .on('end', () => {
      res.json({ success: true });
    })
    .parse(req, (error, fields, files) => {
      // Create a thumbnail
      const size = imageSize(files.image.path);
      const options = { percentage: 600 / size.height * 100 };
      
      imageThumbnail(files.image.path, options)
        .then(thumbnail => { 
          fs.writeFile(__dirname + '/../storage/thumbnails/' + files.image.name, thumbnail, (err) => { 
            if (err) {
              console.log("error:", err);
            }
          });
        })
        .catch(err => console.error(err));
    });
});

// append /api for our http requests
app.use('/api', router);

// launch our backend into a port
console.log(__dirname);
app.listen(API_PORT, () => console.log(`LISTENING ON PORT ${API_PORT}`));