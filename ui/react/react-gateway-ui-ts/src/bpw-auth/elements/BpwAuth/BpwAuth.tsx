import { React } from 'react';
import { Outlet } from 'react-router-dom';
const BpwAuth = () => {
  return (
    <>
      <div>
        BPW_AUTH
        <Outlet />
      </div>
    </>
  );
};

export default BpwAuth;
