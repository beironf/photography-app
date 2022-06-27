import {
  Box, CircularProgress, Divider, Drawer,
} from '@mui/material';
import { ImageApi } from 'api/ImageApi';
import { NonIdealState } from 'components/NonIdealState';
import { PhotoEditor } from 'components/PhotoEditor';
import { ImageUploader } from 'components/ImageUploader';
import { ImageRemover } from 'components/ImageRemover';
import { usePromise } from 'hooks';
import React, { useCallback, useEffect, useState } from 'react';
import { ImageGallery } from 'components/ImageGallery';
import { theme } from 'style/theme';
import { ImageRenderer } from './ImageRenderer';

export const ManagePhotos: React.FunctionComponent = () => {
  const [imageUploaded, setImageUploaded] = useState<string>();
  const [imageRemoved, setImageRemoved] = useState<string>();
  const [selectedImageId, setSelectedImageId] = useState<string>();

  const listImages = useCallback(() => ImageApi.ImageRoute.listImages(), []);
  const {
    trigger: reloadImages, data: images, error, loading,
  } = usePromise(listImages);

  useEffect(() => {
    if (imageUploaded !== undefined) {
      reloadImages();
      setSelectedImageId(imageUploaded);
      setImageUploaded(undefined);
    }
  }, [reloadImages, imageUploaded]);

  useEffect(() => {
    if (imageRemoved !== undefined) {
      reloadImages();
      setImageRemoved(undefined);
      setSelectedImageId(undefined);
    }
  }, [reloadImages, imageRemoved]);

  useEffect(() => {
    reloadImages();
  }, [reloadImages]);

  // Styling constants
  const drawerWidth = '40%';
  const selectionColor = 'white';
  const opacityWhenNotSelected = 0.4;

  return (
    <Box sx={{ display: 'flex', backgroundColor: theme.primaryDark }}>
      <Box
        component="main"
        sx={{ flexGrow: 1, p: '2px' }}
      >
        {loading && <CircularProgress />}
        {error && <NonIdealState title="No images found" />}
        {images !== undefined && (
          <ImageGallery
            images={images}
            renderImage={
              ({ photo }) => (
                <ImageRenderer
                  key={photo.key}
                  selectedImageId={selectedImageId}
                  photo={photo}
                  margin={2}
                  selectionColor={selectionColor}
                  opacityWhenNotSelected={opacityWhenNotSelected}
                  onImageClick={
                    () => (selectedImageId === photo.key
                      ? setSelectedImageId(undefined)
                      : setSelectedImageId(photo.key))
                  }
                />
              )
            }
          />
        )}
      </Box>
      <Drawer
        sx={{
          width: drawerWidth,
          flexShrink: 0,
          '& .MuiDrawer-paper': {
            width: drawerWidth,
            boxSizing: 'border-box',
            p: 2,
          },
        }}
        variant="permanent"
        anchor="right"
      >
        {selectedImageId === undefined && (
          <ImageUploader
            onImageUploaded={(imageId) => setImageUploaded(imageId)}
          />
        )}
        {selectedImageId !== undefined && <PhotoEditor imageId={selectedImageId} />}
        {selectedImageId !== undefined && (
          <>
            <Divider sx={{ marginTop: 3 }} />
            <ImageRemover
              imageId={selectedImageId}
              onImageRemoved={(imageId) => setImageRemoved(imageId)}
            />
          </>
        )}
      </Drawer>
    </Box>
  );
};
