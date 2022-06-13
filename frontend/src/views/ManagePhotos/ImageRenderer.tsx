import { Button } from '@mui/material';
import React from 'react';
import { PhotoProps } from 'react-photo-gallery';

type props = {
  selectedImageId?: string;
  photo: PhotoProps<{}>;
  margin: number;
  selectionColor: string;
  opacityWhenNotSelected: number;
  onImageClick: () => void;
};

export const ImageRenderer: React.FunctionComponent<props> = ({
  selectedImageId,
  photo,
  margin,
  selectionColor,
  opacityWhenNotSelected,
  onImageClick,
}) => {
  const selected = selectedImageId === photo.key;
  const noImageSelected = selectedImageId === undefined;

  const opacity = selected || noImageSelected ? 1 : opacityWhenNotSelected;
  const border = selected ? `${margin}px solid ${selectionColor}` : undefined;

  return (
    <Button style={{ margin, padding: 0 }} onClick={onImageClick}>
      <img
        key={photo.key}
        src={photo.src}
        width={photo.width}
        height={photo.height}
        style={{ opacity, border }}
        alt={photo.key}
      />
    </Button>
  );
};
