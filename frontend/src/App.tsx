import { createTheme, ThemeProvider } from '@mui/material';
import '@fontsource/raleway';
import { PageLayout } from 'views/PageLayout';
import React from 'react';
import { ManagePhotos } from 'views/ManagePhotos';
import {
  BrowserRouter, Routes, Route, Navigate,
} from 'react-router-dom';
import { Gallery } from 'views/Gallery';
import { Showroom } from 'views/Showroom';
import { PasswordContextProvider } from 'contexts/PasswordContext';
import { PasswordRestricted } from 'components/PasswordRestricted';
import { theme } from 'style/theme';

const darkTheme = createTheme({
  typography: {
    fontFamily: "'Raleway', sans-serif",
    h4: { fontWeight: 'lighter' },
    h5: { fontWeight: 'lighter' },
    h6: { fontWeight: 'lighter' },
    subtitle1: { fontWeight: 'lighter' },
    subtitle2: { fontWeight: 'lighter' },
    body1: { fontWeight: 'lighter' },
    body2: { fontWeight: 'lighter' },
  },
  palette: {
    mode: theme.mode,
  },
});

const App: React.FC = () => {
  const body = (
    <Routes>
      <Route path="/" element={<Showroom />} />
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
