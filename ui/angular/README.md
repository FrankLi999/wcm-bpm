# build libraries: do not to it for now, as tsconfig set the apps to refer to lib locally.
  bpw-rest-client: 
    npm run build --project bpw-rest-client
  bpw-auth:
    npm run build --project bpm-ui 
  bpw-ajsf-core:
    npm run build --project @bpw/ajsf-core
  bpw-ajsf-material
    npm run build --project @bpw/ajsf-material

# build

  sudo npm install -g @angular/cli 
  sudo npm install -g http-server

  npm run copy-styles
  npm run copy-wcm-styles

# Run bpm-ui build locally
  npm run build --project bpm-ui 
  or
  mvn clean install -Dangular-project=bpm-ui
  npm install http-server -g

  http-server dist/bpm-ui --port 4009
  http://bpm-ui:4009

# Run wcm-authoring locally

  npm run build --project wcm-authoring
   mvn clean install -Dangular-project=wcm-authoring
  npm install http-server -g

  http-server dist/wcm-authoring --port 3009
  http://wcm-authoring:3009 
