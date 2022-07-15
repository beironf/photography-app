import React from 'react';
import { ImageApi } from 'api/ImageApi';
import { Photo } from 'model/photo';
import { CircularProgress, Dialog, IconButton } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';
import ArrowBackIosNewIcon from '@mui/icons-material/ArrowBackIosNew';

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
}) => (
  <Dialog
    open={selectedPhoto !== undefined}
    onClose={onClose}
    fullWidth
    maxWidth="xl"
    PaperProps={{
      sx: {
        p: '0 48px',
        height: '85%',
        position: 'relative',
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
    {!loading && (
      <IconButton
        onClick={goToPrevious}
        size="large"
        style={{
          position: 'absolute', left: 0, top: '50%', transform: 'translateY(-50%)',
        }}
      >
        <ArrowBackIosNewIcon />
      </IconButton>
    )}
    {!loading && (
      <IconButton
        onClick={goToNext}
        size="large"
        style={{
          position: 'absolute', top: '50%', right: 0, transform: 'translateY(-50%)',
        }}
      >
        <ArrowForwardIosIcon />
      </IconButton>
    )}
    {selectedPhoto !== undefined && !!loading && (<CircularProgress sx={{ margin: 'auto' }} />)}
    {selectedPhoto !== undefined && (
      <img
        onLoad={onLoaded !== undefined ? () => onLoaded() : undefined}
        style={{
          width: '100%',
          height: '100%',
          objectFit: 'contain',
          display: !loading ? 'flex' : 'none',
        }}
        src={ImageApi.ImageRoute.getImageUrl(selectedPhoto.imageId)}
        alt={selectedPhoto.title}
      />
    )}
  </Dialog>
);
