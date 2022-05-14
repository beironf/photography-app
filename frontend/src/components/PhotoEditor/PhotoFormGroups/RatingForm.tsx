import React, { useState } from 'react';
import {
  Row, Col, Form, Badge,
} from 'react-bootstrap';
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
  const symbols: boolean[] = [];
  for (let i = 1; i <= maxValue; i += 1) {
    symbols.push(rating ? i <= rating : false);
  }

  return (
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
              // eslint-disable-next-line react/no-array-index-key
              key={`tag-${i}`}
              pill
              bg={isHighlighted ? 'primary' : 'secondary'}
            >
              {i + 1}
            </Badge>
          ))}
        </div>
      </Col>
    </Form.Group>
  );
};
