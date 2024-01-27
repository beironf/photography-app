import { Grid, TextField } from '@mui/material';
import { DateTimePicker, LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterMoment } from '@mui/x-date-pickers/AdapterMoment';
import { InputTextField } from 'components/Inputs/InputTextField';
import { SelectField } from 'components/Inputs/SelectField';
import { Camera, Lens, toCamera, toLens } from 'model/camera';
import { Exif } from 'model/exif';
import React, { useEffect } from 'react';

type Props = {
  exif: Exif;
  value: {
    camera?: Camera;
    lens?: Lens;
    focalLength?: string;
    aperture?: string;
    iso?: string;
    exposureTime?: string;
    date?: Date;
  };
  setCamera: (_: Camera) => void;
  setLens: (_: Lens) => void;
  setFocalLength: (_: string) => void;
  setAperture: (_: string) => void;
  setExposureTime: (_: string) => void;
  setIso: (_: string) => void;
  setDate: (_: Date) => void;
};

const formatFocalLength = (fL: number): string => `${fL}mm`;
const formatAperture = (a: number): string => `f/${a}`;
const formatExposureTime = (eT: string): string => `${eT}s`;
const formatIso = (i: number): string => `ISO ${i}`;

export const ExifForm: React.FunctionComponent<Props> = ({
  exif,
  value: { camera, lens, focalLength, aperture, exposureTime, iso, date },
  setCamera,
  setLens,
  setFocalLength,
  setAperture,
  setExposureTime,
  setIso,
  setDate,
}) => {
  // On exif loaded - override state
  useEffect(() => {
    if (exif.camera !== undefined) setCamera(exif.camera);
    if (exif.lens !== undefined) setLens(exif.lens);
    if (exif.focalLength !== undefined)
      setFocalLength(formatFocalLength(exif.focalLength));
    if (exif.fNumber !== undefined) setAperture(formatAperture(exif.fNumber));
    if (exif.exposureTime !== undefined)
      setExposureTime(formatExposureTime(exif.exposureTime));
    if (exif.iso !== undefined) setIso(formatIso(exif.iso));
    if (exif.date !== undefined) setDate(exif.date);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [exif]);

  return (
    <>
      <Grid item>
        <SelectField
          id="camera"
          label="Camera"
          onChange={(s) => setCamera(toCamera(s))}
          disabled={exif.camera !== undefined}
          required
          options={Object.values(Object.values(Camera)).map((cam) => ({
            value: cam.toString(),
            label: cam.toString(),
          }))}
          value={camera !== undefined ? camera.toString() : undefined}
        />
      </Grid>

      <Grid item>
        <SelectField
          id="lens"
          label="Lens"
          onChange={(s) => setLens(toLens(s))}
          disabled={exif.lens !== undefined}
          required
          options={Object.values(Object.values(Lens)).map((l) => ({
            value: l.toString(),
            label: l.toString(),
          }))}
          value={lens !== undefined ? lens.toString() : undefined}
        />
      </Grid>

      <Grid item>
        <InputTextField
          id="focalLength"
          label="Focal Length"
          onChange={(s) => setFocalLength(s)}
          disabled={exif.focalLength !== undefined}
          value={focalLength}
          helperText="ex: 23mm"
          required
        />
      </Grid>

      <Grid item>
        <InputTextField
          id="aperture"
          label="Aperture"
          onChange={(s) => setAperture(s)}
          disabled={exif.fNumber !== undefined}
          value={aperture}
          helperText="ex: f/5.6"
          required
        />
      </Grid>

      <Grid item>
        <InputTextField
          id="exposureTime"
          label="Exposure Time"
          onChange={(s) => setExposureTime(s)}
          disabled={exif.exposureTime !== undefined}
          value={exposureTime}
          helperText="ex: 1/100s"
          required
        />
      </Grid>

      <Grid item>
        <InputTextField
          id="iso"
          label="ISO"
          onChange={(s) => setIso(s)}
          disabled={exif.iso !== undefined}
          value={iso}
          helperText="ex: ISO 100"
          required
        />
      </Grid>

      <Grid item>
        <LocalizationProvider dateAdapter={AdapterMoment}>
          <DateTimePicker
            label="Date and Time Taken"
            value={date}
            onChange={(d) => setDate(d)}
            disabled={exif.date !== undefined}
            renderInput={(params) => (
              <TextField
                size="small"
                disabled
                // eslint-disable-next-line react/jsx-props-no-spreading
                {...params}
              />
            )}
          />
        </LocalizationProvider>
      </Grid>
    </>
  );
};
