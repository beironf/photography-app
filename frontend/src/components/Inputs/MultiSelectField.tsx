import {
  FormControl, InputLabel, MenuItem, OutlinedInput, Select,
} from '@mui/material';
import React from 'react';

type Option = {
  value: string;
  label: string;
};

type Props = {
  id: string;
  label: string;
  options: Option[];
  values: string[];
  onChange: (_: string[]) => void;
};

export const MultiSelectField: React.FunctionComponent<Props> = ({
  id, label, options, values, onChange,
}) => (
  <div>
    <FormControl>
      <InputLabel id={`label-${id}`} shrink>{label}</InputLabel>
      <Select
        labelId={`label-${id}`}
        label={label}
        id={id}
        multiple
        value={values}
        onChange={(e) => onChange(e.target.value as string[])}
        input={<OutlinedInput label={label} notched />}
        size="small"
        style={{ minWidth: 250 }}
      >
        {options.map((option) => (
          <MenuItem
            key={option.value}
            value={option.value}
          >
            {option.label}
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  </div>
);
