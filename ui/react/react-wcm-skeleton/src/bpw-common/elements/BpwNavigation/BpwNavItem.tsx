import React from 'react';

const components = {};

export function registerComponent(name, Component) {
  components[name] = Component;
}

export default function BpwNavItem(props) {
  const C = components[props.type];
  return C ? <C {...props} /> : null;
}
