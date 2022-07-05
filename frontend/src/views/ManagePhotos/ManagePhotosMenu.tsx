import {
  Divider, FormControlLabel, Switch, Typography,
} from '@mui/material';
import { ImageUploader } from 'components/ImageUploader';
import React from 'react';
import { theme } from 'style/theme';

type props = {
  onlyUnfinished: boolean;
  setOnlyUnfinished: (_: boolean) => void;
  setImageUploaded: (_: string) => void;
};

export const ManagePhotosMenu: React.FunctionComponent<props> = ({
  onlyUnfinished, setOnlyUnfinished, setImageUploaded,
}) => (
  <>
    <ImageUploader
      onImageUploaded={setImageUploaded}
    />
    <Divider variant="middle" sx={{ width: '50%', margin: `${theme.primaryPadding}px 0` }} />
    <Typography variant="h6">
      Select an image to edit.
    </Typography>
    <FormControlLabel
      control={(
        <Switch
          checked={onlyUnfinished}
          onChange={(event) => setOnlyUnfinished(event.target.checked)}
        />
      )}
      label="Only Unfinished"
    />
  </>
);
