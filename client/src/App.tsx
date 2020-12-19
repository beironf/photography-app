import React from "react";
import { PhotoForm } from "./components/PhotoEditor/PhotoForm";

const App: React.FC = () => {
  return <PhotoForm onSubmit={() => 1} />;
};

export default App;
