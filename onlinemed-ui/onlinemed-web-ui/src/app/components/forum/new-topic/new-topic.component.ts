import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ForumPostCtrl, ForumTopicCtrl} from '../../../core/sdk/onlinemed-controllers';
import {EditorComponent} from '../../../shared/components/editor/editor.component';
import {ForumCategory, ForumPost, ForumTopic} from '../../../core/sdk/onlinemed-model';
import {Utility} from '../../../shared/utilities/utility';
import {AuthService} from '../../../core/auth/auth.service';
import {NotificationWrapComponent} from '../../../shared/components/notification-box/notification-wrap.component';
import {Alert, AlertType} from '../../../shared/classes/alert';
import {TranslateService} from '../../../core/translations/translate.service';

@Component({
  selector: 'app-new-topic',
  templateUrl: './new-topic.component.html',
  styles: []
})
export class NewTopicComponent implements OnInit {

  @ViewChild('editorComponent') editorComponent: EditorComponent;
  @ViewChild('topic') topicInput: ElementRef;
  @ViewChild('tags') tagsInput: ElementRef;
  categoryId: string;

  constructor(private forumTopic: ForumTopicCtrl,
              private forumPostCtrl: ForumPostCtrl,
              private translateService: TranslateService,
              private route: ActivatedRoute,
              private router: Router,
              private authService: AuthService) {
  }


  ngOnInit(): void {
    this.translateService.loadTranslationModule('forum');
    this.route.params.subscribe(param => this.categoryId = param.id);
  }

  onClickSave(): void {
    const topicValue = this.topicInput.nativeElement.value;
    const tagsValue = (this.tagsInput.nativeElement.value || '');
    const editorVal = this.editorComponent.editorVal;

    if (this.isNotEmptyContent([topicValue, editorVal])){
      const forumTopic = this.createNewTopic(topicValue, tagsValue);
      this.saveTopic(forumTopic, editorVal);
    }
  }

  private isNotEmptyContent(fields: string[]): boolean {
    for (const val of fields) {
      if (!val || fields.isEmpty()) {
        NotificationWrapComponent.sendAlert(new Alert(AlertType.WARNING, 'Topic and content can not be empty'));
        return false;
      }
    }
    return true;
  }
  private saveTopic(topic: ForumTopic, editorVal: string): void {
    this.forumTopic.createObject(topic).subscribe(res => {
        this.forumPostCtrl.createObject(this.getForumPostObject(editorVal, res.id)).subscribe(_ => {
          Utility.updateSuccessfullyNotification();
          this.router.navigate(['/om/forum/post/' + Utility.getObjectId(res.id)]);
        });
      },
      error => Utility.showViolationsIfOccurs(error));
  }

  private getForumPostObject(message: string, id: string): ForumPost {
    return {
      text: message,
      forumTopic: {id},
      postCreator: {id: this.authService.getCurrentLoggedPerson().id},
      ...Utility.getDefaultBaseObject('forum-post')
    } as ForumPost;
  }

  private createNewTopic(topicValue: string, hasztags: string): ForumTopic {
    return {
      creatorUserName: this.authService.getCurrentLoggedPerson().userName,
      favorites: [],
      forumCategory: {id: 'forum-category/' + this.categoryId} as ForumCategory,
      hasztags,
      title: topicValue,
      ...Utility.getDefaultBaseObject('forum-topic')
    } as ForumTopic;
  }
}
