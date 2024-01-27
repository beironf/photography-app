import { MenuItem } from 'model/menu';
import {
  Box,
  Drawer,
  IconButton,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Typography,
} from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import React, { useState } from 'react';
import { theme } from 'style/theme';
import { Burger } from 'components/Menu/Burger';
import { useLocation, useNavigate } from 'react-router-dom';
import { isTablet } from 'react-device-detect';

type Props = {
  items: MenuItem[];
};

export const MobileMenu: React.FunctionComponent<Props> = ({ items }) => {
  const [open, setOpen] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const activeItem = items.find((item) =>
    location.pathname.includes(item.onClickDestination),
  );
  const isActive = (menuLocation: string): boolean =>
    location.pathname.includes(menuLocation);

  return (
    <>
      <div
        style={{
          textAlign: 'center',
          marginBottom: `${theme.primaryPadding}px`,
        }}
      >
        <Typography
          color={theme.menuInactiveColor}
          variant={theme.menuTextVariant as any}
          align="center"
        >
          {(activeItem?.label ?? 'Unknown Menu Item').toUpperCase()}
        </Typography>
      </div>
      <Burger onClick={() => setOpen(!open)} />
      <Drawer open={open} onClose={() => setOpen(false)}>
        <Box sx={{ width: theme.mobileMenuDrawerWidth }}>
          <IconButton
            sx={{
              left: theme.primaryPadding - 8,
              top: theme.primaryPadding - 8,
              padding: '8px',
            }}
            onClick={() => setOpen(false)}
          >
            <CloseIcon fontSize={isTablet ? 'large' : 'medium'} />
          </IconButton>
          <List style={{ marginTop: theme.primaryPadding }}>
            {items.map((item) => (
              <ListItem key={`mobile-menu-item-${item.id}`} disablePadding>
                <ListItemButton
                  disabled={isActive(item.onClickDestination)}
                  onClick={() => {
                    navigate(item.onClickDestination);
                    setOpen(false);
                  }}
                >
                  <ListItemIcon>{item.icon}</ListItemIcon>
                  <ListItemText
                    primary={item.label.toUpperCase()}
                    primaryTypographyProps={{
                      variant: theme.menuTextVariant as any,
                    }}
                  />
                </ListItemButton>
              </ListItem>
            ))}
          </List>
        </Box>
      </Drawer>
    </>
  );
};
