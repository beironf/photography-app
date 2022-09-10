/* eslint-disable no-unused-vars */
import { Grid } from '@mui/material';
import { PhotoApi } from 'api/PhotoApi';
import { DataFetcher } from 'components/DataFetcher';
import { NumberRatingField } from 'components/Inputs/NumberRatingField';
import { SelectField } from 'components/Inputs/SelectField';
import { PhotoCategory, toCategory } from 'model/metadata';
import React, { useCallback } from 'react';
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
  const listPhotoGroups = useCallback(
    () => PhotoApi.listPhotoGroups(),
    [],
  );

  return (
    <DataFetcher apiMethod={listPhotoGroups} errorText="Groups could not be found">
      {(groups) => (
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
            <SelectField
              id="category-filter"
              label="Category"
              options={
                Object.values(PhotoCategory)
                  .map((c) => ({ value: c, label: c }))
              }
              value={category}
              onChange={(s) => setCategory(toCategory(s as string))}
            />
          </Grid>
          <Grid item>
            <SelectField
              id="group-filter"
              label="Group"
              options={groups.map((g) => ({ value: g, label: g }))}
              value={group}
              onChange={(s) => setGroup(s)}
            />
          </Grid>
          <Grid item>
            <NumberRatingField
              id="rating-filter"
              label="Rating"
              rating={rating}
              setRating={setRating}
            />
          </Grid>
        </Grid>
      )}
    </DataFetcher>
  );
};
