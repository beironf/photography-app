import React, { useEffect, useRef, useState } from 'react';
import { Photo } from 'model/photo';
import {
  Drawer, Grid, IconButton, Typography,
} from '@mui/material';
import PlaceIcon from '@mui/icons-material/Place';
import ScheduleIcon from '@mui/icons-material/Schedule';
import PhotoCameraIcon from '@mui/icons-material/PhotoCamera';
import CameraIcon from '@mui/icons-material/Camera';
import TuneIcon from '@mui/icons-material/Tune';
import SellIcon from '@mui/icons-material/Sell';
import CategoryIcon from '@mui/icons-material/Category';
import CollectionsIcon from '@mui/icons-material/Collections';
import PhotoFilterIcon from '@mui/icons-material/PhotoFilter';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import { theme } from 'style/theme';
import { formatDate } from 'util/date-util';
import { TextWithIcon } from 'components/Text/TextWithIcon';

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

  const dividerBorder = '0.5px rgba(255, 255, 255, 0.12) solid';

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
          p: `${theme.secondaryPadding}px ${theme.primaryPadding}px`,
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

      <Typography
        variant="h6"
        style={{ textAlign: 'center', marginBottom: `${theme.secondaryPadding}px` }}
      >
        {photo.title}
      </Typography>

      <Grid container>
        <Grid
          item
          xs={6}
          style={{ borderRight: dividerBorder, padding: `${theme.secondaryPadding}px` }}
        >
          <TextWithIcon
            icon={<CategoryIcon fontSize="small" />}
            texts={[photo.metadata.category]}
            align="right"
          />
          <TextWithIcon
            icon={<CollectionsIcon fontSize="small" />}
            texts={[photo.group ?? 'Not in a collection']}
            marginBottom={theme.secondaryPadding}
            align="right"
            noValue={photo.group === undefined}
          />

          <TextWithIcon
            icon={<PhotoFilterIcon fontSize="small" />}
            texts={(photo.metadata.cameraTechniques ?? []).length > 0
              ? [photo.metadata.cameraTechniques.join(', ')]
              : ['No special techniques used']}
            align="right"
            noValue={(photo.metadata.cameraTechniques ?? []).length === 0}
          />
          <TextWithIcon
            icon={<SellIcon fontSize="small" />}
            texts={(photo.metadata.tags ?? []).length > 0
              ? [photo.metadata.tags.join(', ')]
              : ['No tags']}
            align="right"
            noValue={(photo.metadata.tags ?? []).length === 0}
          />
        </Grid>
        <Grid
          item
          xs={6}
          style={{ borderLeft: dividerBorder, padding: `${theme.secondaryPadding}px` }}
        >
          <TextWithIcon
            icon={<PlaceIcon fontSize="small" />}
            texts={[`${photo.location.name}, ${photo.location.country}`]}
          />
          <TextWithIcon
            icon={<ScheduleIcon fontSize="small" />}
            texts={[formatDate(photo.taken)]}
            marginBottom={theme.secondaryPadding}
          />

          <TextWithIcon
            icon={<PhotoCameraIcon fontSize="small" />}
            texts={[photo.gear.camera]}
          />
          <TextWithIcon
            icon={<CameraIcon fontSize="small" />}
            texts={[photo.gear.lens]}
          />
          <TextWithIcon
            icon={<TuneIcon fontSize="small" />}
            texts={[
              photo.cameraSettings.focalLength,
              photo.cameraSettings.aperture,
              photo.cameraSettings.exposureTime,
              photo.cameraSettings.iso,
            ]}
          />
        </Grid>
      </Grid>
    </Drawer>
  );
};
