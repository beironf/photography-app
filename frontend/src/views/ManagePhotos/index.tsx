import {
  Box, CircularProgress, Drawer,
} from '@mui/material';
import { ImageApi } from 'api/ImageApi';
import { NonIdealState } from 'components/NonIdealState';
import { PhotoEditor } from 'components/PhotoEditor';
import { ImageRemover } from 'components/ImageRemover';
import { usePromise } from 'hooks';
import React, { useCallback, useEffect, useState } from 'react';
import { ImageGallery } from 'components/ImageGallery';
import { theme } from 'style/theme';
import { PhotoApi } from 'api/PhotoApi';
import { ImageRenderer } from './ImageRenderer';
import { ManagePhotosMenu } from './ManagePhotosMenu';

// Styling constants
const drawerWidth = '40%';
const selectionColor = 'white';
const opacityWhenNotSelected = 0.4;

const drawerBaseStyle = {
  width: drawerWidth,
};

const drawerPaperBaseStyle = {
  width: drawerWidth,
  backgroundColor: theme.primaryDark,
  boxSizing: 'border-box',
  p: `${theme.primaryPadding}px`,
};

export const ManagePhotos: React.FunctionComponent = () => {
  const [imageUploaded, setImageUploaded] = useState<string>();
  const [imageRemoved, setImageRemoved] = useState<string>();
  const [selectedImageId, setSelectedImageId] = useState<string>();

  const listImages = useCallback(() => ImageApi.ImageRoute.listImages(), []);
  const {
    trigger: reloadImages, data: images, error: listImagesError, loading: listImagesLoading,
  } = usePromise(listImages);

  const listPhotos = useCallback(() => PhotoApi.listPhotos(), []);
  const {
    trigger: reloadPhotos, data: photos, error: listPhotosError, loading: listPhotosLoading,
  } = usePromise(listPhotos);

  // Fetch images on startup
  useEffect(() => {
    reloadImages();
  }, [reloadImages]);

  // Reload photos on `images` change
  useEffect(() => {
    reloadPhotos();
  }, [images, reloadPhotos]);

  // Reload images on image added
  useEffect(() => {
    if (imageUploaded !== undefined) {
      reloadImages();
      setSelectedImageId(imageUploaded);
      setImageUploaded(undefined);
    }
  }, [reloadImages, imageUploaded]);

  // Reload images on image removed
  useEffect(() => {
    if (imageRemoved !== undefined) {
      reloadImages();
      setImageRemoved(undefined);
      setSelectedImageId(undefined);
    }
  }, [reloadImages, imageRemoved]);

  return (
    <Box sx={{ display: 'flex', backgroundColor: theme.primaryDark }}>
      <Box
        component="main"
        sx={{ flexGrow: 1, marginRight: '-3px' }}
      >
        {listImagesLoading && <CircularProgress />}
        {listImagesError && <NonIdealState title="No images found" />}
        {images !== undefined && (
          <ImageGallery
            images={images}
            margin={2}
            renderImage={
              ({ photo: image }) => (
                <ImageRenderer
                  key={image.key}
                  selectedImageId={selectedImageId}
                  unfinished={(photos ?? []).findIndex((_) => _.imageId === image.key) === -1}
                  image={image}
                  margin={2}
                  selectionColor={selectionColor}
                  opacityWhenNotSelected={opacityWhenNotSelected}
                  onImageClick={
                    () => (selectedImageId === image.key
                      ? setSelectedImageId(undefined)
                      : setSelectedImageId(image.key))
                  }
                />
              )
            }
          />
        )}
      </Box>
      <Drawer
        variant="permanent"
        anchor="right"
        sx={{
          ...drawerBaseStyle,
          flexShrink: 0,
          '& .MuiDrawer-paper': {
            ...drawerPaperBaseStyle,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            justifyContent: 'center',
            flexWrap: 'wrap',
          },
        }}
      >
        {listPhotosLoading && <CircularProgress style={{ margin: theme.primaryPadding }} />}
        {listPhotosError && <NonIdealState title="There was a problem when fetching photos" />}
        <ManagePhotosMenu setImageUploaded={(imageId) => setImageUploaded(imageId)} />
      </Drawer>
      <Drawer
        variant="persistent"
        anchor="right"
        open={selectedImageId !== undefined}
        sx={{
          ...drawerBaseStyle,
          position: 'absolute',
          right: 0,
          top: 0,
          '& .MuiDrawer-paper': {
            ...drawerPaperBaseStyle,
          },
        }}
      >
        {selectedImageId !== undefined && !listPhotosError && (
          <>
            <PhotoEditor
              imageId={selectedImageId}
              onNewPhoto={() => reloadPhotos()}
            />
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
