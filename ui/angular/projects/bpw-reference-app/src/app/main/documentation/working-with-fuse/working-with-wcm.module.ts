import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { SharedUIModule, HighlightModule } from 'bpw-common';

import { DocsWorkingWithServerComponent } from './server/server.component';
import { DocsWorkingWithProductionComponent } from './production/production.component';
import { DocsWorkingWithDirectoryStructureComponent } from './directory-structure/directory-structure.component';
import { DocsWorkingWithUpdatingComponent } from './updating/updating.component';
import { DocsWorkingWithMultiLanguageComponent } from './multi-language/multi-language.component';
import { DocsWorkingWithMaterialThemingComponent } from './material-theming/material-theming.component';
import { DocsWorkingWithThemeLayoutsComponent } from './theme-layouts/theme-layouts.component';
import { DocsWorkingWithPageLayoutsComponent } from './page-layouts/page-layouts.component';

const routes = [
    {
        path     : 'server',
        component: DocsWorkingWithServerComponent
    },
    {
        path     : 'production',
        component: DocsWorkingWithProductionComponent
    },
    {
        path     : 'directory-structure',
        component: DocsWorkingWithDirectoryStructureComponent
    },
    {
        path     : 'updating-wcmm',
        component: DocsWorkingWithUpdatingComponent
    },
    {
        path     : 'multi-language',
        component: DocsWorkingWithMultiLanguageComponent
    },
    {
        path     : 'material-theming',
        component: DocsWorkingWithMaterialThemingComponent
    },
    {
        path     : 'theme-layouts',
        component: DocsWorkingWithThemeLayoutsComponent
    },
    {
        path     : 'page-layouts',
        component: DocsWorkingWithPageLayoutsComponent
    }
];

@NgModule({
    declarations: [
        DocsWorkingWithServerComponent,
        DocsWorkingWithProductionComponent,
        DocsWorkingWithDirectoryStructureComponent,
        DocsWorkingWithUpdatingComponent,
        DocsWorkingWithMaterialThemingComponent,
        DocsWorkingWithMultiLanguageComponent,
        DocsWorkingWithThemeLayoutsComponent,
        DocsWorkingWithPageLayoutsComponent
    ],
    imports     : [
        RouterModule.forChild(routes),

        MatButtonModule,
        MatIconModule,

        SharedUIModule,
        HighlightModule
    ]
})
export class WorkingWithWCMModule {
}
