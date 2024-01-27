import { Chip, FormControlLabel } from '@mui/material';
import React from 'react';

type Props = {
  id: string;
  label: string;
  rating?: number;
  required?: boolean;
  setRating: (_: number) => void;
};

export const NumberRatingField: React.FunctionComponent<Props> = ({
  id,
  label,
  rating,
  required,
  setRating,
}) => {
  const symbols: boolean[] = [];
  for (let i = 1; i <= 5; i += 1) {
    symbols.push(rating ? i <= rating : false);
  }

  const ratingColor = (
    isHighlighted: boolean,
  ): 'default' | 'primary' | 'error' => {
    if (rating !== undefined) return isHighlighted ? 'primary' : 'default';
    return required ? 'error' : 'default';
  };

  const ratingVariant = (isHighlighted: boolean): 'filled' | 'outlined' => {
    if (rating !== undefined && isHighlighted) return 'filled';
    return 'outlined';
  };

  return (
    <FormControlLabel
      id={id}
      style={{ margin: 0 }}
      control={
        <div style={{ marginRight: 12, padding: '6px 0' }}>
          {symbols.map((isHighlighted, i) => (
            <Chip
              style={{
                margin:
                  ratingVariant(isHighlighted) === 'filled'
                    ? '0 1px'
                    : undefined,
              }}
              onClick={() =>
                rating !== i + 1 ? setRating(i + 1) : setRating(undefined)
              }
              // eslint-disable-next-line react/no-array-index-key
              key={`tag-${i}`}
              label={i + 1}
              color={ratingColor(isHighlighted)}
              variant={ratingVariant(isHighlighted)}
              size="small"
            />
          ))}
        </div>
      }
      label={label}
    />
  );
};
