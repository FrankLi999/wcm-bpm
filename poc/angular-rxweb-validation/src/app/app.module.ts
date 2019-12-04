import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule, MatInputModule } from '@angular/material';

import { RxReactiveFormsModule } from '@rxweb/reactive-form-validators'
import { RxFormBuilder } from "@rxweb/reactive-form-validators";
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { UserComponent } from './user/user.component'
import { MaterialComponent } from './material/material.component';

@NgModule({
  declarations: [
    AppComponent,
    UserComponent,
    MaterialComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    AppRoutingModule,
    ReactiveFormsModule,
    RxReactiveFormsModule
  ],
  providers: [RxFormBuilder],
  bootstrap: [AppComponent]
})
export class AppModule { }
