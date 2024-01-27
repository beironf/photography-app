import React from 'react';
import { ImageApi } from 'api/ImageApi';
import { Typography } from '@mui/material';
import { theme } from 'style/theme';
import { useNavigate } from 'react-router-dom';

type Props = {
  hidden?: boolean;
};

export const PageHeader: React.FunctionComponent<Props> = ({ hidden }) => {
  const navigate = useNavigate();

  return (
    <div
      style={{
        width: `${theme.logoSize}px`,
        margin: `${theme.primaryPadding}px auto ${theme.secondaryPadding}px`,
        position: 'relative',
        textAlign: 'center',
      }}
      hidden={hidden}
      role="button"
      onClick={() => navigate('/')}
      onKeyUp={() => navigate('/')}
      tabIndex={0}
    >
      <div
        style={{
          position: 'absolute',
          top: '50%',
          transform: 'translateY(-50%)',
          right: `calc(100% + ${theme.secondaryPadding}px`,
          whiteSpace: 'nowrap',
        }}
      >
        <Typography variant={theme.headerTextVariant as any} align="center">
          Fredrik Beiron
        </Typography>
      </div>
      <img
        src={ImageApi.SiteImageRoute.getSiteImageUrl('logo.png')}
        alt="Icon"
        width="100%"
      />
      <div
        style={{
          position: 'absolute',
          top: '50%',
          transform: 'translateY(-50%)',
          left: `calc(100% + ${theme.secondaryPadding}px`,
          whiteSpace: 'nowrap',
        }}
      >
        <Typography variant={theme.headerTextVariant as any} align="center">
          Photography
        </Typography>
      </div>
    </div>
  );
};
