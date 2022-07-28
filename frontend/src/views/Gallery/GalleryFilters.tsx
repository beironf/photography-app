/* eslint-disable no-unused-vars */
import { Grid } from '@mui/material';
import { PhotoCategory } from 'model/metadata';
import React from 'react';
import { theme } from 'style/theme';

type props = {
  active: boolean;
  category?: PhotoCategory;
  group?: string;
  rating?: number;
  setCategory: (_?: PhotoCategory) => void;
  setGroup: (_?: string) => void;
  setRating: (_?: number) => void;
};

export const GalleryFilters: React.FunctionComponent<props> = ({
  active,
  category,
  group,
  rating,
  setCategory,
  setGroup,
  setRating,
}) => {
  // TODO: fetch from API
  const groups = ['USA'];

  return (
    <Grid
      container
      columnGap={`${theme.primaryPadding}px`}
      justifyContent="center"
      sx={{
        opacity: active ? 1 : 0,
        height: active ? 'auto' : 0,
        transition: 'opacity 1s ease-out',
        overflow: active ? undefined : 'hidden',
        mt: active ? `${theme.secondaryPadding}px` : 0,
      }}
    >
      <Grid item>
        Category Filter
      </Grid>
      <Grid item>
        Group Filter
      </Grid>
      <Grid item>
        Rating Filter
      </Grid>
    </Grid>
  );
};
