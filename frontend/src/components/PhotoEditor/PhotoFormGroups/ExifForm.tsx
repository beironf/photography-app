import { Camera, Lens } from 'model/camera';
import { Exif } from 'model/exif';
import React from 'react';
import { Row, Form, Col } from 'react-bootstrap';

type Props = {
  exif: Exif;
};

// TODO: can't get return type JSX.Element working
const textForm = (title: string, name: string, placeholder: string, value?: string): any => (
  <Form.Group as={Row}>
    <Form.Label column sm="2">{title}</Form.Label>
    <Col sm="10">
      <Form.Control
        name={name}
        type="text"
        plaintext={value !== undefined}
        disabled={value !== undefined}
        placeholder={value !== undefined ? value : placeholder}
        required
      />
    </Col>
  </Form.Group>
);

export const ExifForm: React.FunctionComponent<Props> = ({ exif }) => (
  <>
    <Form.Group as={Row}>
      <Form.Label column sm="2">
        Camera
      </Form.Label>
      <Col sm="10">
        <Form.Control
          name="camera"
          as="select"
          required
          disabled={exif.camera != null}
        >
          {exif.camera != null && (
          <option key={`camera-${exif.camera}`}>
            {exif.camera}
          </option>
          )}
          {exif.camera == null
            && Object.values(Object.values(Camera))
              .map((camera) => <option key={`camera-${camera}`}>{camera}</option>)}
        </Form.Control>
      </Col>
    </Form.Group>

    <Form.Group as={Row}>
      <Form.Label column sm="2">
        Lens
      </Form.Label>
      <Col sm="10">
        <Form.Control
          name="lens"
          as="select"
          required
          disabled={exif.lens != null}
        >
          {exif.lens != null && (
          <option key={`lens-${exif.lens}`}>
            {exif.lens}
          </option>
          )}
          {exif.lens == null
              && Object.values(Object.values(Lens))
                .map((lens) => <option key={`lens-${lens}`}>{lens}</option>)}
        </Form.Control>
      </Col>
    </Form.Group>

    {textForm('Focal Length', 'focalLength', 'ex: 23mm', exif.focalLength !== undefined
      ? `${exif.focalLength}mm`
      : undefined)}

    {textForm('Aperture', 'aperture', 'ex: f/5.6', exif.fNumber !== undefined
      ? `f/${exif.fNumber}`
      : undefined)}

    {textForm('Exposure Time', 'exposureTime', 'ex: 1/100s', exif.exposureTime !== undefined
      ? `${exif.exposureTime}s`
      : undefined)}

    {textForm('ISO', 'iso', 'ex: ISO 100', exif.iso !== undefined
      ? `ISO ${exif.iso}`
      : undefined)}
  </>
);
