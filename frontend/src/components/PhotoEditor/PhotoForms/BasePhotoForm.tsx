import { Grid } from '@mui/material';
import { InputTextField } from 'components/Inputs/InputTextField';
import { MultiSelectField } from 'components/Inputs/MultiSelectField';
import { SelectField } from 'components/Inputs/SelectField';
import {
  CameraTechnique, PhotoCategory, toCameraTechnique, toCategory,
} from 'model/metadata';
import React from 'react';

type Props = {
  title?: string;
  category?: PhotoCategory;
  group?: string;
  cameraTechniques?: CameraTechnique[];
  setTitle: (_: string) => void;
  setCategory: (_: PhotoCategory) => void;
  setGroup: (_: string) => void;
  setCameraTechniques: (_: CameraTechnique[]) => void;
};

export const BasePhotoForm: React.FunctionComponent<Props> = ({
  title, category, group, cameraTechniques, setTitle, setCategory, setGroup, setCameraTechniques,
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
      <InputTextField
        id="group"
        label="Group"
        value={group}
        onChange={(s) => {
          if (s.trim() === '') {
            setGroup(undefined);
          } else {
            setGroup(s);
          }
        }}
        error={group !== undefined && group.trim() === ''}
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
