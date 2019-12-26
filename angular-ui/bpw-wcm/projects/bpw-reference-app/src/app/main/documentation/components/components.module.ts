import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { 
    SharedUIModule, 
    CountdownModule,
    HighlightModule,
    MaterialColorPickerModule,
    WidgetModule
} from 'bpw-common';
import { DocsComponentsCountdownComponent } from './countdown/countdown.component';
import { DocsComponentsHighlightComponent } from './highlight/highlight.component';
import { DocsComponentsMaterialColorPickerComponent } from '../components/material-color-picker/material-color-picker.component';
import { DocsComponentsNavigationComponent } from './navigation/navigation.component';
import { DocsComponentsProgressBarComponent } from './progress-bar/progress-bar.component';
import { DocsComponentsSearchBarComponent } from './search-bar/search-bar.component';
import { DocsComponentsSidebarComponent } from './sidebar/sidebar.component';
import { DocsComponentsShortcutsComponent } from './shortcuts/shortcuts.component';
import { DocsComponentsWidgetComponent } from './widget/widget.component';

const routes = [
    {
        path     : 'countdown',
        component: DocsComponentsCountdownComponent
    },
    {
        path     : 'highlight',
        component: DocsComponentsHighlightComponent
    },
    {
        path     : 'material-color-picker',
        component: DocsComponentsMaterialColorPickerComponent
    },
    {
        path     : 'navigation',
        component: DocsComponentsNavigationComponent
    },
    {
        path     : 'progress-bar',
        component: DocsComponentsProgressBarComponent
    },
    {
        path     : 'search-bar',
        component: DocsComponentsSearchBarComponent
    },
    {
        path     : 'sidebar',
        component: DocsComponentsSidebarComponent
    },
    {
        path     : 'shortcuts',
        component: DocsComponentsShortcutsComponent
    },
    {
        path     : 'widget',
        component: DocsComponentsWidgetComponent
    }
];

@NgModule({
    declarations: [
        DocsComponentsCountdownComponent,
        DocsComponentsHighlightComponent,
        DocsComponentsMaterialColorPickerComponent,
        DocsComponentsNavigationComponent,
        DocsComponentsProgressBarComponent,
        DocsComponentsSearchBarComponent,
        DocsComponentsSidebarComponent,
        DocsComponentsShortcutsComponent,
        DocsComponentsWidgetComponent
    ],
    imports     : [
        RouterModule.forChild(routes),

        MatButtonModule,
        MatIconModule,

        SharedUIModule,

        CountdownModule,
        HighlightModule,
        MaterialColorPickerModule,
        WidgetModule
    ]
})
export class ComponentsModule {
}
