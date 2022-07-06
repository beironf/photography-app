import { MenuItem, TextField } from '@mui/material';
import React from 'react';

type Option = {
  value: string;
  label: string;
};

type Props = {
  id: string;
  label: string;
  options: Option[];
  onChange: (_: string) => void;
  value?: string;
  helperText?: string;
  required?: boolean;
  disabled?: boolean;
};

export const SelectField: React.FunctionComponent<Props> = ({
  id, label, options, onChange, value, helperText, required, disabled,
}) => (
  <TextField
    id={id}
    label={label}
    value={value || ''}
    error={value === undefined || value === ''}
    required={required}
    onChange={(e) => onChange(e.target.value)}
    disabled={disabled}
    helperText={(value === undefined || value === '') ? helperText : undefined}
    select
    InputLabelProps={{ shrink: true }}
    size="small"
    style={{ minWidth: 200 }}
  >
    {options.map((option) => (
      <MenuItem key={option.value} value={option.value}>
        {option.label}
      </MenuItem>
    ))}
  </TextField>
);
