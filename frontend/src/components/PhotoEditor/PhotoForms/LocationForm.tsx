import { Grid } from '@mui/material';
import { InputTextField } from 'components/InputTextField';
import React from 'react';

type Props = {
  location?: string;
  country?: string;
  coordinates?: number[]; // [lat, long]
  setLocation: (_: string) => void;
  setCountry: (_: string) => void;
};

const round = (x: number, decimals: number): number => (
  Math.round(x * 10 ** decimals) / 10 ** decimals
);

const formatCoordinates = (coord: number[]): string => (
  `Latitude: ${round(coord[0], 3)}, Longitude: ${round(coord[1], 3)}`
);

export const LocationForm: React.FunctionComponent<Props> = ({
  location, country, coordinates, setLocation, setCountry,
}) => (
  <>
    <Grid item>
      <InputTextField
        id="location"
        label="Location"
        onChange={(s) => setLocation(s)}
        value={location}
        helperText="Name of Location"
        required
      />
    </Grid>

    <Grid item>
      <InputTextField
        id="country"
        label="Country"
        onChange={(s) => setCountry(s)}
        value={country}
        helperText="Country"
        required
      />
    </Grid>

    <Grid item>
      <InputTextField
        id="coordinates"
        label="Coordinates"
        onChange={() => 1}
        value={coordinates !== undefined ? formatCoordinates(coordinates) : undefined}
        helperText={coordinates === undefined ? 'Click on the map to set coordinates' : undefined}
        required
        disabled
        variant="standard"
        minWidth={300}
      />
    </Grid>
  </>
);
