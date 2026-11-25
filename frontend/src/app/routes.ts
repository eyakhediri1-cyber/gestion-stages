import { Routes } from '@angular/router';
import { StudentProfileComponent } from './features/student/pages/profile/student-profile';

export const routes: Routes = [
  {
    path: 'student/profile',
    component: StudentProfileComponent
  },
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  }
];
