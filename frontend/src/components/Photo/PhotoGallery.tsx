import React from 'react';
import { ImageApi } from 'api/ImageApi';
import Gallery, { RenderImageProps } from 'react-photo-gallery';
import { PhotoWithRatio } from 'model/photo';

type props = {
  photosWithRatio: PhotoWithRatio[];
  sideMargin?: number;
  bottomMargin?: number;
  targetRowHeight?: number; // Select either row height or columns
  columns?: number; // Select either row height or columns
  onPhotoClick?: (_: string) => void; // will not work if custom renderImage is used
  renderPhoto?: React.ComponentType<RenderImageProps<{}>>;
};

export const PhotoGallery: React.FunctionComponent<props> = ({
  photosWithRatio,
  sideMargin,
  bottomMargin,
  targetRowHeight,
  columns,
  onPhotoClick,
  renderPhoto,
}) => (
  <div style={{ padding: `0 ${sideMargin ?? 0}px ${bottomMargin ?? 0}px` }}>
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
      columns={columns}
      direction={columns !== undefined ? 'column' : 'row'}
    />
  </div>
);
