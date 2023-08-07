import React from 'react';
import { Button, Grid, Typography } from '@mui/material';
import { theme } from 'style/theme';
import { MenuItem } from 'model/menu';
import { useLocation, useNavigate } from 'react-router-dom';

type Props = {
  items: MenuItem[];
};

export const DesktopMenu: React.FunctionComponent<Props> = ({ items }) => {
  const navigate = useNavigate();
  const location = useLocation();

  const isActive = (menuLocation: string): boolean => location.pathname.includes(menuLocation);

  return (
    <Grid
      container
      key="desktop-menu"
      columnGap={`${theme.primaryPadding}px`}
      justifyContent="center"
      alignItems="center"
      marginBottom={`${theme.primaryPadding}px`}
    >
      {items.map((item) => (
        <Grid
          item
          id={`desktop-menu-item-${item.id}`}
          key={`desktop-menu-item-${item.id}`}
        >
          <Button onClick={() => navigate(item.onClickDestination)}>
            <Typography
              color={
                isActive(item.onClickDestination)
                  ? theme.menuActiveColor
                  : theme.menuInactiveColor
              }
              variant={theme.menuTextVariant as any}
              align="center"
            >
              {item.label}
            </Typography>
          </Button>
        </Grid>
      ))}
    </Grid>
  );
};
