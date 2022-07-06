import { Paper } from '@mui/material';
import HomeIcon from '@mui/icons-material/Home';
import CollectionsIcon from '@mui/icons-material/Collections';
import SettingsIcon from '@mui/icons-material/Settings';
import React from 'react';
import { NavigateFunction, useNavigate } from 'react-router-dom';
import { Menu, MenuItem } from 'components/Menu';

type props = {
  body: JSX.Element;
  footer?: JSX.Element;
};

const menuItems = (navigate: NavigateFunction): MenuItem[] => [
  {
    id: 'showroom',
    label: 'Showroom',
    icon: <HomeIcon />,
    onClick: () => navigate('/'),
  },
  {
    id: 'gallery',
    label: 'Gallery',
    icon: <CollectionsIcon />,
    onClick: () => navigate('/gallery'),
  },
  {
    id: 'manage-photos',
    label: 'Manage Photos',
    icon: <SettingsIcon />,
    onClick: () => navigate('/manage-photos'),
  },
];

export const PageLayout: React.FunctionComponent<props> = ({ body, footer }) => {
  const navigate = useNavigate();

  return (
    <>
      <Menu items={menuItems(navigate)} />
      <Paper
        elevation={0}
        square
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
      </Paper>
    </>
  );
};
