import { Typography } from '@mui/material';
import React from 'react';
import { theme } from 'style/theme';

type props = {
  icon: JSX.Element;
  texts: string[];
  marginBottom?: number;
  align?: 'left' | 'right';
  noValue?: boolean;
  textColor?: string;
};

export const TextWithIcon: React.FunctionComponent<props> = ({
  icon,
  texts,
  marginBottom,
  align,
  noValue,
  textColor,
}) => (
  <div style={{
    display: 'flex',
    alignItems: 'center',
    marginBottom: `${marginBottom ?? 2}px`,
    flexDirection: align === 'right' ? 'row-reverse' : 'row',
  }}
  >
    {icon}
    {texts.map((text, i) => (
      <Typography
        key={text}
        variant="body2"
        style={{
          color: textColor ?? theme.modeContrastColor,
          marginLeft: align !== 'right'
            ? `${(i === 0) ? theme.body2IconMargin : theme.body2Margin}px`
            : undefined,
          marginRight: align === 'right'
            ? `${(i === 0) ? theme.body2IconMargin : theme.body2Margin}px`
            : undefined,
          opacity: noValue === true ? 0.5 : 1,
        }}
      >
        {text}
      </Typography>
    ))}
  </div>
);
