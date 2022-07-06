import { CircularProgress } from '@mui/material';
import DangerousIcon from '@mui/icons-material/Dangerous';
import PhotoLibraryIcon from '@mui/icons-material/PhotoLibrary';
import { PhotoApi } from 'api/PhotoApi';
import { NonIdealState } from 'components/NonIdealState';
import { usePromise } from 'hooks';
import React, { useCallback, useEffect } from 'react';
import { PhotoGallery } from 'components/Photo/PhotoGallery';

export const Gallery: React.FunctionComponent = () => {
  const listPhotos = useCallback(() => PhotoApi.listPhotos(), []);
  const {
    trigger: reloadPhotos, data: photos, error: listPhotosError, loading: listPhotosLoading,
  } = usePromise(listPhotos);

  // Fetch photos on startup
  useEffect(() => {
    reloadPhotos();
  }, [reloadPhotos]);

  const photosWithRatio = (photos ?? []).map((photo) => ({ ...photo, width: 1, height: 1 }));

  return (
    <>
      {listPhotosLoading
        && <NonIdealState description="Loading photo metadata" icon={<CircularProgress />} />}
      {listPhotosError && (
        <NonIdealState
          description="Failed to fetch photos"
          icon={<DangerousIcon fontSize="large" />}
        />
      )}
      {photos !== undefined && photos.length === 0 && (
        <NonIdealState
          title="No photos found."
          icon={<PhotoLibraryIcon fontSize="large" />}
        />
      )}
      {photos !== undefined && photos.length > 0 && (
        <PhotoGallery
          photos={photosWithRatio}
          margin={2}
          onPhotoClick={(_) => 1}
        />
      )}
    </>
  );
};
