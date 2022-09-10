import {
  FormControlLabel, Grid, Switch,
} from '@mui/material';
import { NumberRatingField } from 'components/Inputs/NumberRatingField';
import React from 'react';

type Props = {
  rating?: number;
  inShowroom: boolean;
  setRating: (_: number) => void;
  setInShowroom: (_: boolean) => void;
};

export const JudgementForm: React.FunctionComponent<Props> = ({
  rating,
  inShowroom,
  setRating,
  setInShowroom,
}) => (
  <>
    <Grid item>
      <NumberRatingField id="rating" label="Rating" rating={rating} setRating={setRating} />
    </Grid>
    <Grid item>
      <FormControlLabel
        control={(
          <Switch
            checked={inShowroom}
            onChange={(event) => setInShowroom(event.target.checked)}
          />
        )}
        label="In Showroom"
      />
    </Grid>
  </>
);
