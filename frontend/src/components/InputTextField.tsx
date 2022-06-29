import { TextField } from '@mui/material';
import React from 'react';

type Props = {
  id: string;
  label: string;
  onChange: (_: string) => void;
  value?: string;
  helperText?: string;
  required?: boolean;
  error?: boolean;
  disabled?: boolean;
  variant?: 'filled' | 'outlined' | 'standard';
  minWidth?: number;
};

export const InputTextField: React.FunctionComponent<Props> = ({
  id, label, onChange, value, helperText, disabled, required, error, variant, minWidth,
}) => (
  <TextField
    id={id}
    label={label}
    value={value || ''}
    error={error ?? (required && (value === undefined || value === ''))}
    required={required}
    onChange={(e) => onChange(e.target.value)}
    disabled={disabled}
    helperText={(value === undefined || value === '') ? helperText : undefined}
    InputLabelProps={{ shrink: true }}
    size="small"
    style={{ minWidth: minWidth ?? 200 }}
    variant={variant}
  />
);
