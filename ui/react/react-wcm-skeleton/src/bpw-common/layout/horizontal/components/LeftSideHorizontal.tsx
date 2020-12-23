import React from 'react';
import BpwShortcuts from '../../../elements/BpwShortcuts';
import BpwSidePanel from '../../../elements/BpwSidePanel';

function LeftSideHorizontal() {
  return (
    <>
      <BpwSidePanel>
        <BpwShortcuts className="py-16 px-8" variant="vertical" />
      </BpwSidePanel>
    </>
  );
}

export default React.memo(LeftSideHorizontal);
