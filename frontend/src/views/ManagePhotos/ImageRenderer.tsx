import { Button } from '@mui/material';
import FlagIcon from '@mui/icons-material/Flag';
import React from 'react';
import { PhotoProps } from 'react-photo-gallery';

type props = {
  selectedImageId?: string;
  image: PhotoProps<{}>;
  margin: number;
  selectionColor: string;
  opacityWhenNotSelected: number;
  onImageClick: () => void;
  unfinished?: boolean;
};

export const ImageRenderer: React.FunctionComponent<props> = ({
  selectedImageId,
  image,
  margin,
  selectionColor,
  opacityWhenNotSelected,
  onImageClick,
  unfinished,
}) => {
  const selected = selectedImageId === image.key;
  const noImageSelected = selectedImageId === undefined;

  const opacity = selected || noImageSelected ? 1 : opacityWhenNotSelected;
  const border = selected ? `${margin}px solid ${selectionColor}` : undefined;

  return (
    <Button style={{ margin, padding: 0, position: 'relative' }} onClick={onImageClick}>
      {(unfinished ?? false) && (
        <FlagIcon
          style={{
            color: 'red', position: 'absolute', top: 0, right: 0,
          }}
        />
      )}
      <img
        key={image.key}
        src={image.src}
        width={image.width}
        height={image.height}
        style={{ opacity, border }}
        alt={image.key}
      />
    </Button>
  );
};
