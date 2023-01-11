import { CircularProgress } from '@mui/material';
import DangerousIcon from '@mui/icons-material/Dangerous';
import PhotoLibraryIcon from '@mui/icons-material/PhotoLibrary';
import { PhotoApi } from 'api/PhotoApi';
import { NonIdealState } from 'components/NonIdealState';
import { usePromise } from 'hooks';
import React, {
  useCallback, useEffect,
} from 'react';
import { theme } from 'style/theme';
import { PhotoGalleryWithViewer } from 'components/Photo/PhotoGalleryWithViewer';

export const Showroom: React.FunctionComponent = () => {
  const listPhotos = useCallback(
    () => PhotoApi.listPhotos(undefined, undefined, undefined, true),
    [],
  );
  const {
    trigger: reloadPhotos, data: photosWithRatio, error: listPhotosError,
    loading: listPhotosLoading,
  } = usePromise(listPhotos);

  // Fetch photos on startup
  useEffect(() => {
    reloadPhotos();
  }, [reloadPhotos]);

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
        <PhotoGalleryWithViewer
          urlPath="/showroom"
          photosWithRatio={photosWithRatio}
          sideMargin={theme.primaryPadding}
          bottomMargin={theme.primaryPadding}
          targetRowHeight={theme.showroomTargetRowHeight}
          columns={theme.showroomColumns}

        />
      )}
    </>
  );
};
