import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { MatIconModule } from '@angular/material/icon';

import { SharedUIModule } from 'bpw-common';

import { DocsChangelogComponent } from './changelog/changelog.component';

const routes: Routes = [
    {
        path     : 'changelog',
        component: DocsChangelogComponent
    },
    {
        path        : 'getting-started',
        loadChildren: './getting-started/getting-started.module#GettingStartedModule'
    },
    {
        path        : 'working-with-wcm',
        loadChildren: './working-with-wcm/working-with-wcm.module#WorkingWithModule'
    },
    {
        path        : 'components',
        loadChildren: './components/components.module#ComponentsModule'
    },
    {
        path        : 'components-third-party',
        loadChildren: './components-third-party/components-third-party.module#ComponentsThirdPartyModule'
    },
    {
        path        : 'directives',
        loadChildren: './directives/directives.module#DirectivesModule'
    },
    {
        path        : 'services',
        loadChildren: './services/services.module#ServicesModule'
    }
];

@NgModule({
    declarations: [
        DocsChangelogComponent
    ],
    imports     : [
        RouterModule.forChild(routes),

        MatIconModule,

        SharedUIModule
    ]
})
export class DocumentationModule
{
}
