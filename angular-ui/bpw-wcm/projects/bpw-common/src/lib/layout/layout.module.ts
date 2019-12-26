import { NgModule } from '@angular/core';

import { VerticalLayout1Module } from './vertical/layout-1/layout-1.module';
import { VerticalLayout2Module } from './vertical/layout-2/layout-2.module';
import { VerticalLayout3Module } from './vertical/layout-3/layout-3.module';

import { HorizontalLayout1Module } from './horizontal/layout-1/layout-1.module';
import { AppConfigStoreModule } from './store/store.module';
@NgModule({
    imports: [
        VerticalLayout1Module,
        VerticalLayout2Module,
        VerticalLayout3Module,

        HorizontalLayout1Module,
        AppConfigStoreModule
    ],
    exports: [
        VerticalLayout1Module,
        VerticalLayout2Module,
        VerticalLayout3Module,

        HorizontalLayout1Module
    ]
})
export class LayoutModule {
}
