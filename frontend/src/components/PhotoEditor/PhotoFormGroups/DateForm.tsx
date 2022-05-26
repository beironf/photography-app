import React from 'react';
import { Row, Form, Col } from 'react-bootstrap';
import { formatDate } from 'util/date-util';

type Props = {
  date?: Date;
};

export const DateForm: React.FunctionComponent<Props> = ({ date }) => (
  <Form.Group as={Row}>
    <Form.Label column sm="2">
      Date
    </Form.Label>
    <Col sm="10">
      <Form.Control
        name="date"
        type="text"
        plaintext={date !== undefined}
        disabled={date !== undefined}
        placeholder={
          date ? formatDate(date) : 'yyyy-MM-dd HH:mm:ss'
        }
        required
      />
    </Col>
  </Form.Group>
);
