import React, {useState} from "react";
import axios from "axios";
import { Form, Button, Image, Row, Col } from "react-bootstrap";
import { ImageUploader } from "./ImageUploader";
import { PhotoSelector } from "./PhotoSelector";
import { getExif, PhotoExif } from "./getExif";

import { Photo } from "../model/photo";
import { PhotoCategory } from "../model/metadata";
import { Camera, Lens } from "../model/camera";


const PATH_TO_GET_IMAGE = "http://localhost:3001/api/images?filename=";
const PATH_TO_GET_THUMBNAIL = "http://localhost:3001/api/thumbnails?filename=";

const initialExif = {
  cameraGear: {}, 
  cameraSettings: {
    focalLenght: NaN,
    fNumber: NaN,
    iso: NaN,
    exposureTime: "",
  },
};

type Props = {
  onSubmit: () => void;
};

export const PhotoForm: React.FunctionComponent<Props> = ({ onSubmit }) => {
  const [validated, setValidated] = useState(false);
  const [imageFilename, setImageFilename] = useState<string>("IMG_4459.jpg");
  const [photo, setPhoto] = useState<Photo>();
  const [exif, setExif] = useState<PhotoExif>(initialExif);

  const handleSubmit = (event: any) => {
    event.preventDefault();
    event.stopPropagation();
    
    const form = event.currentTarget;
    if (form.checkValidity()) {
      onSubmit();
    }

    setValidated(true);
  };

  const onImageUploaded = (filename: string) => {
    setImageFilename(filename);
  };

  const onPhotoSelected = (filename: string) => {
    setImageFilename(filename);
  };

  const loadExif = (event: any) => {
    getExif(event.currentTarget, setExif);
  };

  if (imageFilename === undefined) {
    return (
      <div>
        <ImageUploader onImageUploaded={onImageUploaded}/>
        <PhotoSelector onPhotoSelected={onPhotoSelected}/>
      </div>
    );
  }

  return (
    <div>
      <Image style={{height: 600}} id="image" src={PATH_TO_GET_IMAGE + imageFilename} onLoad={loadExif}/>
      <Form noValidate validated={validated} onSubmit={handleSubmit}>
        <Form.Group as={Row}>
          <Form.Label column sm="2">Filename</Form.Label>
          <Col sm="10">
            <Form.Control type="text" plaintext readOnly placeholder={imageFilename} required />
          </Col>
        </Form.Group>

        <Form.Group as={Row}>
          <Form.Label column sm="2">Camera</Form.Label>
          <Col sm="10">
            <Form.Control 
              as="select" 
              required
              readOnly={exif.cameraGear.camera != null} 
            >
              {exif.cameraGear.camera != null && (
                <option key={"camera-" + exif.cameraGear.camera}>
                  {exif.cameraGear.camera}
                </option>
              )}
              {exif.cameraGear.camera == null && Object.values(Object.values(Camera)).map((camera) => {
                return <option key={"camera-" + camera}>{camera}</option>;
              })}
            </Form.Control>
          </Col>
        </Form.Group>

        <Form.Group as={Row}>
          <Form.Label column sm="2">Lens</Form.Label>
          <Col sm="10">
            <Form.Control 
              as="select" 
              required
              readOnly={exif.cameraGear.lens != null} 
            >
              {exif.cameraGear.lens != null && (
                <option key={"lens-" + exif.cameraGear.lens}>
                  {exif.cameraGear.lens}
                </option>
              )}
              {exif.cameraGear.lens == null && Object.values(Object.values(Lens)).map((lens) => {
                return <option key={"lens-" + lens}>{lens}</option>;
              })}
            </Form.Control>
          </Col>
        </Form.Group>

        <Form.Group as={Row}>
          <Form.Label column sm="2">Exposure Time</Form.Label>
          <Col sm="10">
            <Form.Control 
              type="text" 
              plaintext={exif.cameraSettings.exposureTime !== ""}
              readOnly={exif.cameraSettings.exposureTime !== ""}
              placeholder={exif.cameraSettings.exposureTime}
              required
            />
          </Col>
        </Form.Group>

        <Form.Group>
          <Form.Label>Title</Form.Label>
          <Form.Control type="text" placeholder="Title of Photo" required />
        </Form.Group>

        <Form.Group>
          <Form.Label>Category</Form.Label>
          <Form.Control as="select" required>
            {Object.values(PhotoCategory).map((category) => {
              return <option key={"category-" + category}>{category}</option>;
            })}
          </Form.Control>
        </Form.Group>

        <Button variant="primary" type="submit">
          Submit
        </Button>
      </Form>
    </div>
  );
};
