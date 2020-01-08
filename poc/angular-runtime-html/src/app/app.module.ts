import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AppChildComponent } from './app-child/app-child.component';
import { HelloComponent } from './hello.component';
import { DynamicContentComponent } from './dynamic-content/dynamic-content.component';
import { DynamicContent2Component } from './dynamic-content2/dynamic-content2.component';
import { CounterComponent } from './counter/counter.component';
import { WrapperComponent } from './wrapper/wrapper.component';
import { SafePipe } from './safe.pipe';
@NgModule({
  declarations: [
    AppComponent,
    AppChildComponent,
    HelloComponent,
    DynamicContentComponent,
    DynamicContent2Component,
    CounterComponent,
    WrapperComponent,
    SafePipe
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
