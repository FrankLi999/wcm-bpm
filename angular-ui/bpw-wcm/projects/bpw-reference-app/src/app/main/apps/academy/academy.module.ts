import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';
import { RouterModule } from '@angular/router';

import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';

import { SharedUIModule, SidebarModule } from 'bpw-common';

import { AcademyCoursesComponent } from './courses/courses.component';
import { AcademyCourseComponent } from './course/course.component';
import { AcademyCoursesService } from './courses.service';
import { AcademyCourseService } from './course.service';

const routes = [
    {
        path     : 'courses',
        component: AcademyCoursesComponent,
        resolve  : {
            academy: AcademyCoursesService
        }
    },
    {
        path     : 'courses/:courseId/:courseSlug',
        component: AcademyCourseComponent,
        resolve  : {
            academy: AcademyCourseService
        }
    },
    {
        path      : '**',
        redirectTo: 'courses'
    }
];

@NgModule({
    declarations: [
        AcademyCoursesComponent,
        AcademyCourseComponent
    ],
    imports     : [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        FlexLayoutModule,
        RouterModule.forChild(routes),

        MatButtonModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        MatSelectModule,

        SharedUIModule,
        SidebarModule
    ],
    providers   : [
        AcademyCoursesService,
        AcademyCourseService
    ]
})
export class AcademyModule {
}
