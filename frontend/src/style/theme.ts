import { PaletteMode } from '@mui/material';
import { isMobile, isTablet } from 'react-device-detect';

const mode: PaletteMode = 'light';

export const theme = {
  mode,
  modeContrastColor: (mode === 'light') ? 'black' : 'white',

  primaryPadding: 20,
  secondaryPadding: 12,

  // Mobil Menu
  menuWidth: 300,

  // Menu
  menuActiveColor: 'rgba(0, 0, 0, 0.4)',
  menuInactiveColor: 'rgba(0, 0, 0, 0.8)',

  // Header and Logo
  headerHSize: isMobile ? 'h5' : 'h4',
  logoSize: isMobile ? 55 : 75,

  // Showroom
  // eslint-disable-next-line no-nested-ternary
  showroomColumns: isTablet
    ? 2
    : isMobile
      ? 1
      : undefined,
  showroomTargetRowHeight: isMobile ? undefined : 400,

  // Gallery
  // eslint-disable-next-line no-nested-ternary
  galleryColumns: isTablet
    ? 2
    : isMobile
      ? 1
      : undefined,
  galleryTargetRowHeight: isMobile ? undefined : 250,

  // Manage Photos
  managePhotosSelectedColor: 'DodgerBlue',

  // Photo Viewer
  photoViewerBackdropFilter: 'blur(1px)',
  photoViewerBackdropColor: 'rgba(0, 0, 0, 0.5)',
  photoViewerBackgroundColor: 'rgba(0, 0, 0, 0.75)',
  photoViewerTitleColor: 'rgba(255, 255, 255, 1)',
  photoViewerIconColor: 'rgba(255, 255, 255, 0.85)',
  photoViewerTextColor: 'rgba(255, 255, 255, 0.8)',
  photoInfoDividerColor: 'rgba(255, 255, 255, 0.12)',
  photoInfoBackgroundColor: 'rgba(0, 0, 0, 0)',

  // Thumbnail
  thumbnailCaptionColor: 'rgba(255, 255, 255, 1)',
  thumbnailCaptionBackgroundColor: 'rgba(0, 0, 0, 0.5)',

  // Don't change (mirrors the drawer's built in transition)
  drawerTransition: '225ms cubic-bezier(0, 0, 0.2, 1) 0s',

  body2Margin: 10,
  body2IconMargin: 5,
};
