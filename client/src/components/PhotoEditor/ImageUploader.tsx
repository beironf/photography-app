import React, { useState } from "react";
import { FormControl, FormGroup, Form, Button } from "react-bootstrap";
import axios from "axios";

type props = {
  onImageUploaded: (filename: string) => void;
};

export const ImageUploader: React.FunctionComponent<props> = ({
  onImageUploaded,
}) => {
  const [fileName, setFileName] = useState("");

  const onSubmit = (event: React.FormEvent) => {
    event.preventDefault();
    const formData = new FormData();
    const fileInput = document.querySelector("#file-input") as any;
    if (fileInput && fileInput.files[0]) {
      formData.append("image", fileInput.files[0]);
      axios
        .post("http://localhost:3001/api/images/upload", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then((response: any) => {
          onImageUploaded(fileInput.files[0].name);
        });
    }
  };

  return (
    <Form onSubmit={onSubmit}>
      <FormGroup>
        <FormControl
          id="file-input"
          type="file"
          accept="image/jpeg"
          onChange={(e: any) => {
            setFileName(e.target.files[0] ? e.target.files[0].name : "");
          }}
        />
      </FormGroup>
      {fileName !== "" && (
        <Button variant="primary" type="submit">
          Upload
        </Button>
      )}
    </Form>
  );
};
