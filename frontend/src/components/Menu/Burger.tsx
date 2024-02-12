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
      top: theme.primaryPadding + 4,
      left: theme.primaryPadding - 10,
      padding: '8px',
      color: theme.modeContrastColor,
      backgroundColor: theme.modeColor,
      border: '1px solid rgba(0, 0, 0, 0.1)',
      zIndex: 1000,
    }}
  >
    <MenuIcon fontSize={isTablet ? 'large' : 'medium'} />
  </IconButton>
);
