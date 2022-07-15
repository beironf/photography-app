import { Button, Typography } from '@mui/material';
import React, { useState } from 'react';
import { PhotoProps } from 'react-photo-gallery';

type props = {
  selected: boolean;
  noImageSelected: boolean;
  image: PhotoProps<{}>;
  onImageClick: () => void;
  child?: JSX.Element;
  selectionColor?: string;
};

export const ImageRenderer: React.FunctionComponent<props> = ({
  selected,
  noImageSelected,
  image,
  onImageClick,
  child,
  selectionColor,
}) => {
  const [isHovering, setIsHovering] = useState(false);

  const opacity = selected || noImageSelected ? 1 : 0.4;
  const border = selected ? `2px solid ${selectionColor ?? 'white'}` : undefined;

  return (
    <Button
      style={{ margin: '2px', padding: 0, position: 'relative' }}
      onClick={onImageClick}
      onMouseEnter={() => setIsHovering(true)}
      onMouseLeave={() => setIsHovering(false)}
    >
      {child !== undefined && child}
      {isHovering && image.alt && (
        <div
          style={{
            position: 'absolute',
            width: '100%',
            color: 'white',
            backgroundColor: 'rgba(0,0,0,0.5)',
            bottom: 0,
          }}
        >
          <Typography variant="caption" textOverflow="ellipsis" width="100%">
            {image.alt}
          </Typography>
        </div>
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
