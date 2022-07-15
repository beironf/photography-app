import React from 'react';
import { ImageApi } from 'api/ImageApi';
import Gallery, { RenderImageProps } from 'react-photo-gallery';
import { PhotoWithRatio } from 'model/photo';

type props = {
  photosWithRatio: PhotoWithRatio[];
  margin: number;
  targetRowHeight?: number;
  onPhotoClick?: (_: string) => void; // will not work if custom renderImage is used
  renderPhoto?: React.ComponentType<RenderImageProps<{}>>;
};

export const PhotoGallery: React.FunctionComponent<props> = ({
  photosWithRatio,
  margin,
  targetRowHeight,
  onPhotoClick,
  renderPhoto,
}) => (
  <div style={{ minHeight: '100vh', padding: `${margin}px` }}>
    <Gallery
      photos={Object.values(photosWithRatio)
        .map(({
          photo: { title, imageId }, width, height,
        }) => ({
          key: imageId,
          src: ImageApi.ThumbnailRoute.getThumbnailUrl(imageId),
          width,
          height,
          alt: title,
        }))}
      renderImage={renderPhoto}
      onClick={onPhotoClick !== undefined
        ? (_, { photo: { key } }) => onPhotoClick(key)
        : undefined}
      targetRowHeight={targetRowHeight}
    />
  </div>
);
