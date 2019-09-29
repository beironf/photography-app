import React, { useState } from "react";
import { FormControl, FormGroup, FormLabel, Form, Button } from "react-bootstrap";
import axios from "axios";

type props = {
  onImageUploaded: (fileName: string) => void;
};

export const ImageUploader: React.FunctionComponent<props> = ({ onImageUploaded }) => {
  const [fileName, setFileName] = useState("");

  const onSubmit = (event: React.FormEvent) => {
    event.preventDefault();
    const formData = new FormData();
    const fileInput = document.querySelector("#file-input") as any;
    if (fileInput) {
      formData.append("image", fileInput.files[0]);
      axios.post("http://localhost:3001/api/images/upload", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
      });
    }
  };

  // TODO: FIX POST... multipart/form-data formidable
  return (
    <Form onSubmit={onSubmit}>
      <FormGroup>
        <FormControl
          id="file-input"
          type="file"
          accept="image/jpeg"
          onChange={(e: any) => {
            setFileName((e.target.files[0]) ? e.target.files[0].name : "");
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
