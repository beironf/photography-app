import React, { useState } from 'react';
import { Row, Col, Form, Badge } from 'react-bootstrap';
import { toCamelCase } from 'util/string-id-utils';

type Props = {
  name: string;
  maxValue: number;
};

export const RatingForm: React.FunctionComponent<Props> = ({
  name,
  maxValue,
}) => {
  const [rating, setRating] = useState<number>();
  const symbols = [];
  for (let i = 1; i <= maxValue; i++) {
    symbols.push(rating ? i <= rating : false);
  }

  return (
    <>
      <Form.Group as={Row}>
        <Form.Label column sm="2">
          {name}
        </Form.Label>
        <Col sm="10">
          <Form.Control
            name={toCamelCase(name)}
            type="text"
            plaintext
            disabled
            required
            value={rating ? rating.toString() : undefined}
          />
          <div>
            {symbols.map((isHighlighted, i) => (
              <Badge
                onClick={() => setRating(i + 1)}
                key={'tag-' + i}
                pill
                variant={isHighlighted ? 'primary' : 'secondary'}
              >
                {i + 1}
              </Badge>
            ))}
          </div>
        </Col>
      </Form.Group>
    </>
  );
};
