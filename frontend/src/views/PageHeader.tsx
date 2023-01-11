import React from 'react';
import { ImageApi } from 'api/ImageApi';
import { Grid, Typography } from '@mui/material';
import { theme } from 'style/theme';
import { useNavigate } from 'react-router-dom';

type Props = {
  hidden?: boolean;
};

export const PageHeader: React.FunctionComponent<Props> = ({ hidden }) => {
  const navigate = useNavigate();

  return (
    <div
      style={{ margin: theme.primaryPadding }}
      hidden={hidden}
      role="button"
      onClick={() => navigate('/')}
      onKeyUp={() => navigate('/')}
      tabIndex={0}
    >
      <Grid
        container
        columnGap={`${theme.primaryPadding}px`}
        justifyContent="center"
        alignItems="center"
      >
        <Grid item>
          <Typography variant={theme.headerHSize as any} align="center">
            Fredrik Beiron
          </Typography>
        </Grid>
        <Grid item>
          <img
            src={ImageApi.SiteImageRoute.getSiteImageUrl('logo.png')}
            alt="Icon"
            width={`${theme.logoSize}px`}
          />
        </Grid>
        <Grid item>
          <Typography variant={theme.headerHSize as any} align="center">
            Photography
          </Typography>
        </Grid>
      </Grid>
    </div>
  );
};
