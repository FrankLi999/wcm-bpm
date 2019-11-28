import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatToolbarModule } from '@angular/material/toolbar';

import { FuseSharedModule } from 'bpw-components';

import { KnowledgeBaseService } from './knowledge-base.service';
import { KnowledgeBaseComponent } from './knowledge-base.component';
import { KnowledgeBaseArticleComponent } from './dialogs/article/article.component';

const routes = [
    {
        path     : 'knowledge-base',
        component: KnowledgeBaseComponent,
        resolve  : {
            knowledgeBase: KnowledgeBaseService
        }
    }
];

@NgModule({
    declarations   : [
        KnowledgeBaseComponent,
        KnowledgeBaseArticleComponent
    ],
    imports        : [
        RouterModule.forChild(routes),

        MatButtonModule,
        MatDialogModule,
        MatIconModule,
        MatListModule,
        MatToolbarModule,

        FuseSharedModule
    ],
    providers      : [
        KnowledgeBaseService
    ],
    entryComponents: [
        KnowledgeBaseArticleComponent
    ]
})
export class KnowledgeBaseModule
{
}
