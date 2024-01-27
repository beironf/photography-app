import { PaletteMode } from '@mui/material';
import { isMobile, isTablet } from 'react-device-detect';

const mode: PaletteMode = 'light';

const deviceSensitive = (
  desktopVal: any,
  tabletVal: any,
  mobileVal: any,
): any => {
  if (isTablet) {
    return tabletVal;
  }
  if (isMobile) {
    return mobileVal;
  }
  return desktopVal;
};

export const theme = {
  mode,
  modeContrastColor: mode === 'light' ? 'black' : 'white',

  primaryPadding: 20,
  secondaryPadding: 12,

  // Header and Logo
  headerTextVariant: deviceSensitive('h4', 'h4', 'subtitle1'),
  logoSize: deviceSensitive(70, 70, 50),

  // Menu
  menuActiveColor: 'rgba(0, 0, 0, 0.4)',
  menuInactiveColor: 'rgba(0, 0, 0, 0.8)',
  menuTextVariant: 'button',
  mobileMenuDrawerWidth: 300,

  // Showroom
  showroomColumns: deviceSensitive(undefined, 2, 1),
  showroomTargetRowHeight: deviceSensitive(400, undefined, undefined),

  // Gallery
  galleryColumns: deviceSensitive(undefined, 2, 1),
  galleryTargetRowHeight: deviceSensitive(250, undefined, undefined),

  // About
  aboutContentMaxWidth: deviceSensitive(800, 800, 600),
  aboutSubtitleVariant: deviceSensitive('h5', 'h5', 'h6'),
  aboutBodyTextVariant: deviceSensitive('body1', 'body1', 'body2'),
  aboutAvatarSize: 120,

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
