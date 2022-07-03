import {
  Chip, FormControlLabel, Grid, Switch,
} from '@mui/material';
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

  const ratingColor = (isHighlighted: boolean): 'default' | 'primary' | 'error' => {
    if (rating !== undefined) return isHighlighted ? 'primary' : 'default';
    return 'error';
  };

  const ratingVariant = (isHighlighted: boolean): 'filled' | 'outlined' => {
    if (rating !== undefined && isHighlighted) return 'filled';
    return 'outlined';
  };

  return (
    <>
      <Grid item>
        <FormControlLabel
          style={{ margin: 0 }}
          control={(
            <div style={{ marginRight: 12, padding: '6px 0' }}>
              {symbols.map((isHighlighted, i) => (
                <Chip
                  onClick={() => setRating(i + 1)}
                  // eslint-disable-next-line react/no-array-index-key
                  key={`tag-${i}`}
                  label={i + 1}
                  color={ratingColor(isHighlighted)}
                  variant={ratingVariant(isHighlighted)}
                  size="small"
                />
              ))}
            </div>
          )}
          label="Rating"
        />
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
};
