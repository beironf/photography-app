import { Chip, Grid } from '@mui/material';
import { InputTextField } from 'components/InputTextField';
import React from 'react';

type Props = {
  rating?: number;
  setRating: (_: number) => void;
};

export const RatingForm: React.FunctionComponent<Props> = ({
  rating,
  setRating,
}) => {
  const symbols: boolean[] = [];
  for (let i = 1; i <= 5; i += 1) {
    symbols.push(rating ? i <= rating : false);
  }

  return (
    <Grid item>
      <InputTextField
        id="rating"
        label="Rating"
        value={rating ? rating.toString() : undefined}
        onChange={() => 1}
        required
        disabled
        variant="standard"
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
  );
};
