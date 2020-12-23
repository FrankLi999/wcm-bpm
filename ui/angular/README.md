# Run bpm-ui build locally
  bpm run build --project bpm-ui
  npm install http-server -g

  http-server dist/bpm-ui --port 4009
  http://bpm-ui:4009

# Run wcm-authoring locally

  npm run build --project wcm-authoring
  npm install http-server -g

  http-server dist/wcm-authoring --port 3009
  http://wcm-authoring:3009 
