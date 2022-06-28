import { Chip, Grid } from '@mui/material';
import { InputTextField } from 'components/InputTextField';
import React, { useState, useEffect } from 'react';

type Props = {
  tags: string[];
  setTags: (_: string[]) => void;
};

export const TagsForm: React.FunctionComponent<Props> = ({
  tags,
  setTags,
}) => {
  const [tagsInputValue, setTagsInputValue] = useState<string>('');

  useEffect(() => {
    if (tagsInputValue.trim() === '') {
      setTags(undefined);
    } else {
      const ts = tagsInputValue.trim().toLowerCase().split(' ');
      const uniqueTags = [...Array.from(new Set(ts))];
      setTags(uniqueTags);
    }
  }, [tagsInputValue, setTags]);

  return (
    <Grid item>
      <InputTextField
        id="tags"
        label="Tags"
        value={tagsInputValue}
        onChange={(s) => setTagsInputValue(s)}
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
