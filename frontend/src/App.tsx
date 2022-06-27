import { PageLayout } from 'components/PageLayout';
import React from 'react';
import { ManagePhotos } from 'views/ManagePhotos';

const App: React.FC = () => (
  <PageLayout
    body={<ManagePhotos />}
  />
);

export default App;
