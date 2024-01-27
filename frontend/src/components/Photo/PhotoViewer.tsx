import React, { useCallback, useRef, useState } from 'react';
import { ImageApi } from 'api/ImageApi';
import { Photo } from 'model/photo';
import { CircularProgress, Dialog, IconButton } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';
import ArrowBackIosNewIcon from '@mui/icons-material/ArrowBackIosNew';
import InfoOutlinedIcon from '@mui/icons-material/InfoOutlined';
import { theme } from 'style/theme';
import { useKeyPress } from 'hooks/use-key-press';
import { PhotoInfoDrawer } from './PhotoInfoDrawer';

type props = {
  selectedPhoto?: Photo;
  loading?: boolean;
  onClose: () => void;
  onLoaded?: () => void;
  goToNext?: () => void;
  goToPrevious?: () => void;
};

export const PhotoViewer: React.FunctionComponent<props> = ({
  selectedPhoto,
  loading,
  onClose,
  onLoaded,
  goToNext,
  goToPrevious,
}) => {
  const [showInfo, setShowInfo] = useState(true);
  const [infoHeight, setInfoHeight] = useState(0);

  const toggleShowInfo = useCallback(() => setShowInfo(!showInfo), [showInfo]);

  const viewerRef = useRef();
  useKeyPress(['i'], (_) => toggleShowInfo(), viewerRef.current);

  return (
    <Dialog
      open={selectedPhoto !== undefined}
      onClose={onClose}
      fullWidth
      maxWidth="xl"
      sx={{
        backdropFilter: theme.photoViewerBackdropFilter,
        backgroundColor: theme.photoViewerBackdropColor,
      }}
      PaperProps={{
        sx: {
          margin: '32px',
          height: 'calc(100% - 64px)',
          p: '0 48px',
          position: 'relative',
          backgroundColor: theme.photoViewerBackgroundColor,
          backgroundImage: 'none',
          boxShadow: 'none',
        },
      }}
    >
      <IconButton
        onClick={onClose}
        size="large"
        style={{
          color: theme.photoViewerIconColor,
          position: 'absolute',
          top: 0,
          right: 0,
        }}
      >
        <CloseIcon />
      </IconButton>
      {!loading && goToPrevious !== undefined && (
        <IconButton
          onClick={goToPrevious}
          size="large"
          style={{
            color: theme.photoViewerIconColor,
            position: 'absolute',
            left: 0,
            top: `calc(50% - ${infoHeight / 2}px)`,
            transform: 'translateY(-50%)',
            transition: `top ${theme.drawerTransition}`,
          }}
        >
          <ArrowBackIosNewIcon />
        </IconButton>
      )}
      {!loading && goToNext !== undefined && (
        <IconButton
          onClick={goToNext}
          size="large"
          style={{
            color: theme.photoViewerIconColor,
            position: 'absolute',
            top: `calc(50% - ${infoHeight / 2}px)`,
            right: 0,
            transform: 'translateY(-50%)',
            transition: `top ${theme.drawerTransition}`,
          }}
        >
          <ArrowForwardIosIcon />
        </IconButton>
      )}
      {!loading && !showInfo && (
        <IconButton
          onClick={() => setShowInfo(true)}
          size="large"
          style={{
            color: theme.photoViewerIconColor,
            position: 'absolute',
            bottom: 0,
            right: 0,
          }}
        >
          <InfoOutlinedIcon />
        </IconButton>
      )}
      {selectedPhoto !== undefined && !!loading && (
        <CircularProgress sx={{ margin: 'auto' }} />
      )}
      {selectedPhoto !== undefined && (
        <img
          onLoad={onLoaded !== undefined ? () => onLoaded() : undefined}
          style={{
            flex: 1,
            minHeight: 0,
            width: '100%',
            objectFit: 'contain',
            display: !loading ? 'flex' : 'none',
            padding: `${theme.secondaryPadding}px 0`,
          }}
          src={ImageApi.ImageRoute.getImageUrl(selectedPhoto.imageId)}
          alt={selectedPhoto.title}
        />
      )}
      {selectedPhoto !== undefined && (
        <PhotoInfoDrawer
          open={showInfo}
          photo={selectedPhoto}
          onClose={() => setShowInfo(false)}
          onHeightChange={(height) => setInfoHeight(height)}
        />
      )}
    </Dialog>
  );
};
