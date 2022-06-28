import { Grid } from '@mui/material';
import { InputTextField } from 'components/InputTextField';
import { MultiSelectField } from 'components/MultiSelectField';
import { SelectField } from 'components/SelectField';
import {
  CameraTechnique, PhotoCategory, toCameraTechnique, toCategory,
} from 'model/metadata';
import React from 'react';

type Props = {
  title?: string;
  category?: PhotoCategory;
  cameraTechniques?: CameraTechnique[];
  setTitle: (_: string) => void;
  setCategory: (_: PhotoCategory) => void;
  setCameraTechniques: (_: CameraTechnique[]) => void;
};

export const BasePhotoForm: React.FunctionComponent<Props> = ({
  title, category, cameraTechniques, setTitle, setCategory, setCameraTechniques,
}) => (
  <>
    <Grid item>
      <InputTextField
        id="title"
        label="Title"
        value={title}
        onChange={(s) => setTitle(s)}
        required
      />
    </Grid>

    <Grid item>
      <SelectField
        id="category"
        label="Category"
        options={
              Object.values(PhotoCategory)
                .map((c) => ({ value: c, label: c }))
            }
        value={category}
        onChange={(s) => setCategory(toCategory(s as string))}
        required
      />
    </Grid>

    <Grid item>
      <MultiSelectField
        id="camera-techniques"
        label="Camera Techniques"
        options={
              Object.values(CameraTechnique)
                .map((cT) => ({ value: cT, label: cT }))
            }
        values={cameraTechniques.map((cT) => cT.toString())}
        onChange={(ss) => setCameraTechniques(ss.map((s) => toCameraTechnique(s)))}
      />
    </Grid>
  </>
);
