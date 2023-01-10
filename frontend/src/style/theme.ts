import { PaletteMode } from '@mui/material';

const mode: PaletteMode = 'light';

export const theme = {
  mode,
  modeContrastColor: (mode === 'light') ? 'black' : 'white',

  primaryPadding: 20,
  secondaryPadding: 12,

  logoSize: 75,

  // Mobil Menu
  menuWidth: 300,

  // Menu
  menuActiveColor: 'rgba(0, 0, 0, 0.4)',
  menuInactiveColor: 'rgba(0, 0, 0, 0.8)',

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
