import React from 'react';
import BpwScrollbars from '../../../elements/BpwScrollbars';
import Navigation from '../../shared-components/Navigation';

function NavbarHorizontal() {
  return (
    <div className="flex flex-auto items-center w-full h-full container px-16 lg:px-24">
      <BpwScrollbars className="flex h-full items-center">
        <Navigation className="w-full" layout="horizontal" dense />
      </BpwScrollbars>
    </div>
  );
}

export default React.memo(NavbarHorizontal);
