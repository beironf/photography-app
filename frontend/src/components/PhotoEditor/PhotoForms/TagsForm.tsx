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
  const toTags = (tagString: string): string[] => {
    const ts = tagString.toLowerCase().split(' ').map((_) => _.trim());
    return [...Array.from(new Set(ts))];
  };

  const onChange = (tagString: string): void => {
    if (tagString.trim() === '') {
      setTags([]);
    } else {
      setTags(toTags(tagString));
    }
  };

  const removeTag = (tag: string): void => {
    const tagsWithoutTag = tags.filter((_) => _ !== tag);
    setTags(tagsWithoutTag);
  };

  return (
    <Grid item xs={12}>
      <InputTextField
        id="tags"
        label="Tags"
        value={tags.join(' ')}
        onChange={onChange}
        fullWidth
        error={tags.find((_) => _.trim() === '') !== undefined}
      />
      <div>
        {tags && tags.map((tag) => (
          <Chip
            key={`tag-${tag}`}
            label={tag}
            color="secondary"
            size="small"
            sx={{ marginLeft: '2px' }}
            onClick={() => removeTag(tag)}
          />
        ))}
      </div>
    </Grid>
  );
};
