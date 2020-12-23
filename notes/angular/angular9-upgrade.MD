## How To Update Angular CLI To Latest Version 9
https://angular.io/guide/updating-to-version-9
https://update.angular.io/#8.0:9.0l3

https://www.angularjswiki.com/angular/update-angular-cli-version-ng-update-to-latest-6-7-versions/

  > First update the local angular cli to version 8.3.17 or 8.x version by using below command.

  npm install --no-save @angular/cli@^8.3.17

  > After updating angular cli to version 8 and above, use the below command to 
    update angular cli to version 9

    ng update @angular/cli @angular/core



## Update Angular CLI version Globally
    To update Angular CLI version globally in your system use the below commands

    npm uninstall -g angular-cli
    npm cache clean or npm cache verify (if npm &gt; 5)
    npm install -g @angular/cli@latest

## Checklist

### Before Updating

    > Instead of importing from @angular/material, you should import deeply from the specific component. E.g. @angular/material/button. ng update will do this automatically for you.

    > For lazy loaded modules via the router, make sure you are using dynamic imports. Importing via string is removed in v9. ng update should take care of this automatically.

    > We are deprecating support for @angular/platform-webworker, as it has been incompatible with the CLI. Running Angular's rendering architecture in a web worker did not meet developer needs. You can still use web workers with Angular. Learn more in our web worker guide. If you have use cases where you need this, let us know at devrel@angular.io!

    https://v9.angular.io/guide/web-worker

    > Support for web tracing framework in Angular was deprecated in version 8. You should stop using any of the wtf* APIs. To do performance tracing, we recommend using browser performance tools.
    https://developers.google.com/web/tools/lighthouse/audits/user-timing

### During the Update
    > Make sure you are using Node 10.13 or later.
    > Run ng update @angular/core@8 @angular/cli@8 in your workspace directory to update to the latest 8.x 
        version of   @angular/core and @angular/cli and commit these changes.
    > You can optionally pass the --create-commits (or -C) flag to ng update commands to create a git 
        commit per individual migration.
    > Run ng update @angular/core @angular/cli which should bring you to version 9 of Angular.
    > Your project has now been updated to TypeScript 3.7, read more about new compiler checks and 
        errors that might require you to fix issues in your code in the TypeScript 3.6 or TypeScript 3.7 announcements.
    > Run ng update @angular/material.

    > If you use Angular Universal, run ng update @nguniversal/hapi-engine or ng update @nguniversal/express-engine 
        depending on the engine you use. This step may require the --force flag if any of your third-party dependencies have not updated the Angular version of their peer dependencies.
    > If your project depends on other Angular libraries, we recommend that you consider updating to their 
        latest version. In some cases this update might be required in order to resolve API incompatibilities. Consult ng update or npm outdated to learn about your outdated libraries.
    > During the update to version 9, your project was transformed as necessary via code migrations in order to 
        remove any incompatible or deprecated API calls from your code base. You can now review these changes, and consult the Updating to version 9 guide to learn more about the changes.
    > Bound CSS styles and classes previously were applied with a "last change wins" strategy, but now follow a 
        defined precedence. Learn more about Class and Style bindings.
    > If you are a library author and you had a method returning ModuleWithProviders (typically via a method named 
        forRoot()), you will need to specify the generic type. Learn more angular.io, https://v9.angular.io/guide/deprecations#modulewithproviders-type-without-a-generic
    > If you use ngForm element selector to create Angular Forms, you should instead use ng-form.
    > We have updated the tsconfig.app.json to limit the files compiled. If you rely on other files being 
        included in the compilation, such as a typings.d.ts file, you need to manually add it to the compilation.
### After the Update
    > If you depend on many Angular libraries you may consider speeding up your build by invoking the 
        ngcc (Angular Compatibility Compiler) in an npm postinstall script via small change to your 
        package.json.
    > If you use Angular Universal with @nguniversal/express-engine or @nguniversal/hapi-engine, several backup 
        files will be created. One of them for server.ts. If this file defers from the default one, you may need to copy some changes from the server.ts.bak to server.ts manually.
    > Angular 9 introduced a global $localize() function that needs to be loaded if you depend on Angular's 
        internationalization (i18n). Run ng add @angular/localize to add the necessary packages and code modifications. Consult the $localize Global Import Migration guide to learn more about the changes.
    > If you have specified any entryComponents in your NgModules or had any uses of ANALYZE_FOR_ENTRY_COMPONENTS, 
        you can remove them. They are no longer required with the Ivy compiler and runtime.
    > If you use TestBed.get, you should instead use TestBed.inject. This new method has the same behavior,
         but is type safe.
    > If you use Angular's i18n support, you will need to begin using @angular/localize. Learn more 
        about the $localize Global Import Migration.

        https://v9.angular.io/guide/migration-localize

## Check out of date modules
  npm outdated    