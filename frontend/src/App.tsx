import { createTheme, ThemeProvider } from '@mui/material';
import { PageLayout } from 'views/PageLayout';
import React from 'react';
import { ManagePhotos } from 'views/ManagePhotos';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Gallery } from 'views/Gallery';
import { Showroom } from 'views/Showroom';

const darkTheme = createTheme({
  palette: {
    mode: 'dark',
  },
});

const App: React.FC = () => {
  const body = (
    <Routes>
      <Route path="/" element={<Showroom />} />
      <Route path="/gallery" element={<Gallery />} />
      <Route path="/manage-photos" element={<ManagePhotos />} />
    </Routes>
  );

  return (
    <ThemeProvider theme={darkTheme}>
      <BrowserRouter>
        <PageLayout
          body={body}
        />
      </BrowserRouter>
    </ThemeProvider>
  );
};

export default App;
