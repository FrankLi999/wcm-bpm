import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { SharedUIModule, HighlightModule } from 'bpw-common';

import { DocsDirectivesIfOnDomComponent } from './ifOnDom/if-on-dom.component';
import { DocsDirectivesInnerScrollComponent } from './innerScroll/inner-scroll.component';
import { DocsDirectivesMatSidenavComponent } from './matSidenav/mat-sidenav.component';
import { DocsDirectivesPerfectScrollbarComponent } from './perfectScrollbar/perfect-scrollbar.component';

const routes = [
    {
        path     : 'if-on-dom',
        component: DocsDirectivesIfOnDomComponent
    },
    {
        path     : 'inner-scroll',
        component: DocsDirectivesInnerScrollComponent
    },
    {
        path     : 'mat-sidenav',
        component: DocsDirectivesMatSidenavComponent
    },
    {
        path     : 'perfect-scrollbar',
        component: DocsDirectivesPerfectScrollbarComponent
    }
];

@NgModule({
    declarations: [
        DocsDirectivesIfOnDomComponent,
        DocsDirectivesInnerScrollComponent,
        DocsDirectivesMatSidenavComponent,
        DocsDirectivesPerfectScrollbarComponent
    ],
    imports     : [
        RouterModule.forChild(routes),

        MatButtonModule,
        MatIconModule,

        SharedUIModule,
        HighlightModule
    ]
})
export class DirectivesModule {
}
