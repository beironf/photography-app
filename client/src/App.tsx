import React from "react";
import { PhotoForm } from "views/PhotoForm";

const App: React.FC = () => {
  return <PhotoForm onSubmit={() => 1} />;
};

export default App;
