import React from 'react';
import { Card } from '@mui/material';
import { theme } from 'style/theme';
import { HomeHeader } from './HomeHeader';

export const Home: React.FunctionComponent = () => (
  <>
    <Card sx={{ p: `${theme.primaryPadding}px` }}>
      <HomeHeader />
    </Card>
    <p />
  </>
);
