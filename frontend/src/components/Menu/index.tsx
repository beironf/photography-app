import { MenuItem } from 'model/menu';
import React from 'react';
import { BrowserView, MobileView } from 'react-device-detect';
import { MobileMenu } from 'components/Menu/MobileMenu';
import { DesktopMenu } from './DesktopMenu';

type Props = {
  items: MenuItem[];
  hidden?: boolean;
};

export const Menu: React.FunctionComponent<Props> = ({ items, hidden }) => (
  <div hidden={hidden}>
    <BrowserView>
      <DesktopMenu items={items} />
    </BrowserView>
    <MobileView>
      <MobileMenu items={items} />
    </MobileView>
  </div>
);
