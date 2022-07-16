import React, { useEffect, useRef, useState } from 'react';
import { Photo } from 'model/photo';
import {
  Drawer, IconButton, Typography,
} from '@mui/material';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import { theme } from 'style/theme';

type props = {
  open: boolean;
  photo: Photo;
  onClose: () => void;
  onHeightChange?: (_: number) => void;
};

export const PhotoInfoDrawer: React.FunctionComponent<props> = ({
  open,
  photo,
  onClose,
  onHeightChange,
}) => {
  const [height, setHeight] = useState(0);
  const heightRef = useRef(null);

  useEffect(() => {
    const newHeight = heightRef.current.clientHeight;
    setHeight(open ? newHeight : 0);
    onHeightChange(newHeight);
  }, [open, onHeightChange]);

  return (
    <Drawer
      variant="persistent"
      open={open}
      anchor="bottom"
      sx={{
        height: `${height}px`,
        transition: `height ${theme.drawerTransition}`,
      }}
      PaperProps={{
        ref: heightRef,
        sx: {
          position: 'absolute',
          height: 'auto',
          display: open ? 'flex' : 'none',
          p: `${theme.primaryPadding}px`,
          backgroundColor: 'rgba(0,0,0,0)',
        },
      }}
    >
      <IconButton
        onClick={() => onClose()}
        size="large"
        style={{ position: 'absolute', top: 0, right: 0 }}
      >
        <ExpandMoreIcon />
      </IconButton>

      <Typography variant="h5">
        {photo.title}
      </Typography>
    </Drawer>
  );
};
