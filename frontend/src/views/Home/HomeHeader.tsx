import React from 'react';
import { ImageApi } from 'api/ImageApi';
import { Grid, Typography } from '@mui/material';
import { theme } from 'style/theme';

export const HomeHeader: React.FunctionComponent = () => (
  <Grid
    container
    columnGap={`${theme.primaryPadding}px`}
    justifyContent="center"
    alignItems="center"
  >
    <Grid item>
      <Typography variant="h4" align="center">
        Fredrik Beiron
      </Typography>
    </Grid>
    <Grid item>
      <img
        src={ImageApi.SiteImageRoute.getSiteImageUrl('logo.png')}
        alt="Icon"
        width="75px"
      />
    </Grid>
    <Grid item>
      <Typography variant="h4" align="center">
        Photography
      </Typography>
    </Grid>
  </Grid>
);
