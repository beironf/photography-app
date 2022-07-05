import { createTheme, ThemeProvider } from '@mui/material';
import { PageLayout } from 'views/PageLayout';
import React from 'react';
import { ManagePhotos } from 'views/ManagePhotos';

const darkTheme = createTheme({
  palette: {
    mode: 'dark',
  },
});

const App: React.FC = () => (
  <ThemeProvider theme={darkTheme}>
    <PageLayout
      body={<ManagePhotos />}
    />
  </ThemeProvider>
);

export default App;
