import React, { useState } from 'react';
import {
  FormControl, FormGroup, Form, Button,
} from 'react-bootstrap';
import { ImageApi } from 'api/ImageApi';

type props = {
  onImageUploaded: (_: string) => void;
};

export const ImageUploader: React.FunctionComponent<props> = ({
  onImageUploaded,
}) => {
  const [fileName, setFileName] = useState('');

  const onSubmit = (event: React.FormEvent): void => {
    event.preventDefault();
    const formData = new FormData();
    const fileInput = document.querySelector('#file-input') as any;
    if (fileInput && fileInput.files[0]) {
      const file = fileInput.files[0];
      formData.append('image', file);
      ImageApi.ImageRoute.uploadImage(formData, () => onImageUploaded(file.name));
    }
  };

  return (
    <Form onSubmit={onSubmit}>
      <FormGroup>
        <FormControl
          id="file-input"
          type="file"
          accept="image/jpeg"
          onChange={(e: any) => {
            setFileName(e.target.files[0] ? e.target.files[0].name : '');
          }}
        />
      </FormGroup>
      {fileName !== '' && (
        <Button variant="primary" type="submit">
          Upload
        </Button>
      )}
    </Form>
  );
};
