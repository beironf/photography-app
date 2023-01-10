import {
  CircularProgress, IconButton,
} from '@mui/material';
import FilterAltIcon from '@mui/icons-material/FilterAlt';
import FilterAltOffIcon from '@mui/icons-material/FilterAltOff';
import DangerousIcon from '@mui/icons-material/Dangerous';
import PhotoLibraryIcon from '@mui/icons-material/PhotoLibrary';
import { PhotoApi } from 'api/PhotoApi';
import { NonIdealState } from 'components/NonIdealState';
import { usePromise } from 'hooks';
import React, {
  useCallback, useEffect, useRef, useMemo, useState,
} from 'react';
import { PhotoGallery } from 'components/Photo/PhotoGallery';
import { PhotoViewer } from 'components/Photo/PhotoViewer';
import { useNavigate, useParams } from 'react-router-dom';
import { ImageRenderer } from 'components/Image/ImageRenderer';
import { ARROW_LEFT, ARROW_RIGHT, useKeyPress } from 'hooks/use-key-press';
import { nextIndex, prevIndex } from 'util/carousel-utils';
import { PhotoCategory } from 'model/metadata';
import { theme } from 'style/theme';
import { GalleryFilters } from './GalleryFilters';

export const Gallery: React.FunctionComponent = () => {
  const params = useParams();
  const { imageId } = params;

  const [imageIsLoading, setImageIsLoading] = useState(false);

  const [useFilters, setUseFilters] = useState(false);
  const [categoryFilter, setCategoryFilter] = useState<PhotoCategory>();
  const [groupFilter, setGroupFilter] = useState<string>();
  const [ratingFilter, setRatingFilter] = useState<number>();

  const navigate = useNavigate();
  const setImageId = useCallback((id?: string) => {
    if (id !== undefined) {
      navigate(`/gallery/${id}`);
      setImageIsLoading(true);
    } else navigate('/gallery');
  }, [navigate]);

  const listPhotos = useCallback(
    () => PhotoApi.listPhotos(categoryFilter, groupFilter, ratingFilter),
    [categoryFilter, groupFilter, ratingFilter],
  );
  const {
    trigger: reloadPhotos, data: photosWithRatio, error: listPhotosError,
    loading: listPhotosLoading,
  } = usePromise(listPhotos);
  const photos = useMemo(() => (photosWithRatio ?? []).map((_) => _.photo), [photosWithRatio]);

  // Fetch photos on startup
  useEffect(() => {
    reloadPhotos();
  }, [reloadPhotos]);

  // Reset filters on hide
  useEffect(() => {
    if (!useFilters) {
      setCategoryFilter(undefined);
      setGroupFilter(undefined);
      setRatingFilter(undefined);
    }
  }, [useFilters]);

  const selectedIndex = photos.findIndex((_) => _.imageId === imageId);
  const selectedPhoto = selectedIndex !== -1 ? photos[selectedIndex] : undefined;

  const goTo = useCallback((direction: 'next' | 'prev') => {
    if (!imageIsLoading) {
      const newIndex = direction === 'next'
        ? nextIndex(selectedIndex, photos.length)
        : prevIndex(selectedIndex, photos.length);
      if (newIndex === -1) setImageId(undefined);
      else setImageId(photos[newIndex].imageId);
    }
  }, [photos, selectedIndex, imageIsLoading, setImageId]);

  const galleryRef = useRef();
  useKeyPress([ARROW_RIGHT], (_) => goTo('next'), galleryRef.current);
  useKeyPress([ARROW_LEFT], (_) => goTo('prev'), galleryRef.current);

  return (
    <>
      <IconButton
        onClick={() => setUseFilters(!useFilters)}
        sx={{
          position: 'absolute',
          top: `${theme.primaryPadding}px`,
          right: `${theme.primaryPadding}px`,
        }}
      >
        {useFilters && <FilterAltOffIcon />}
        {!useFilters && <FilterAltIcon />}
      </IconButton>
      <GalleryFilters
        active={useFilters}
        category={categoryFilter}
        group={groupFilter}
        rating={ratingFilter}
        setCategory={(c) => setCategoryFilter(c)}
        setGroup={(g) => setGroupFilter(g)}
        setRating={(r) => setRatingFilter(r)}
      />
      {listPhotosLoading
        && <NonIdealState description="Loading photo metadata" icon={<CircularProgress />} />}
      {listPhotosError && (
        <NonIdealState
          description="Failed to fetch photos"
          icon={<DangerousIcon fontSize="large" />}
        />
      )}
      {photosWithRatio !== undefined && photosWithRatio.length === 0 && (
        <NonIdealState
          title="No photos found."
          icon={<PhotoLibraryIcon fontSize="large" />}
        />
      )}
      {photosWithRatio !== undefined && photosWithRatio.length > 0 && (
        <>
          <PhotoGallery
            photosWithRatio={photosWithRatio}
            sideMargin={theme.primaryPadding}
            bottomMargin={theme.primaryPadding}
            targetRowHeight={300}
            onPhotoClick={(id) => setImageId(id)}
            renderPhoto={
              ({ photo: image }) => (
                <ImageRenderer
                  key={image.key}
                  selected={false}
                  noImageSelected
                  image={image}
                  onImageClick={
                    () => (imageId === image.key
                      ? setImageId(undefined)
                      : setImageId(image.key))
                  }
                />
              )
            }
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
      )}
    </>
  );
};
