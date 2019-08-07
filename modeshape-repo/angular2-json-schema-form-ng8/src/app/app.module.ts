import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FlexLayoutModule } from '@angular/flex-layout';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import {
  MatButtonModule, MatCardModule, MatCheckboxModule, MatIconModule,
  MatMenuModule, MatSelectModule, MatToolbarModule
} from '@angular/material';
import { RouterModule,  Routes } from '@angular/router';

import {
  JsonSchemaFormModule, 
  // NoFrameworkModule, 
  // Bootstrap3FrameworkModule, 
  // Bootstrap4FrameworkModule,
  MaterialDesignFrameworkModule
} from './dynamic-ui';

// To include JsonSchemaFormModule after downloading from NPM, use this instead:
//
//   import { JsonSchemaFormModule, NoFrameworkModule } from 'angular2-json-schema-form';
//
// but replace "NoFrameworkModule" with the framework you want to use,
// then import both JsonSchemaFormModule and the framework module, like this:
//
//   imports: [ ... NoFrameworkModule, JsonSchemaFormModule.forRoot(NoFrameworkModule) ... ]

// import { AceEditorDirective } from './ace-editor.directive';
import { ActivatedRoute, Router } from '@angular/router';

import { AppComponent } from './app.component';
import { DemoComponent } from './demo.component';

// const routes: Routes[] = [
export const routes: Routes = [
  { path: '', component: DemoComponent },
  { path: '**', component: DemoComponent }
];

@NgModule({
  declarations: [
    // AceEditorDirective,
    DemoComponent,
    AppComponent
  ],
  imports: [
    BrowserModule,
    // AppRoutingModule,
    BrowserModule, 
    BrowserAnimationsModule, 
    FlexLayoutModule, 
    FormsModule,
    HttpClientModule, 
    MatButtonModule, 
    MatCardModule, 
    MatCheckboxModule,
    MatIconModule, 
    MatMenuModule, 
    MatSelectModule, 
    MatToolbarModule,
    RouterModule.forRoot(routes),

    // NoFrameworkModule, 
    MaterialDesignFrameworkModule,
    // Bootstrap3FrameworkModule, 
    // Bootstrap4FrameworkModule,

    JsonSchemaFormModule.forRoot(
      // NoFrameworkModule,
      MaterialDesignFrameworkModule,
      // Bootstrap3FrameworkModule,
      // Bootstrap4FrameworkModule,
      MaterialDesignFrameworkModule
    )
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
