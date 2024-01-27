import React, { useEffect, useRef, useState } from 'react';
import { Photo } from 'model/photo';
import { Drawer, Grid, IconButton, Typography } from '@mui/material';
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

  const dividerBorder = `0.5px ${theme.photoInfoDividerColor} solid`;

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
          borderTop: `1px solid ${theme.photoInfoDividerColor}`,
          backgroundColor: theme.photoInfoBackgroundColor,
        },
      }}
    >
      <IconButton
        onClick={() => onClose()}
        size="large"
        style={{
          color: theme.photoViewerIconColor,
          position: 'absolute',
          top: 0,
          right: 0,
        }}
      >
        <ExpandMoreIcon />
      </IconButton>

      <Typography
        variant="h6"
        style={{
          color: theme.photoViewerTitleColor,
          textAlign: 'center',
          marginBottom: `${theme.secondaryPadding}px`,
        }}
      >
        {photo.title}
      </Typography>

      <Grid container>
        <Grid
          item
          xs={6}
          style={{
            borderRight: dividerBorder,
            padding: `${theme.secondaryPadding}px`,
          }}
        >
          <TextWithIcon
            icon={
              <CategoryIcon
                htmlColor={theme.photoViewerIconColor}
                fontSize="small"
              />
            }
            texts={[photo.metadata.category]}
            textColor={theme.photoViewerTextColor}
            align="right"
          />
          <TextWithIcon
            icon={
              <CollectionsIcon
                htmlColor={theme.photoViewerIconColor}
                fontSize="small"
              />
            }
            texts={[photo.group ?? 'Not in a collection']}
            textColor={theme.photoViewerTextColor}
            marginBottom={theme.secondaryPadding}
            align="right"
            noValue={photo.group === undefined}
          />

          <TextWithIcon
            icon={
              <PhotoFilterIcon
                htmlColor={theme.photoViewerIconColor}
                fontSize="small"
              />
            }
            texts={
              (photo.metadata.cameraTechniques ?? []).length > 0
                ? [photo.metadata.cameraTechniques.join(', ')]
                : ['No special techniques used']
            }
            textColor={theme.photoViewerTextColor}
            align="right"
            noValue={(photo.metadata.cameraTechniques ?? []).length === 0}
          />
          <TextWithIcon
            icon={
              <SellIcon
                htmlColor={theme.photoViewerIconColor}
                fontSize="small"
              />
            }
            texts={
              (photo.metadata.tags ?? []).length > 0
                ? [photo.metadata.tags.join(', ')]
                : ['No tags']
            }
            textColor={theme.photoViewerTextColor}
            align="right"
            noValue={(photo.metadata.tags ?? []).length === 0}
          />
        </Grid>
        <Grid
          item
          xs={6}
          style={{
            borderLeft: dividerBorder,
            padding: `${theme.secondaryPadding}px`,
          }}
        >
          <TextWithIcon
            icon={
              <PlaceIcon
                htmlColor={theme.photoViewerIconColor}
                fontSize="small"
              />
            }
            texts={[`${photo.location.name}, ${photo.location.country}`]}
            textColor={theme.photoViewerTextColor}
          />
          <TextWithIcon
            icon={
              <ScheduleIcon
                htmlColor={theme.photoViewerIconColor}
                fontSize="small"
              />
            }
            texts={[formatDate(photo.taken)]}
            textColor={theme.photoViewerTextColor}
            marginBottom={theme.secondaryPadding}
          />

          <TextWithIcon
            icon={
              <PhotoCameraIcon
                htmlColor={theme.photoViewerIconColor}
                fontSize="small"
              />
            }
            texts={[photo.gear.camera]}
            textColor={theme.photoViewerTextColor}
          />
          <TextWithIcon
            icon={
              <CameraIcon
                htmlColor={theme.photoViewerIconColor}
                fontSize="small"
              />
            }
            texts={[photo.gear.lens]}
            textColor={theme.photoViewerTextColor}
          />
          <TextWithIcon
            icon={
              <TuneIcon
                htmlColor={theme.photoViewerIconColor}
                fontSize="small"
              />
            }
            texts={[
              photo.cameraSettings.focalLength,
              photo.cameraSettings.aperture,
              photo.cameraSettings.exposureTime,
              photo.cameraSettings.iso,
            ]}
            textColor={theme.photoViewerTextColor}
          />
        </Grid>
      </Grid>
    </Drawer>
  );
};
