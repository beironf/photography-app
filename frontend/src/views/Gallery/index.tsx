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
  useCallback, useEffect, useState,
} from 'react';
import { PhotoCategory } from 'model/metadata';
import { theme } from 'style/theme';
import { PhotoGalleryWithViewer } from 'components/Photo/PhotoGalleryWithViewer';
import { GalleryFilters } from './GalleryFilters';

export const Gallery: React.FunctionComponent = () => {
  const [useFilters, setUseFilters] = useState(false);
  const [categoryFilter, setCategoryFilter] = useState<PhotoCategory>();
  const [groupFilter, setGroupFilter] = useState<string>();
  const [ratingFilter, setRatingFilter] = useState<number>();

  const listPhotos = useCallback(
    () => PhotoApi.listPhotos(categoryFilter, groupFilter, ratingFilter),
    [categoryFilter, groupFilter, ratingFilter],
  );
  const {
    trigger: reloadPhotos, data: photosWithRatio, error: listPhotosError,
    loading: listPhotosLoading,
  } = usePromise(listPhotos);

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
        <PhotoGalleryWithViewer
          urlPath="/gallery"
          photosWithRatio={photosWithRatio}
          sideMargin={theme.primaryPadding}
          bottomMargin={theme.primaryPadding}
          targetRowHeight={theme.galleryTargetRowHeight}
          columns={theme.galleryColumns}
        />
      )}
    </>
  );
};
