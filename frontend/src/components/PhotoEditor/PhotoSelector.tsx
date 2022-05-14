import React, { useState } from 'react';
import { Photo } from 'model/photo';

type props = {
  onPhotoSelected: (_: string) => void;
};

export const PhotoSelector: React.FunctionComponent<props> = () => {
  const [photos] = useState<Photo>();

  if (!photos) {
    return <h3>No photos found..</h3>;
  }

  return <div />;
};
