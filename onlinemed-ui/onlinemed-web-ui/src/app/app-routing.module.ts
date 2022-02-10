import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ErrorMessageComponent} from './components/error-message/error-message.component';
import {LoginComponent} from './core/login/login.component';
import {AuthGuard} from './core/auth/auth.guard';
import {HomeComponent} from './components/home/home.component';
import {UserManagementComponent} from './components/user-management/user-management.component';
import {DrugEquivalentsComponent} from './components/drug-equivalents/drug-equivalents.component';
import {ProfileComponent} from './components/profile/profile.component';
import {DoctorsReviewComponent} from './components/doctors-review/doctors-review.component';
import {NotificationsComponent} from './components/notifications/notifications.component';
import {RegistrationComponent} from './core/registration/registration.component';
import {ForumComponent} from './components/forum/forum.component';
import {ForumCategoriesComponent} from './components/forum/forum-categories/forum-categories.component';
import {NewestForumTopicComponent} from './components/forum/newest-forum-topics/newest-forum-topic.component';
import {CategoryTopicsComponent} from './components/forum/category-topics/category-topics.component';
import {SingleTopicPostsViewComponent} from './components/forum/single-topic-posts-view/single-topic-posts-view.component';
import {NewTopicComponent} from './components/forum/new-topic/new-topic.component';


const routes: Routes = [

  {path: '', redirectTo: 'login', pathMatch: 'full'},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegistrationComponent},
  {path: 'register/:id', component: RegistrationComponent},
  {
    path: 'om', component: HomeComponent, canActivate: [AuthGuard], children: [
      {path: 'error-message', component: ErrorMessageComponent},
      {path: 'user-management', component: UserManagementComponent},
      {path: 'drug-equivalents', component: DrugEquivalentsComponent},
      {path: 'profile', component: ProfileComponent},
      {path: 'doctors-profile', component: DoctorsReviewComponent},
      {path: 'notifications', component: NotificationsComponent},
      {
        path: 'forum', component: ForumComponent, children: [
          {path: 'categories', component: ForumCategoriesComponent},
          {path: 'all-post', component: NewestForumTopicComponent},
          {path: 'categories/**', redirectTo: 'categories', pathMatch: 'full'},
        ]
      },
      {path: 'forum/category-topic/:id', component: CategoryTopicsComponent},
      {path: 'forum/post/:id', component: SingleTopicPostsViewComponent},
      {path: 'forum/new-topic/:id', component: NewTopicComponent},
    ]
  },
  {path: '**', redirectTo: 'login'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
