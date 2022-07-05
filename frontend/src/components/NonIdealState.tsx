import React from 'react';
import { Grid, Typography } from '@mui/material';
import { theme } from 'style/theme';

interface INonIdealStateProps {
  action?: JSX.Element;
  children?: React.ReactNode;
  description?: React.ReactChild;
  icon?: React.ReactNode;
  title?: React.ReactNode;
}

export const NonIdealState: React.FC<INonIdealStateProps> = ({
  action,
  children,
  description,
  icon,
  title,
}) => {
  const maybeRenderVisual = (): JSX.Element => {
    if (icon === null || icon === undefined) {
      return null;
    }
    return <Grid item style={{ marginTop: theme.primaryPadding }}>{icon}</Grid>;
  };

  return (
    <Grid
      container
      justifyContent="center"
      direction="column"
      alignItems="center"
      spacing={2}
    >
      {maybeRenderVisual()}
      {title && (
        <Grid item>
          <Typography variant="h6">{title}</Typography>
          {' '}
        </Grid>
      )}
      {description && <Grid item>{description}</Grid>}
      <Grid item>{action}</Grid>
      {children}
    </Grid>
  );
};
