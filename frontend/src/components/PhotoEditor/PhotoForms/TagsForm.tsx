import { Chip, Grid } from '@mui/material';
import { InputTextField } from 'components/InputTextField';
import React from 'react';

type Props = {
  tags: string[];
  setTags: (_: string[]) => void;
};

export const TagsForm: React.FunctionComponent<Props> = ({
  tags,
  setTags,
}) => {
  const onChange = (tagString: string): void => {
    if (tagString.trim() === '') {
      setTags([]);
    } else {
      const ts = tagString.trim().toLowerCase().split(' ');
      const uniqueTags = [...Array.from(new Set(ts))];
      setTags(uniqueTags);
    }
  };

  return (
    <Grid item>
      <InputTextField
        id="tags"
        label="Tags"
        value={tags.join(' ')}
        onChange={onChange}
        minWidth={400}
      />
      <div>
        {tags && tags.map((tag) => (
          <Chip key={`tag-${tag}`} label={tag} color="secondary" size="small" />
        ))}
      </div>
    </Grid>
  );
};
