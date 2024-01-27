import { Paper } from '@mui/material';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import HomeIcon from '@mui/icons-material/Home';
import CollectionsIcon from '@mui/icons-material/Collections';
import React from 'react';
import { useLocation } from 'react-router-dom';
import { Menu } from 'components/Menu';
import { MenuItem } from 'model/menu';
import { PageHeader } from './PageHeader';

type props = {
  body: JSX.Element;
  footer?: JSX.Element;
};

const menuItems: MenuItem[] = [
  {
    id: 'showroom',
    label: 'Showroom',
    icon: <HomeIcon />,
    onClickDestination: '/showroom',
  },
  {
    id: 'gallery',
    label: 'Gallery',
    icon: <CollectionsIcon />,
    onClickDestination: '/gallery',
  },
  {
    id: 'about',
    label: 'About',
    icon: <AccountCircleIcon />,
    onClickDestination: '/about',
  },
];

export const PageLayout: React.FunctionComponent<props> = ({
  body,
  footer,
}) => {
  const location = useLocation();
  const pageHeaderShouldBeHidden = location.pathname.includes('admin');
  const pageMenuShouldBeHidden = location.pathname.includes('admin');

  return (
    <Paper
      elevation={0}
      square
      sx={{
        display: 'flex',
        flexDirection: 'column',
        minHeight: '100vh',
      }}
    >
      <PageHeader hidden={pageHeaderShouldBeHidden} />
      <Menu items={menuItems} hidden={pageMenuShouldBeHidden} />
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
  );
};
