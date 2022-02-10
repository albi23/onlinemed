import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {ErrorMessageComponent} from './components/error-message/error-message.component';
import {TranslatePipe} from './core/translations/translate.pipe';
import {
  CalendarEventCtrl,
  CommunityCtrl,
  DoctorInfoCtrl,
  DrugEquivalentsCtrl,
  EmailSendCtrl,
  ForumCategoryCtrl,
  ForumPostCtrl,
  ForumTopicCtrl,
  LanguageCtrl,
  LoginCtrl,
  NotificationCtrl,
  PersonCtrl,
  RegistrationLinkCtrl,
  RoleCtrl,
  StaticTranslationCtrl
} from './core/sdk/onlinemed-controllers';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {LoginComponent} from './core/login/login.component';
import {FormsModule} from '@angular/forms';
import {NotificationWrapComponent} from './shared/components/notification-box/notification-wrap.component';
import {NotificationComponent} from './shared/components/notification-box/notification/notification.component';
import {HomeComponent} from './components/home/home.component';
import {SideBarComponent} from './components/side-bar/side-bar.component';
import {UserManagementComponent} from './components/user-management/user-management.component';
import {UserManagementTableComponent} from './components/user-management/user-managment-table';
import {UserManagementEditComponent} from './components/user-management/user-management-edit/user-management-edit.component';
import {AuthTokenInterceptor} from './core/auth/auth-token.interceptor';
import {NgMultiSelectDropDownModule} from 'ng-multiselect-dropdown';
import {SpinnerComponent} from './shared/components/spinner/spinner.component';
import {ModalComponent} from './shared/components/modal/modal.component';
import {DrugEquivalentsComponent} from './components/drug-equivalents/drug-equivalents.component';
import {DrugHintsComponent} from './components/drug-equivalents/drug-hints/drug-hints.component';
import {DrugInfoTableComponent} from './components/drug-equivalents/drug-info-table.component';
import {DrugInfoRefEditComponent} from './components/drug-equivalents/drug-info-ref-edit/drug-info-ref-edit.component';
import {ProfileComponent} from './components/profile/profile.component';
import {UserInfoComponent} from './components/profile/user-info/user-info.component';
import {CommunityComponent} from './components/profile/community/community.component';
import {CalendarComponent} from './shared/components/calendar/calendar.component';
import {CalendarModule, DateAdapter} from 'angular-calendar';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {adapterFactory} from 'angular-calendar/date-adapters/date-fns';
import {FlatpickrModule} from 'angularx-flatpickr';
import {DoctorsReviewComponent} from './components/doctors-review/doctors-review.component';
import {MessageSenderComponent} from './shared/components/message-sender/message-sender.component';
import {NotificationsComponent} from './components/notifications/notifications.component';
import {NewVisitComponent} from './components/doctors-review/new-visit/new-visit.component';
import {RegistrationComponent} from './core/registration/registration.component';
import {LinkGenerationComponent} from './components/user-management/link-generation/link-generation.component';
import {ForumComponent} from './components/forum/forum.component';
import {ForumCategoriesComponent} from './components/forum/forum-categories/forum-categories.component';
import {NewestForumTopicComponent} from './components/forum/newest-forum-topics/newest-forum-topic.component';
import {CategoryTopicsComponent} from './components/forum/category-topics/category-topics.component';
import {SingleTopicViewComponent} from './components/forum/single-topic-view/single-topic-view.component';
import {NewTopicComponent} from './components/forum/new-topic/new-topic.component';
import {SingleTopicPostsViewComponent} from './components/forum/single-topic-posts-view/single-topic-posts-view.component';
import {SinglePostViewComponent} from './components/forum/single-topic-posts-view/single-post-view/single-post-view.component';
import {RichTextEditorModule} from '@syncfusion/ej2-angular-richtexteditor';
import {EditorComponent} from './shared/components/editor/editor.component';
import {SanitizeHtmlPipePipe} from './shared/pipes/sanitize-html-pipe.pipe';
import {ForumTempleComponent} from './components/forum/forum-temple.component';

@NgModule({
  declarations: [
    AppComponent,
    ErrorMessageComponent,
    TranslatePipe,
    LoginComponent,
    NotificationWrapComponent,
    NotificationComponent,
    HomeComponent,
    SideBarComponent,
    UserManagementComponent,
    UserManagementTableComponent,
    UserManagementEditComponent,
    SpinnerComponent,
    ModalComponent,
    DrugEquivalentsComponent,
    DrugHintsComponent,
    DrugInfoTableComponent,
    DrugInfoRefEditComponent,
    ProfileComponent,
    UserInfoComponent,
    CommunityComponent,
    CalendarComponent,
    DoctorsReviewComponent,
    MessageSenderComponent,
    NotificationsComponent,
    NewVisitComponent,
    RegistrationComponent,
    LinkGenerationComponent,
    ForumComponent,
    ForumCategoriesComponent,
    NewestForumTopicComponent,
    CategoryTopicsComponent,
    SingleTopicViewComponent,
    NewTopicComponent,
    SingleTopicPostsViewComponent,
    SinglePostViewComponent,
    EditorComponent,
    SanitizeHtmlPipePipe,
    ForumTempleComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    RichTextEditorModule,
    FlatpickrModule.forRoot(),
    NgMultiSelectDropDownModule.forRoot(),
    BrowserAnimationsModule,
    CalendarModule.forRoot({
      provide: DateAdapter,
      useFactory: adapterFactory,
    }),
  ],
  providers: [
    LanguageCtrl,
    LoginCtrl,
    StaticTranslationCtrl,
    PersonCtrl,
    RegistrationLinkCtrl,
    DoctorInfoCtrl,
    DrugEquivalentsCtrl,
    EmailSendCtrl,
    RoleCtrl,
    CommunityCtrl,
    CalendarEventCtrl,
    NotificationCtrl,
    ForumCategoryCtrl,
    ForumTopicCtrl,
    ForumPostCtrl,
    {provide: HTTP_INTERCEPTORS, useClass: AuthTokenInterceptor, multi: true},
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
