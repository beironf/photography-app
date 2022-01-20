import React, { useState } from 'react';
import { Photo } from 'model/photo';

type props = {
  onPhotoSelected: (filename: string) => void;
};

export const PhotoSelector: React.FunctionComponent<props> = ({
  onPhotoSelected,
}) => {
  const [photos, setPhotos] = useState<Photo>();

  if (!photos) {
    return <h3>No photos found..</h3>;
  }

  return <div />;
};
