import React, { useState } from 'react';
import { ImageApi } from 'api/ImageApi';
import { Photo } from 'model/photo';
import {
  CircularProgress, Dialog, IconButton,
} from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';
import ArrowBackIosNewIcon from '@mui/icons-material/ArrowBackIosNew';
import InfoOutlinedIcon from '@mui/icons-material/InfoOutlined';
import { theme } from 'style/theme';
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
  const [showInfo, setShowInfo] = useState(false);
  const [infoHeight, setInfoHeight] = useState(0);

  return (
    <Dialog
      open={selectedPhoto !== undefined}
      onClose={onClose}
      fullWidth
      maxWidth="xl"
      sx={{
        backdropFilter: 'blur(1px)',
        backgroundColor: 'rgba(0,0,0,0.3)',
      }}
      PaperProps={{
        sx: {
          p: '0 48px',
          height: '85%',
          position: 'relative',
          backgroundColor: 'rgba(0,0,0,0.5)',
          backgroundImage: 'none',
          boxShadow: 'none',
        },
      }}
    >
      <IconButton
        onClick={onClose}
        size="large"
        style={{
          position: 'absolute', top: 0, right: 0,
        }}
      >
        <CloseIcon />
      </IconButton>
      {!loading && goToPrevious !== undefined && (
        <IconButton
          onClick={goToPrevious}
          size="large"
          style={{
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
            position: 'absolute', bottom: 0, right: 0,
          }}
        >
          <InfoOutlinedIcon />
        </IconButton>
      )}
      {selectedPhoto !== undefined && !!loading && (<CircularProgress sx={{ margin: 'auto' }} />)}
      {selectedPhoto !== undefined && (
        <img
          onLoad={onLoaded !== undefined ? () => onLoaded() : undefined}
          style={{
            flex: 1,
            minHeight: 0,
            width: '100%',
            objectFit: 'contain',
            display: !loading ? 'flex' : 'none',
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
