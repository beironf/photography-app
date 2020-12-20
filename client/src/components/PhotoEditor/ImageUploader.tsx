import React, { useState } from "react";
import { FormControl, FormGroup, Form, Button } from "react-bootstrap";
import { StorageApi } from "../../api/StorageApi";

const storageApi = new StorageApi();

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
      const file = fileInput.files[0];
      formData.append("image", file);
      storageApi.uploadImage(formData, () => onImageUploaded(file.name));
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
