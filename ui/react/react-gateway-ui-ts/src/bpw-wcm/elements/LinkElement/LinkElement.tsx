import React from 'react';
function LinkElement(props) {
  const { title, contentparameter, contentpath, link, hint } = props;
  const query = {};
  query[`${contentparameter}`] = `${contentpath}`;
  return (
    <Link to={{ pathname: `${link}`, query: { ...query } }} title={`${hint}`}>
      {title}
    </Link>
  );
}
export default LinkElement;
