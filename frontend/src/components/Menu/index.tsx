import { MenuItem } from 'model/menu';
import React from 'react';
import { BrowserView, MobileView } from 'react-device-detect';
import { MobileMenu } from 'components/Menu/MobileMenu';
import { BrowserMenu } from './BrowserMenu';

type Props = {
  items: MenuItem[];
  hidden?: boolean;
};

export const Menu: React.FunctionComponent<Props> = ({ items, hidden }) => (
  <div hidden={hidden}>
    <BrowserView>
      <BrowserMenu items={items} />
    </BrowserView>
    <MobileView>
      <MobileMenu items={items} />
    </MobileView>
  </div>
);
