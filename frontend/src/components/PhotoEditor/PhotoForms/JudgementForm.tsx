import {
  Chip, FormControlLabel, Grid, Switch,
} from '@mui/material';
import { InputTextField } from 'components/InputTextField';
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
}) => {
  const symbols: boolean[] = [];
  for (let i = 1; i <= 5; i += 1) {
    symbols.push(rating ? i <= rating : false);
  }

  return (
    <>
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
      <Grid item>
        <InputTextField
          id="rating"
          label="Rating"
          value={rating ? rating.toString() : undefined}
          onChange={() => 1}
          required
          disabled
          minWidth={150}
        />
        <div>
          {symbols.map((isHighlighted, i) => (
            <Chip
              onClick={() => setRating(i + 1)}
              // eslint-disable-next-line react/no-array-index-key
              key={`tag-${i}`}
              label={i + 1}
              color={isHighlighted ? 'secondary' : undefined}
              size="small"
            />
          ))}
        </div>
      </Grid>
    </>
  );
};
