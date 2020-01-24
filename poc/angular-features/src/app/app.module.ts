import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { TabsComponent } from './tabs/tabs.component';
import { TabComponent } from './tabs/tab.component';
import { DynamicTabsDirective } from './tabs/dynamic-tabs.directive';
import { PersonEditComponent } from './people/person-edit.component';
import { PeopleListComponent } from './people/people-list.component';
import { AppComponent } from './app.component';


import { TalksCmp } from './talks/talks.component';
import { TalkCmp } from './talk/talk.component';
import { WatchButtonCmp } from './watch-button/watch-button.component';
import { RateButtonCmp } from './rate-button/rate-button.component';
import { FormatRatingPipe } from './format-rating.pipe';
import {App} from "./app";
import {createFiltersObject} from "./create_filters_object";
import { FiltersCmp } from './filters/filters.component';

@NgModule({
  declarations: [
    AppComponent,
    TabsComponent,
    TabComponent,
    DynamicTabsDirective,
    PersonEditComponent,
    PeopleListComponent,
    TalksCmp,
    TalkCmp,
    WatchButtonCmp,
    RateButtonCmp,
    FormatRatingPipe,
    FiltersCmp
  ],
  imports: [
    BrowserModule, 
    BrowserAnimationsModule,
    ReactiveFormsModule,
    HttpModule,
    MatButtonModule,
    MatCardModule,
    MatInputModule,
    MatCheckboxModule
  ],
  providers: [
    App,
    {provide: 'createFiltersObject', useValue: createFiltersObject}
  ],
  bootstrap: [AppComponent],
  // register the dynamic components here
  entryComponents: [TabComponent]
})
export class AppModule {}