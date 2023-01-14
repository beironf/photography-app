import { IconButton } from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import React from 'react';
import { theme } from 'style/theme';
import { isTablet } from 'react-device-detect';

type props = {
  onClick: () => void;
};

export const Burger: React.FunctionComponent<props> = ({ onClick }) => (
  <IconButton
    onClick={() => onClick()}
    sx={{
      position: 'fixed',
      top: theme.primaryPadding - 8,
      left: theme.primaryPadding - 8,
      padding: '8px',
      color: theme.modeContrastColor,
      zIndex: 1000,
    }}
  >
    <MenuIcon fontSize={isTablet ? 'large' : 'medium'} />
  </IconButton>
);
