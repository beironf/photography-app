/* eslint-disable jsx-a11y/label-has-associated-control */
import React, { useEffect, useState } from 'react';
import { ImageApi } from 'api/ImageApi';
import ImageIcon from '@mui/icons-material/Image';
import {
  Button, Input,
} from '@mui/material';
import { theme } from 'style/theme';

type props = {
  onImageUploading: () => void;
  onImageUploaded: (_: string) => void;
};

export const ImageUploader: React.FunctionComponent<props> = ({
  onImageUploading,
  onImageUploaded,
}) => {
  const [fileName, setFileName] = useState('');

  const password = 'password'; // TODO: set password in pop-up and local storage

  useEffect(() => {
    const formData = new FormData();
    const fileInput = document.querySelector('#file-input') as any;
    if (fileInput && fileInput.files[0]) {
      const file = fileInput.files[0];
      formData.append('image', file);
      ImageApi.ImageRoute.uploadImage(formData, password, () => onImageUploaded(file.name));
      onImageUploading();
    }
  }, [fileName, onImageUploaded, onImageUploading]);

  return (
    <label htmlFor="file-input">
      <Input
        id="file-input"
        type="file"
        inputProps={{ accept: 'image/jpeg' }}
        onChange={(e: any) => {
          setFileName(e.target.files[0] ? e.target.files[0].name : '');
        }}
        hidden
        sx={{ margin: theme.primaryPadding }}
      />
      <Button startIcon={<ImageIcon />} variant="contained" component="span">
        Upload Image
      </Button>
    </label>
  );
};
