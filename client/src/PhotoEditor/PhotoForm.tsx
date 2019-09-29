import React, {useState} from "react";
import axios from "axios";
import { Form, Button, Image } from "react-bootstrap";
import { Photo } from "../model/photo";  
import { ImageUploader } from "./ImageUploader";
const getExif = require("./getExif");

const PATH_TO_IMAGE_STORAGE = "../../storage/images/";

type Props = {
  onSubmit: () => void;
};

export const PhotoForm: React.FunctionComponent<Props> = ({ onSubmit }) => {
  const [validated, setValidated] = useState(false);
  const [image, setImage] = useState<string>();
  const [photo, setPhoto] = useState<Photo>();

  const handleSubmit = (event: any) => {
    event.preventDefault();
    event.stopPropagation();
    
    const form = event.currentTarget;
    if (form.checkValidity()) {
      onSubmit();
    }

    setValidated(true);
  };

  const onImageUploaded = (imageName: string) => {
    setImage(PATH_TO_IMAGE_STORAGE + imageName);
  };

  if (image === undefined) {
    return <ImageUploader onImageUploaded={onImageUploaded}/>;
  }

  return (
    <div>
      <Image src={image}/>
      <Form noValidate validated={validated} onSubmit={handleSubmit}>
        <Form.Group controlId="formBasicEmail">
          <Form.Label>Email address</Form.Label>
          <Form.Control type="email" placeholder="Enter email" required />
          <Form.Text className="text-muted">
            We'll never share your email with anyone else.
          </Form.Text>
        </Form.Group>

        <Form.Group controlId="formBasicPassword">
          <Form.Label>Password</Form.Label>
          <Form.Control type="password" placeholder="Password" />
        </Form.Group>
        <Form.Group controlId="formBasicCheckbox">
          <Form.Check type="checkbox" label="Check me out" />
        </Form.Group>
        <Button variant="primary" type="submit">
          Submit
        </Button>
      </Form>
    </div>
  );
};
