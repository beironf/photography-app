import React, {
  useCallback, useRef, useMemo, useState,
} from 'react';
import { PhotoGallery } from 'components/Photo/PhotoGallery';
import { PhotoViewer } from 'components/Photo/PhotoViewer';
import { useNavigate, useParams } from 'react-router-dom';
import { ARROW_LEFT, ARROW_RIGHT, useKeyPress } from 'hooks/use-key-press';
import { nextIndex, prevIndex } from 'util/carousel-utils';
import { PhotoWithRatio } from 'model/photo';

type Props = {
  photosWithRatio: PhotoWithRatio[];
  urlPath: string;
  sideMargin?: number;
  bottomMargin?: number;
  targetRowHeight?: number; // Select either row height or columns
  columns?: number; // Select either row height or columns
};

export const PhotoGalleryWithViewer: React.FunctionComponent<Props> = ({
  photosWithRatio,
  urlPath,
  sideMargin,
  bottomMargin,
  targetRowHeight,
  columns,
}) => {
  const ref = useRef();
  const params = useParams();
  const navigate = useNavigate();

  const [imageIsLoading, setImageIsLoading] = useState(false);

  const { imageId } = params;
  const photos = useMemo(() => (photosWithRatio ?? []).map((_) => _.photo), [photosWithRatio]);

  const selectedIndex = photos.findIndex((_) => _.imageId === imageId);
  const selectedPhoto = selectedIndex !== -1 ? photos[selectedIndex] : undefined;

  const setImageId = useCallback((id?: string) => {
    if (id !== undefined) {
      navigate(`${urlPath}/${id}`);
      setImageIsLoading(true);
    } else navigate(`${urlPath}`);
  }, [navigate, urlPath]);

  const goTo = useCallback((direction: 'next' | 'prev') => {
    if (!imageIsLoading) {
      const newIndex = direction === 'next'
        ? nextIndex(selectedIndex, photos.length)
        : prevIndex(selectedIndex, photos.length);
      if (newIndex === -1) setImageId(undefined);
      else setImageId(photos[newIndex].imageId);
    }
  }, [photos, selectedIndex, imageIsLoading, setImageId]);

  useKeyPress([ARROW_RIGHT], (_) => goTo('next'), ref.current);
  useKeyPress([ARROW_LEFT], (_) => goTo('prev'), ref.current);

  return (
    <>
      <PhotoGallery
        photosWithRatio={photosWithRatio}
        sideMargin={sideMargin}
        bottomMargin={bottomMargin}
        targetRowHeight={targetRowHeight}
        columns={columns}
        onPhotoClick={(id) => setImageId(id)}
      />
      <PhotoViewer
        selectedPhoto={selectedPhoto}
        loading={imageIsLoading}
        onClose={() => setImageId(undefined)}
        onLoaded={() => setImageIsLoading(false)}
        goToNext={photos.length > 1 ? () => goTo('next') : undefined}
        goToPrevious={photos.length > 1 ? () => goTo('prev') : undefined}
      />
    </>
  );
};
