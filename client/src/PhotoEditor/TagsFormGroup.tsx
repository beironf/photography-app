import React, { useState, useEffect } from "react";
import { Row, Col, Form, Badge } from "react-bootstrap";

type Props = {
  name: string;
  placeholder: string;
};

export const TagsFormGroup: React.FunctionComponent<Props> = ({
  name,
  placeholder,
}) => {
  const [tagsInputValue, setTagsInputValue] = useState<string>("");
  const [tags, setTags] = useState<string[]>([]);

  useEffect(() => {
    if (tagsInputValue.trim() === "") {
      setTags([]);
    } else {
      setTags(tagsInputValue.trim().toLowerCase().split(" "));
    }
  }, [tagsInputValue]);

  return (
    <div>
      <Form.Group as={Row}>
        <Form.Label column sm="2">
          {name}
        </Form.Label>
        <Col sm="10">
          <Form.Control
            type="text"
            placeholder={placeholder}
            onChange={(e: any) => setTagsInputValue(e.target.value)}
          />
          <div>
            {tags &&
              tags.map((tag, index) => (
                <Badge key={"tag-" + index} pill variant="primary">
                  {tag}
                </Badge>
              ))}
          </div>
        </Col>
      </Form.Group>
    </div>
  );
};
