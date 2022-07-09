import { CircularProgress, Dialog } from '@mui/material';
import DangerousIcon from '@mui/icons-material/Dangerous';
import PhotoLibraryIcon from '@mui/icons-material/PhotoLibrary';
import { PhotoApi } from 'api/PhotoApi';
import { NonIdealState } from 'components/NonIdealState';
import { usePromise } from 'hooks';
import React, { useCallback, useEffect, useState } from 'react';
import { PhotoGallery } from 'components/Photo/PhotoGallery';
import { useParams } from 'react-router-dom';
import { ImageApi } from 'api/ImageApi';

export const Gallery: React.FunctionComponent = () => {
  const params = useParams();

  const [selectedPhotoId, setSelectedPhotoId] = useState<string>();
  const { imageId } = params;

  const listPhotos = useCallback(() => PhotoApi.listPhotos(), []);
  const {
    trigger: reloadPhotos, data: photosWithRatio, error: listPhotosError,
    loading: listPhotosLoading,
  } = usePromise(listPhotos);

  // Fetch photos on startup
  useEffect(() => {
    reloadPhotos();
  }, [reloadPhotos]);

  // Set selected photo on param change
  useEffect(() => {
    setSelectedPhotoId(imageId);
  }, [imageId]);

  const selectedPhoto = (photosWithRatio ?? [])
    .find((_) => _.photo.imageId === (selectedPhotoId ?? -1));

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
        <>
          <PhotoGallery
            photosWithRatio={photosWithRatio}
            margin={2}
            onPhotoClick={(id) => setSelectedPhotoId(id)}
          />
          <Dialog
            open={selectedPhotoId !== undefined}
            onClose={() => setSelectedPhotoId(undefined)}
            fullWidth
          >
            {selectedPhoto !== undefined && (
              <img
                src={ImageApi.ImageRoute.getImageUrl(selectedPhoto.photo.imageId)}
                alt={selectedPhoto.photo.title}
              />
            )}
          </Dialog>
        </>
      )}
    </>
  );
};
