import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedUIModule } from 'bpw-common';

const routes = [
    {
        path        : 'dashboards/analytics',
        loadChildren: () => import('./dashboards/analytics/analytics.module').then(m => m.AnalyticsDashboardModule)
        // loadChildren: './dashboards/analytics/analytics.module#AnalyticsDashboardModule'
    },
    {
        path        : 'dashboards/project',
        loadChildren: () => import('./dashboards/project/project.module').then(m => m.ProjectDashboardModule)
        //loadChildren: './dashboards/project/project.module#ProjectDashboardModule'
    },
    {
        path        : 'mail',
        loadChildren: () => import('./mail/mail.module').then(m => m.MailModule)
        // loadChildren: './mail/mail.module#MailModule'
    },
    {
        path        : 'mail-ngrx',
        loadChildren: () => import('./mail-ngrx/mail.module').then(m => m.MailNgrxModule)
        //loadChildren: './mail-ngrx/mail.module#MailNgrxModule'
    },
    {
        path        : 'chat',
        loadChildren: () => import('./chat/chat.module').then(m => m.ChatModule)
        // loadChildren: './chat/chat.module#ChatModule'
    },
    {
        path        : 'calendar',
        loadChildren: () => import('./calendar/calendar.module').then(m => m.CalendarModule)
        // loadChildren: './calendar/calendar.module#CalendarModule'
    },
    {
        path        : 'e-commerce',
        loadChildren: () => import('./e-commerce/e-commerce.module').then(m => m.EcommerceModule)
        //loadChildren: './e-commerce/e-commerce.module#EcommerceModule'
    },
    {
        path        : 'academy',
        loadChildren: () => import('./academy/academy.module').then(m => m.AcademyModule)
        // loadChildren: './academy/academy.module#AcademyModule'
    },
    {
        path        : 'todo',
        loadChildren: () => import('./todo/todo.module').then(m => m.TodoModule)
        // loadChildren: './todo/todo.module#TodoModule'
    },
    {
        path        : 'file-manager',
        loadChildren: () => import('./file-manager/file-manager.module').then(m => m.FileManagerModule)
        // loadChildren: './file-manager/file-manager.module#FileManagerModule'
    },
    {
        path        : 'contacts',
        loadChildren: () => import('./contacts/contacts.module').then(m => m.ContactsModule)
        // loadChildren: './contacts/contacts.module#ContactsModule'
    },
    {
        path        : 'scrumboard',
        loadChildren: () => import('./scrumboard/scrumboard.module').then(m => m.ScrumboardModule)
        // loadChildren: './scrumboard/scrumboard.module#ScrumboardModule'
    }
];

@NgModule({
    imports     : [
        RouterModule.forChild(routes),
        SharedUIModule
    ]
})
export class AppsModule {
}
