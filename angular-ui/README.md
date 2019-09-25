# Create library projects

   // ng new angular-commons --create-application=false
   
   ng new bpw-rest-client --create-application=false
   cd bpw-rest-client
   ng generate library bpw-rest-client --prefix=rest-client
   
      
   ng new bpw-form --create-application=false
   ng generate library bpw-form --prefix=bpw-form
   
   ng new bpw-components --create-application=false
   cd bpw-components
   ng generate library bpw-components --prefix=bpw-components

   ng new bpw-auth --create-application=false
   cd bpw-auth
   ng generate library bpw-auth --prefix=bpw-auth
   
   ng new bpw-layout --create-application=false
   cd bpw-layout
   ng generate library bpw-layout --prefix=bpw-layout

# Build
NOTE: ts file name should not has the same name and the folder name, otherwise, ng-packer is going to fail.

NOTES: global styles in the library. check bpw-components.

Rest client:

ng build rest-client
ng build bpw-components
npm run build-bpw-components-styles
npm run bpw-components-scss-bundle
ng build bpw-auth
build-bpw-auth-styles
ng build bpw-layout
npm run build-bpw-layout-styles

# Test
   ng test rest-client
   ng test bpw-components
   ng test bpw-layout
   ng test bpw-auth
   ng test bpw-dynamic-ui
   ng test bpw-form

   ng test bpw-tester

# Run
   ng serve bpw-tester

# Pack
  ng build rest-client
  cd dist/rest-client & npm pack
  cd dist/bpw-components & npm pack
  cd dist/bpw-auth & npm pack

# use the library from external workspace
  npm install ../bpw-rest-client/dist/bpw-rest-client/bpw-rest-client-0.0.1.tgz
  npm install ../bpw-form/dist/bpw-form/bpw-form-0.0.1.tgz
  
  npm install ../bpw-components/dist/bpw-components/bpw-components-0.0.1.tgz
  npm install ../bpw-layout/dist/bpw-layout/bpw-layout-0.0.1.tgz
  npm install ../bpw-auth/dist/bpw-auth/bpw-auth-0.0.1.tgz
  
  