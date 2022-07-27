import {
  Box, Drawer, IconButton, List, ListItem, ListItemButton, ListItemIcon, ListItemText,
} from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import React, { useState } from 'react';
import { theme } from 'style/theme';
import { Burger } from './Burger';

export type MenuItem = {
  id: string;
  label: string;
  icon: JSX.Element;
  onClick: () => void;
};

type Props = {
  items: MenuItem[];
};

export const Menu: React.FunctionComponent<Props> = ({ items }) => {
  const [open, setOpen] = useState(false);

  return (
    <>
      <Burger onClick={() => setOpen(!open)} />
      <Drawer open={open} onClose={() => setOpen(false)}>
        <Box
          sx={{ width: theme.menuWidth }}
        >
          <IconButton
            sx={{
              left: theme.primaryPadding - 8,
              top: theme.primaryPadding - 8,
              padding: '8px',
            }}
            onClick={() => setOpen(false)}
          >
            <CloseIcon fontSize="large" />
          </IconButton>
          <List style={{ marginTop: theme.primaryPadding }}>
            {items.map((item) => (
              <ListItem key={item.id} disablePadding>
                <ListItemButton onClick={() => {
                  item.onClick();
                  setOpen(false);
                }}
                >
                  <ListItemIcon>
                    {item.icon}
                  </ListItemIcon>
                  <ListItemText primary={item.label} />
                </ListItemButton>
              </ListItem>
            ))}
          </List>
        </Box>
      </Drawer>
    </>
  );
};
