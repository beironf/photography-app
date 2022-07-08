import { CircularProgress } from '@mui/material';
import DangerousIcon from '@mui/icons-material/Dangerous';
import PhotoLibraryIcon from '@mui/icons-material/PhotoLibrary';
import { PhotoApi } from 'api/PhotoApi';
import { NonIdealState } from 'components/NonIdealState';
import { usePromise } from 'hooks';
import React, { useCallback, useEffect } from 'react';
import { PhotoGallery } from 'components/Photo/PhotoGallery';
import { useParams } from 'react-router-dom';

export const Gallery: React.FunctionComponent = () => {
  const listPhotos = useCallback(() => PhotoApi.listPhotos(), []);
  const {
    trigger: reloadPhotos, data: photosWithRatio, error: listPhotosError,
    loading: listPhotosLoading,
  } = usePromise(listPhotos);

  // Fetch photos on startup
  useEffect(() => {
    reloadPhotos();
  }, [reloadPhotos]);

  const params = useParams();
  // eslint-disable-next-line no-unused-vars
  const { imageId } = params;

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
      {photosWithRatio !== undefined && photosWithRatio.length === 0 && (
        <NonIdealState
          title="No photos found."
          icon={<PhotoLibraryIcon fontSize="large" />}
        />
      )}
      {photosWithRatio !== undefined && photosWithRatio.length > 0 && (
        <PhotoGallery
          photosWithRatio={photosWithRatio}
          margin={2}
          onPhotoClick={(_) => 1}
        />
      )}
    </>
  );
};
