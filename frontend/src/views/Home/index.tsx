import React from 'react';
import { ImageApi } from 'api/ImageApi';
import { Card, Grid, Typography } from '@mui/material';
import { theme } from 'style/theme';

export const Home: React.FunctionComponent = () => (
  <>
    <Card sx={{ p: `${theme.primaryPadding}px` }}>
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
            width="80px"
          />
        </Grid>
        <Grid item>
          <Typography variant="h4" align="center">
            Photography
          </Typography>
        </Grid>
      </Grid>
    </Card>
    <p />
  </>
);
