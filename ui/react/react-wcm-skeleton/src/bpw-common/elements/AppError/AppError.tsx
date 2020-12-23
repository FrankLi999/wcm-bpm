import { React } from 'react';
import { Outlet } from 'react-router-dom';
const AppError = () => {
  return (
    <div>
      APP_ERROR
      <Outlet />
    </div>
  );
};

export default AppError;
