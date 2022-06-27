import React, { useState } from 'react';
import DeleteIcon from '@mui/icons-material/Delete';
import {
  Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle,
} from '@mui/material';
import { ImageApi } from 'api/ImageApi';

type props = {
  imageId: string;
  onImageRemoved: (_: string) => void;
};

export const ImageRemover: React.FunctionComponent<props> = ({ imageId, onImageRemoved }) => {
  const [showConfirm, setShowConfirm] = useState(false);

  const removeImage = (): void => {
    ImageApi.ImageRoute.removeImage(imageId)
      .then((_: any) => {
        onImageRemoved(imageId);
      });
  };

  return (
    <>
      {!showConfirm && (
        <Button
          startIcon={<DeleteIcon />}
          variant="contained"
          component="span"
          color="error"
          onClick={() => setShowConfirm(true)}
        >
          Remove
        </Button>
      )}
      {showConfirm && (
        <Dialog
          open={showConfirm}
          onClose={() => setShowConfirm(false)}
        >
          <DialogTitle>
            Remove Image?
          </DialogTitle>
          <DialogContent>
            <DialogContentText>
              {`Image with file name ${imageId} will be removed.`}
            </DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setShowConfirm(false)}>Cancel</Button>
            <Button onClick={removeImage} autoFocus>Remove</Button>
          </DialogActions>
        </Dialog>
      )}
    </>
  );
};
