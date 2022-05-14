import { PhotoExif } from 'model/photo-exif';
import React from 'react';
import { Row, Form, Col } from 'react-bootstrap';
import { formatDate } from 'util/date-util';

type Props = {
  exif: PhotoExif;
};

export const DateForm: React.FunctionComponent<Props> = ({ exif }) => (
  <Form.Group as={Row}>
    <Form.Label column sm="2">
      Date
    </Form.Label>
    <Col sm="10">
      <Form.Control
        name="date"
        type="text"
        plaintext={exif.date !== undefined}
        disabled={exif.date !== undefined}
        placeholder={
          exif.date ? formatDate(exif.date) : 'YYYY-MM-DD HH:MM:SS'
        }
        required
      />
    </Col>
  </Form.Group>
);
