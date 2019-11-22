https://blog.angularindepth.com/angular-workspace-no-application-for-you-4b451afcc2ba
https://blog.angularindepth.com/creating-a-library-in-angular-6-87799552e7e5
https://blog.angularindepth.com/creating-a-library-in-angular-6-part-2-6e2bc1e14121
https://blog.angularindepth.com/the-angular-library-series-publishing-ce24bb673275
https://blog.angularindepth.com/npm-peer-dependencies-f843f3ac4e7f

https://www.softwarearchitekt.at/aktuelles/your-options-for-building-angular-elements/

npm install -g @angular/cli@latest

npm uninstall -g angular-cli
npm cache clean
npm cache verify
npm cache verify --force

If you are facing issue with angular/cli then use the following commands:
npm uninstall -g angular-cli to uninstall the angular/cli.
npm cache clean to clean your npm cache from app data folder under your username.
use npm cache verify to verify your cache whether it is corrupted or not.
use npm cache verify --force to clean your entire cache from your system.

Note:
You can also delete by the following the paths
C:\Users\"Your_syste_User_name"\AppData\Roaming\npm and
C:\Users\"Your_syste_User_name"\AppData\Roaming\npm-cache
=====================================================================================


Feature Module	Declarations	Providers	   Exports	        Imported by
--------------  ------------    ---------      --------         ---------
Domain	            Yes	           Rare	        Top component	Feature, AppModule
Routed	            Yes	           Rare	        No	            None
Routing	            No	           Yes (Guards)	RouterModule	Feature (for routing)
Service	            No	           Yes	        No	            AppModule
Widget	            Yes	           Rare	        Yes	            Feature


=====================================================================================

Generally, you'll only need providedIn for providing services and forRoot()/forChild() for routing

If a module defines both providers and declarations (components, directives, pipes), then loading the module in multiple feature modules would duplicate the registration of the service. This could result in multiple service instances and the service would no longer behave as a singleton.

There are multiple ways to prevent this:

	> Use the providedIn syntax instead of registering the service in the module.
	> Separate your services into their own module.
	> Define forRoot() and forChild() methods in the module.

======================================================================================
1. Create library projects

   // ng new angular-commons --create-application=false
   
   ng new bpw-rest-client --create-application=false
   cd bpw-rest-client
   ng generate library bpw-rest-client --prefix=rest-client
   
   ng new bpw-form --create-application=false
   cd bpw-form
   ng generate library bpw-form --prefix=bpw-form
   
   ng new bpw-components --create-application=false
   cd bpw-components
   ng generate library bpw-components --prefix=bpw-components
   
   ng new bpw-auth-store --create-application=false
   cd bpw-auth-store
   ng generate library bpw-auth-store --prefix=bpw-auth-store

   ng new bpw-layout --create-application=false
   cd bpw-layout
   ng generate library bpw-layout --prefix=bpw-layout
   
   <!-- ng new bpw-theme --create-application=false
   cd bpw-theme
   ng generate library wcm-authoring-theme --prefix=wcm-authoring-theme
   ng generate library bpm-theme --prefix=bpm-theme -->

   
   ng new bpw-wcm --create-application=false
   cd bpw-wcm
   ng generate library bpw-wcm-service --prefix=bpw-wcm-service
     ng generate library bpw-wcm-elements --prefix=bpw-wcm-elements
   ng generate library bpw-wcm-preview --prefix=bpw-wcm-preview
   ng generate library bpw-wcm-authoring --prefix=bpw-wcm-authoring

   ng add @angular/elements

   ng new bpw-elements --create-application=false
   cd bpw-elements
   ng g application dashboard-title
   ng g application playground-app

   ng add @angular/elements
   ng add ngx-build-plus
   ng g ngx-build-plus:wc-polyfill
   ng g ngx-build-plus:externals


   ng new bpw-auth --create-application=false
   cd bpw-auth
   ng generate library bpw-auth --prefix=bpw-auth
   

   ng new authoring-ui 
   cd authoring-ui
   ng add @angular/elements
   ng add ngx-build-plus
   ng g ngx-build-plus:wc-polyfill

   ng new bpm-ui 
   ng add @angular/elements
   ng add ngx-build-plus
   ng g ngx-build-plus:wc-polyfill

   ng new bpw-dynamic-ui --create-application=false
   ng generate library bpw-dynamic-ui --prefix=bpw-dynamic-ui
   
2. Build shared compoments

   
   cd bpw-rest-client
   npm install
   ng build bpw-rest-client
   cd dist/bpw-rest-client 
   npm pack

   cd bpw-form
   npm install
   ng build bpw-form
   cd dist/bpw-form 
   npm pack

   cd bpw-components
   
   
   npm install
   ng build bpw-components
   npm run build-bpw-components-styles
   # npm run bpw-components-scss-bundle
   npm run cp-bpw-components-assets

   cd dist/bpw-components 
   npm pack
   
   cd bpw-auth-store
   npm install ../bpw-rest-client/dist/bpw-rest-client/bpw-rest-client-0.0.1.tgz --save

   npm install ../bpw-components/dist/bpw-components/bpw-components-0.0.1.tgz --save

   ng build bpw-auth-store
   cd dist/bpw-auth-store
   npm pack

   cd bpw-layout
   npm install ../bpw-components/dist/bpw-components/bpw-components-0.0.1.tgz --save
   npm install
   
   ng build bpw-layout
   npm run build-bpw-layout-styles
   cd dist/bpw-layout
   npm pack

   cd bpw-wcm
   
   npm install ../bpw-rest-client/dist/bpw-rest-client/bpw-rest-client-0.0.1.tgz --save

   npm install ../bpw-auth-store/dist/bpw-auth-store/bpw-auth-store-0.0.1.tgz --save

   npm install ../bpw-components/dist/bpw-components/bpw-components-0.0.1.tgz --save
   
   npm install ../bpw-form/dist/bpw-form/bpw-form-0.0.1.tgz --save
   

   npm install ../bpw-layout/dist/bpw-layout/bpw-layout-0.0.1.tgz --save
   
   ng build bpw-wcm-service
   cd dist/bpw-wcm-service
   npm pack

   ng build bpw-wcm-preview
   cd dist/bpw-wcm-preview
   npm pack
   
   ng build bpw-wcm-authoring
   cd dist/bpw-wcm-authoring
   npm pack

    cd bpw-auth
    npm install ../bpw-rest-client/dist/bpw-rest-client/bpw-rest-client-0.0.1.tgz --save

    npm install ../bpw-components/dist/bpw-components/bpw-components-0.0.1.tgz --save
   
    npm install ../bpw-auth-store/dist/bpw-auth-store/bpw-auth-store-0.0.1.tgz --save
   
   npm install
   ng build bpw-auth
   npm run build-bpw-auth-styles
   cd dist/bpw-auth
   npm pack
   
	
   
   cd authoring-ui
   
   
   npm install ../bpw-rest-client/dist/bpw-rest-client/bpw-rest-client-0.0.1.tgz --save

   npm install ../bpw-form/dist/bpw-form/bpw-form-0.0.1.tgz --save

   npm install ../bpw-components/dist/bpw-components/bpw-components-0.0.1.tgz --save
   
   npm install ../bpw-auth-store/dist/bpw-auth-store/bpw-auth-store-0.0.1.tgz --save

   npm install ../bpw-auth/dist/bpw-auth/bpw-auth-0.0.1.tgz --save  

   npm install ../bpw-layout/dist/bpw-layout/bpw-layout-0.0.1.tgz --save

   npm install ../bpw-wcm/dist/bpw-wcm-service/bpw-wcm-service-0.0.1.tgz --save
   npm install ../bpw-wcm/dist/bpw-wcm-elements/bpw-wcm-elements-0.0.1.tgz --save
   npm install ../bpw-wcm/dist/bpw-wcm-preview/bpw-wcm-preview-0.0.1.tgz --save
   npm install ../bpw-wcm/dist/bpw-wcm-authoring/bpw-wcm-authoring-0.0.1.tgz --save
   
   bpm-ui
   npm install ../bpw-rest-client/dist/bpw-rest-client/bpw-rest-client-0.0.1.tgz --save

   npm install ../bpw-form/dist/bpw-form/bpw-form-0.0.1.tgz --save

   npm install ../bpw-components/dist/bpw-components/bpw-components-0.0.1.tgz --save
   
   npm install ../bpw-auth-store/dist/bpw-auth-store/bpw-auth-store-0.0.1.tgz --save

   npm install ../bpw-auth/dist/bpw-auth/bpw-auth-0.0.1.tgz --save  

   npm install ../bpw-layout/dist/bpw-layout/bpw-layout-0.0.1.tgz --save

   npm install ../bpw-wcm/dist/bpw-wcm-service/bpw-wcm-service-0.0.1.tgz --save
   npm install ../bpw-wcm/dist/bpw-wcm-elements/bpw-wcm-elements-0.0.1.tgz --save

   npm install
   
   npm run start

# Test

   http://localhost:3000
   login with: admin@example.com/admin!
3. 
   npm install ../bpw-rest-client/dist/bpw-rest-client/bpw-rest-client-0.0.1.tgz	
    npm install ../bpw--form/dist/bpw-form/bpw-form-0.0.1.tgz
   npm install ../bpw-components/dist/bpw-components/bpw-components-0.0.1.tgz	
   npm install ../bpw-components/dist/bpw-store/bpw-store-0.0.1.tgz	
   npm install ../bpw-auth/dist/bpw-auth/bpw-auth-0.0.1.tgz	
   npm install ../bpw-layout/dist/bpw-layout/bpw-layout-0.0.1.tgz	
   cd angular-commons
   
   generate libraries:
   
   
   
   
   
   
   
   generate test app:
   
   ng generate application bpw-tester
## Linked libraries
    https://docs.npmjs.com/cli/link
		While working on a published library, you can use npm link to avoid reinstalling the library on every build.

		The library must be rebuilt on every change. When linking a library, make sure that the build step runs in watch mode, and that the library's package.json configuration points at the correct entry points. For example, main should point at a JavaScript file, not a TypeScript file.

   #Build
   ## libraries are built in prod mode by default
   ng build rest-client [--watch]
   ng build bpw-components [--watch]
   ng build bpw-layout [--watch]
   ng build bpw-auth [--watch]
   ng build bpw-dynamic-ui [--watch]
   ng build bpw-form [--watch]
   
   ng build bpw-tester --prod
   
   #serve
   ng serve bpw-tester
   
   #Test
   ng test rest-client
   ng test bpw-components
   ng test bpw-layout
   ng test bpw-auth
   ng test bpw-dynamic-ui
   ng test bpw-form
   
   ng test bpw-tester
   
   #create a new library components
   ng generate component my-widget --project=bpw-components
   
   #Exporting the component from library’s modules
   
   # Adding the component to the entry file
   export * from './lib/my-widget/my-widget.component';
   
3. packaging

   > Root package.json
   This is the main package.json file for our library workspace. 
   Use this to list dependencies for both the application and the 
   library. Any package that is needed to run or build either the
   application or the project must be in here.

   > Library project package.json
   It tells ng-packagr what information goes into the distribution 
   package.json that will be shipped with the library
   
	name: name of the lib. used in "import {...} from 'lib_name'"
	version: The version of the library. https://semver.org/
	dependency: This contains only the dependencies necessary to run the library
	    can have both dependencies and peerDependencies but not devDependencies 
		in this one
	> Library distribution package.json
	The library distribution package.json is generated by ng-packagr
	
	> packing
		"scripts": {
		  ...
		  "build_lib": "ng build example-ng6-lib",
		  "npm_pack": "cd dist/example-ng6-lib && npm pack",
		  "package": "npm run build_lib && npm run npm_pack"
		  ...
		},
		
		this created "example-ng6-lib-0.0.1.tgz"
	> Using the Library in a Separate Application
      npm install ../bpw-rest-client/dist/bpw-rest-client/bpw-rest-client-0.0.1.tgz	
	
	> npm deal with conflict versions

		npm deals with version conflicts by adding duplicate 
		private versions of the conflicted package.
		
		so if you add a package to your dependencies, there is 
		a chance it may end up being duplicated in node_modules.
		
		The key is:
		We don’t want our library adding another version of a 
		package to node-modules when that package could conflict 
		with an existing version and cause problems.
		
		The Guidelines
		> Favor using Peer Dependencies when one of the following is true:
			o Having multiple copies of a package would cause 
				conflicts
			o The dependency is visible in your interface
			o You want the developer to decide which version to 
				install
		
		Let’s take the example of angular/core. Obviously, if you 
			are creating an Angular Library, angular/core is going 
			to be a very visible part of your library’s interface. 
			Hence, it belongs in your peerDependencies.
		However, maybe your library uses Moment.js internally to 
			process some time related inputs. Moment.js most likely 
			won’t be exposed in the interface of your Angular Services 
			or Components. Hence, it belongs in your dependencies.

		Angular as a Dependency
			“Do I even need to specify angular/core as a dependency?
			If someone is using my library, they will already have an 
			existing Angular project.”
			
			However, it really needs to tell the developer which 
			Angular versions our library is compatible with.
			
			Add at least angular/core for the compatible Angular 
			version to your peerDependencies.
			
			
	> peer-dependencies
	
	Peer Dependencies are used to specify that our package is 
	compatible with a specific version of an npm package.
	
	To add a Peer Dependency you actually need to manually modify
	your package.json file.
	
	When you add a package in dependencies, you are saying:
		My code needs this package to run.
		If this package doesn’t already exist in my node_modules 
			directory, then add it automatically.
		Furthermore, add the packages that are listed in the 
			package’s dependencies. These packages are called 
			transitive dependencies.
			
		npm install lodash
		
	By adding a package in peerDependencies you are saying:
		My code is compatible with this version of the package.
		If this package already exists in node_modules, do nothing.
		If this package doesn’t already exist in the node_modules 
			directory or it is the wrong version, don’t add it. 
			But, show a warning to the user that it wasn’t found.

	
4. publish 
    https://docs.npmjs.com/files/package.json.
	if want to add information, you need to add it in you Library’s Project 
	package.json.
	
	"scripts": {
	  ...
	  "build_lib": "ng build ng-example-library",
	  "copy-license": "copy .\\LICENSE .\\dist\\ng-example-library",
	  "copy-readme": "copy .\\README.md .\\dist\\ng-example-library",
	  "copy-files": "npm run copy-license && npm run copy-readme",
	  "npm_pack": "cd dist/ng-example-library && npm pack",
	  "package": "npm run build_lib && npm run copy-files && npm run npm_pack",
	  ...
	},
	
	project's package.json:
	
	License: Reference the LICENSE file.
	Repository; Point to the GitHub repository.
	Description: Key words
	Home page: Point to this article.
	
	{
	  "name": "ng-example-library",
	  "version": "1.2.0",
	  "description": "This is a simple example Angular Library published to npm.",
	  "keywords" :["Angular","Library"],
	  "license" : "SEE LICENSE IN LICENSE",
	  "repository": {
		"type" : "git",
		"url" : "https://github.com/t-palmer/ng-example-library"
	  },
	  "homepage" :"https://medium.com/@palmer_todd/the-angular-library-series-publishing-ce24bb673275",
	  "peerDependencies": {
		"@angular/common": "^6.0.0-rc.0 || ^6.0.0",
		"@angular/core": "^6.0.0-rc.0 || ^6.0.0"
	  }
	}
	
	npm login
    npm whoami
	npm publish ./dist/ng-example-library/ng-example-library-1.2.0.tgz
	
	https://www.npmjs.com/package/ng-example-library
	
	
5. tsconfig.json, package.json, ng-package.json file of the libraries

	"peerDependencies"

6. Utilize angular libraries within/cross workspace
   within workspace:
	in tsconfig.json of the workspace, the following are added:
		"paths": {
		  "rest-client": [
			"dist/rest-client"
		  ],
		  "rest-client/*": [
			"dist/rest-client/*"
		  ],
		  "bpw-layout": [
			"dist/bpw-layout"
		  ],
		  "bpw-layout/*": [
			"dist/bpw-layout/*"
		  ],
		  "bpw-components": [
			"dist/bpw-components"
		  ],
		  "bpw-components/*": [
			"dist/bpw-components/*"
		  ],
		  "bpw-auth": [
			"dist/bpw-auth"
		  ],
		  "bpw-auth/*": [
			"dist/bpw-auth/*"
		  ],
		  "bpw-dynamic-ui": [
			"dist/bpw-dynamic-ui"
		  ],
		  "bpw-dynamic-ui/*": [
			"dist/bpw-dynamic-ui/*"
		  ],
		  "bpw-form": [
			"dist/bpw-form"
		  ],
		  "bpw-form/*": [
			"dist/bpw-form/*"
		  ]
		}
	 

4. ng-packagr

5. plug and play

   
   