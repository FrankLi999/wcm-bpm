import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';
import { RouterModule, Routes } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatRippleModule } from '@angular/material/core';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatMenuModule } from '@angular/material/menu';
import { MatSelectModule } from '@angular/material/select';
import { MatToolbarModule } from '@angular/material/toolbar';
import { TranslateModule } from '@ngx-translate/core';

import { FuseSharedModule, FuseSidebarModule } from 'bpw-components';

import * as fromGuards from './store/guards/index';
import { MailNgrxStoreModule } from './store/store.module';
import { MailNgrxComponent } from './mail.component';
import { MailNgrxListComponent } from './mail-list/mail-list.component';
import { MailNgrxListItemComponent } from './mail-list/mail-list-item/mail-list-item.component';
import { MailNgrxDetailsComponent } from './mail-details/mail-details.component';
import { MailNgrxMainSidebarComponent } from './sidebars/main/main-sidebar.component';
import { MailNgrxComposeDialogComponent } from './dialogs/compose/compose.component';
import { MailNgrxService } from './mail.service';

const routes: Routes = [
    {
        path       : 'label/:labelHandle',
        component  : MailNgrxComponent,
        canActivate: [fromGuards.ResolveGuard]
    },
    {
        path       : 'label/:labelHandle/:mailId',
        component  : MailNgrxComponent,
        canActivate: [fromGuards.ResolveGuard]
    },
    {
        path       : 'filter/:filterHandle',
        component  : MailNgrxComponent,
        canActivate: [fromGuards.ResolveGuard]
    },
    {
        path       : 'filter/:filterHandle/:mailId',
        component  : MailNgrxComponent,
        canActivate: [fromGuards.ResolveGuard]
    },
    {
        path       : ':folderHandle',
        component  : MailNgrxComponent,
        canActivate: [fromGuards.ResolveGuard]
    },
    {
        path       : ':folderHandle/:mailId',
        component  : MailNgrxComponent,
        canActivate: [fromGuards.ResolveGuard]
    },
    {
        path      : '**',
        redirectTo: 'inbox'
    }
];

@NgModule({
    declarations   : [
        MailNgrxComponent,
        MailNgrxListComponent,
        MailNgrxListItemComponent,
        MailNgrxDetailsComponent,
        MailNgrxMainSidebarComponent,
        MailNgrxComposeDialogComponent
    ],
    imports        : [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        FlexLayoutModule,
        RouterModule.forChild(routes),

        MatButtonModule,
        MatCheckboxModule,
        MatDialogModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        MatMenuModule,
        MatRippleModule,
        MatSelectModule,
        MatToolbarModule,

        TranslateModule,

        FuseSharedModule,
        FuseSidebarModule,

        MailNgrxStoreModule
    ],
    providers      : [
        MailNgrxService,
        fromGuards.ResolveGuard
    ],
    entryComponents: [MailNgrxComposeDialogComponent]
})
export class MailNgrxModule
{
}
