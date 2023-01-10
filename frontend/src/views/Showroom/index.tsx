import React from 'react';
import { Card } from '@mui/material';
import { theme } from 'style/theme';

export const Showroom: React.FunctionComponent = () => (
  <Card elevation={0} sx={{ p: `${theme.primaryPadding}px` }}>
    Showroom
  </Card>
);
