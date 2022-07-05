import { Divider, Typography } from '@mui/material';
import { ImageUploader } from 'components/ImageUploader';
import React from 'react';
import { theme } from 'style/theme';

type props = {
  setImageUploaded: (_: string) => void;
};

export const ManagePhotosMenu: React.FunctionComponent<props> = ({
  setImageUploaded,
}) => (
  <>
    <ImageUploader
      onImageUploaded={setImageUploaded}
    />
    <Divider variant="middle" sx={{ width: '60%', margin: `${theme.primaryPadding}px 0` }} />
    <Typography variant="h6">
      Select an image in order to start editing.
    </Typography>
  </>
);
