import {
  CircularProgress,
  Divider, FormControlLabel, Switch, Typography,
} from '@mui/material';
import { ImageUploader } from 'components/Image/ImageUploader';
import { NonIdealState } from 'components/NonIdealState';
import React, { useState } from 'react';
import { theme } from 'style/theme';

type props = {
  onlyUnfinished: boolean;
  setOnlyUnfinished: (_: boolean) => void;
  setImageUploaded: (_: string) => void;
};

export const ManagePhotosMenu: React.FunctionComponent<props> = ({
  onlyUnfinished, setOnlyUnfinished, setImageUploaded,
}) => {
  const [imageIsUploading, setImageIsUploading] = useState(false);

  return (
    <>
      {imageIsUploading && (
        <NonIdealState description="Uploading image" icon={<CircularProgress />} />
      )}
      {!imageIsUploading && (
        <>
          <ImageUploader
            onImageUploaded={(fileName) => {
              setImageUploaded(fileName);
              setImageIsUploading(false);
            }}
            onImageUploading={() => setImageIsUploading(true)}
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
      )}
    </>
  );
};
