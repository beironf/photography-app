import { createTheme, ThemeProvider } from '@mui/material';
import { PageLayout } from 'views/PageLayout';
import React from 'react';
import { ManagePhotos } from 'views/ManagePhotos';
import {
  BrowserRouter, Routes, Route, Navigate,
} from 'react-router-dom';
import { Gallery } from 'views/Gallery';
import { Home } from 'views/Home';
import { PasswordContextProvider } from 'contexts/PasswordContext';
import { PasswordRestricted } from 'components/PasswordRestricted';

const darkTheme = createTheme({
  palette: {
    mode: 'dark',
  },
});

const App: React.FC = () => {
  const body = (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/gallery" element={<Gallery />} />
      <Route path="/gallery/:imageId" element={<Gallery />} />
      <Route
        path="/manage-photos"
        element={(
          <PasswordRestricted>
            <ManagePhotos />
          </PasswordRestricted>
        )}
      />
      <Route path="*" element={<Navigate replace to="/" />} />
    </Routes>
  );

  return (
    <ThemeProvider theme={darkTheme}>
      <BrowserRouter>
        <PasswordContextProvider>
          <PageLayout
            body={body}
          />
        </PasswordContextProvider>
      </BrowserRouter>
    </ThemeProvider>
  );
};

export default App;
