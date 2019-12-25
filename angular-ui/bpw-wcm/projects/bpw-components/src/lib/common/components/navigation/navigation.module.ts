import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatRippleModule } from '@angular/material/core';
import { MatIconModule } from '@angular/material/icon';

// import { TranslateModule } from '@ngx-translate/core';

import { NavigationComponent } from './navigation.component';
import { NavVerticalItemComponent } from './vertical/item/item.component';
import { NavVerticalCollapsableComponent } from './vertical/collapsable/collapsable.component';
import { NavVerticalGroupComponent } from './vertical/group/group.component';
import { NavHorizontalItemComponent } from './horizontal/item/item.component';
import { NavHorizontalCollapsableComponent } from './horizontal/collapsable/collapsable.component';

@NgModule({
    imports     : [
        CommonModule,
        RouterModule,

        MatIconModule,
        MatRippleModule
        // TranslateModule.forChild()
    ],
    exports     : [
        NavigationComponent
    ],
    declarations: [
        NavigationComponent,
        NavVerticalGroupComponent,
        NavVerticalItemComponent,
        NavVerticalCollapsableComponent,
        NavHorizontalItemComponent,
        NavHorizontalCollapsableComponent
    ]
})
export class NavigationModule {
}
