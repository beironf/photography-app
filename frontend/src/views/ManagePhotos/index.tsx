import { CircularProgress, Drawer } from '@mui/material';
import FlagIcon from '@mui/icons-material/Flag';
import DangerousIcon from '@mui/icons-material/Dangerous';
import ImageSearchIcon from '@mui/icons-material/ImageSearch';
import { ImageApi } from 'api/ImageApi';
import { NonIdealState } from 'components/NonIdealState';
import { PhotoEditor } from 'components/PhotoEditor';
import { ImageRemover } from 'components/Image/ImageRemover';
import { usePromise } from 'hooks';
import React, {
  useCallback, useEffect, useRef, useState,
} from 'react';
import { ImageGallery } from 'components/Image/ImageGallery';
import { ImageRenderer } from 'components/Image/ImageRenderer';
import { theme } from 'style/theme';
import { PhotoApi } from 'api/PhotoApi';
import { nextIndex, prevIndex } from 'util/carousel-utils';
import {
  ARROW_DOWN, ARROW_UP, useKeyPress,
} from 'hooks/use-key-press';
import { ManagePhotosMenu } from './ManagePhotosMenu';

const drawerWidth = '40%';
const drawerBaseStyle = {
  width: drawerWidth,
};
const drawerPaperBaseStyle = {
  width: drawerWidth,
  boxSizing: 'border-box',
  p: `${theme.primaryPadding}px`,
};

export const ManagePhotos: React.FunctionComponent = () => {
  const ref = useRef();

  const [imageUploaded, setImageUploaded] = useState<string>();
  const [imageRemoved, setImageRemoved] = useState<string>();
  const [selectedImageId, setSelectedImageId] = useState<string>();

  const [onlyUnfinished, setOnlyUnfinished] = useState(false);

  const listImages = useCallback(() => ImageApi.ImageRoute.listImages(), []);
  const {
    trigger: reloadImages, data: images, error: listImagesError, loading: listImagesLoading,
  } = usePromise(listImages);

  const listPhotos = useCallback(() => PhotoApi.listPhotos(), []);
  const {
    trigger: reloadPhotos, data: photosWithRatio, error: listPhotosError,
    loading: listPhotosLoading,
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

  const filteredImages = (images ?? []).filter((image) => {
    if (image.id === selectedImageId) return true;
    if (onlyUnfinished) {
      return !(photosWithRatio ?? []).map((_) => _.photo.imageId).includes(image.id);
    }
    return true;
  });

  const goTo = useCallback((direction: 'next' | 'prev') => {
    const selectedIndex = filteredImages.findIndex((i) => i.id === selectedImageId);
    const newIndex = direction === 'next'
      ? nextIndex(selectedIndex, filteredImages.length)
      : prevIndex(selectedIndex, filteredImages.length);
    if (newIndex === -1) setSelectedImageId(undefined);
    else setSelectedImageId(filteredImages[newIndex].id);
  }, [filteredImages, selectedImageId, setSelectedImageId]);

  useKeyPress([ARROW_DOWN], (_) => goTo('next'), ref.current);
  useKeyPress([ARROW_UP], (_) => goTo('prev'), ref.current);

  const unfinishedIcon = (imageId: string): JSX.Element | undefined => {
    const isUnfinished = (photosWithRatio ?? [])
      .findIndex((_) => _.photo.imageId === imageId) === -1;

    return isUnfinished ? (
      <FlagIcon
        style={{
          color: 'red', position: 'absolute', top: 0, right: 0,
        }}
      />
    ) : undefined;
  };

  return (
    <div style={{ display: 'flex' }}>
      <div style={{ flexGrow: 1, marginRight: '-2px' }}>
        {listImagesLoading
            && <NonIdealState description="Loading images" icon={<CircularProgress />} />}
        {listImagesError && (
          <NonIdealState
            description="No images found"
            icon={<ImageSearchIcon fontSize="large" />}
          />
        )}
        {!listImagesError && filteredImages.length === 0 && (
          <NonIdealState
            title="No images matched filter."
            icon={<ImageSearchIcon fontSize="large" />}
          />
        )}
        {filteredImages.length > 0 && (
          <ImageGallery
            images={filteredImages}
            margin={2}
            targetRowHeight={250}
            renderImage={
              ({ photo: image }) => (
                <ImageRenderer
                  key={image.key}
                  selected={selectedImageId === image.key}
                  noImageSelected={selectedImageId === undefined}
                  selectionColor={theme.managePhotosSelectedColor}
                  child={unfinishedIcon(image.key)}
                  image={image}
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
      </div>
      <Drawer
        variant="permanent"
        anchor="right"
        sx={{
          ...drawerBaseStyle,
          flexShrink: 0,
        }}
        PaperProps={{
          sx: {
            ...drawerPaperBaseStyle,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            justifyContent: 'center',
            flexWrap: 'wrap',
          },
        }}
      >
        <ManagePhotosMenu
          onlyUnfinished={onlyUnfinished}
          setOnlyUnfinished={(bool) => setOnlyUnfinished(bool)}
          setImageUploaded={(imageId) => setImageUploaded(imageId)}
        />
        {listPhotosLoading
          && <NonIdealState description="Loading photo metadata" icon={<CircularProgress />} />}
        {listPhotosError && (
          <NonIdealState
            description="Failed to fetch photos"
            icon={<DangerousIcon fontSize="large" />}
          />
        )}
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
        }}
        PaperProps={{ sx: { ...drawerPaperBaseStyle } }}
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
    </div>
  );
};
