/* eslint-disable jsx-a11y/label-has-associated-control */
import { Box } from '@mui/material';
import SettingsIcon from '@mui/icons-material/Settings';
import React from 'react';
import { theme } from 'style/theme';
import { Menu, MenuItem } from './Menu';

type props = {
  body: JSX.Element;
  footer?: JSX.Element;
};

const menuItems: MenuItem[] = [
  {
    id: 'manage-photos',
    label: 'Manage Photos',
    icon: <SettingsIcon />,
    onClick: () => 1,
  },
];

export const PageLayout: React.FunctionComponent<props> = ({ body, footer }) => (
  <>
    <Menu items={menuItems} />
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        minHeight: '100vh',
      }}
    >
      <div
        style={{
          width: '100%',
          flexGrow: 1,
          margin: 0,
          padding: 0,
          backgroundColor: theme.primaryDark,
        }}
      >
        {body}
      </div>
      {footer !== undefined && (
        <footer
          style={{
            width: '100%',
            flexShrink: 0,
            padding: 0,
            margin: 0,
          }}
        >
          {footer}
        </footer>
      )}
    </Box>
  </>
);
