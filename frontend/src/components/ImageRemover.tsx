import React from 'react';
import DeleteIcon from '@mui/icons-material/Delete';
import { Button } from '@mui/material';
import { ImageApi } from 'api/ImageApi';

type props = {
  imageId: string;
  onImageRemoved: (_: string) => void;
};

export const ImageRemover: React.FunctionComponent<props> = ({ imageId, onImageRemoved }) => {
  const removeImage = (): void => {
    ImageApi.ImageRoute.removeImage(imageId)
      .then((_: any) => {
        onImageRemoved(imageId);
      });
  };

  return (
    <Button
      startIcon={<DeleteIcon />}
      variant="contained"
      component="span"
      onClick={removeImage}
    >
      Remove
    </Button>
  );
};
