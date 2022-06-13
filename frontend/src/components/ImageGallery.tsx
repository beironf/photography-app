import React from 'react';
import { ImageApi } from 'api/ImageApi';
import Gallery, { RenderImageProps } from 'react-photo-gallery';
import { ImageDto } from 'api/ImageApi/model';

type props = {
  images: ImageDto[];
  onImageClick?: (_: string) => void; // will not work if custom renderImage is used
  renderImage?: React.ComponentType<RenderImageProps<{}>>;
};

export const ImageGallery: React.FunctionComponent<props> = ({
  images,
  onImageClick,
  renderImage,
}) => (
  <Gallery
    photos={Object.values(images).map(({ id, width, height }) => ({
      key: id,
      src: ImageApi.ThumbnailRoute.getThumbnailUrl(id),
      width,
      height,
      alt: id,
    }))}
    renderImage={renderImage}
    onClick={onImageClick !== undefined
      ? (_, { photo: { key } }) => onImageClick(key)
      : undefined}
  />
);
