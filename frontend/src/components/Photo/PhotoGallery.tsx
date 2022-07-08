import React from 'react';
import { ImageApi } from 'api/ImageApi';
import Gallery from 'react-photo-gallery';
import { PhotoWithRatio } from 'model/photo';

type props = {
  photosWithRatio: PhotoWithRatio[];
  margin: number;
  onPhotoClick?: (_: string) => void;
};

export const PhotoGallery: React.FunctionComponent<props> = ({
  photosWithRatio,
  margin,
  onPhotoClick,
}) => (
  <div style={{ minHeight: '100vh', padding: `${margin}px` }}>
    <Gallery
      photos={Object.values(photosWithRatio).map(({
        photo: { title, imageId }, width, height,
      }) => ({
        key: imageId,
        src: ImageApi.ThumbnailRoute.getThumbnailUrl(imageId),
        width,
        height,
        alt: title,
      }))}
      onClick={onPhotoClick !== undefined
        ? (_, { photo: { key } }) => onPhotoClick(key)
        : undefined}
    />
  </div>
);
