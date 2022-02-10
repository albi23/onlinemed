import {Component, OnInit, ViewChild} from '@angular/core';
import {Location} from '@angular/common';
import {ActivatedRoute, Router} from '@angular/router';
import {ForumPost, ForumTopic, Person} from '../../../core/sdk/onlinemed-model';
import {Utility} from '../../../shared/utilities/utility';
import {ForumPostCtrl, ForumTopicCtrl} from '../../../core/sdk/onlinemed-controllers';
import {EditorComponent} from '../../../shared/components/editor/editor.component';
import {NotificationWrapComponent} from '../../../shared/components/notification-box/notification-wrap.component';
import {Alert, AlertType} from '../../../shared/classes/alert';
import {AuthService} from '../../../core/auth/auth.service';
import {PostValue} from './single-post-view/single-post-view.component';
import {RoleType} from '../../../core/sdk/model-enums';
import {ModalService} from '../../../shared/services/modal.service';
import {TranslateService} from '../../../core/translations/translate.service';

@Component({
  selector: 'app-single-topic-posts-view',
  templateUrl: './single-topic-posts-view.component.html',
  styles: []
})
export class SingleTopicPostsViewComponent implements OnInit {

  @ViewChild('editorComponent') editorComponent: EditorComponent;
  readonly pageSize: number = 10;
  paginatedPost: ForumPost[] = [];
  currentTopic: ForumTopic;
  currentPostTopicId: string;
  currentPage = 0;
  pageNumbers: number[] = [];
  editPrivileges = false;

  constructor(private location: Location,
              private route: ActivatedRoute,
              private router: Router,
              private forumPostCtrl: ForumPostCtrl,
              private forumTopicCtrl: ForumTopicCtrl,
              private authService: AuthService,
              private translateService: TranslateService,
              private modalService: ModalService) {
    this.route.params.subscribe(param => this.currentPostTopicId = param.id);
    this.editPrivileges = !this.authService.getCurrentLoggedPerson().roles
      .filter(r => (r.roleType === RoleType.MODERATOR) || (r.roleType === RoleType.ADMINISTRATOR)).isEmpty();

  }

  ngOnInit(): void {
    this.translateService.loadTranslationModule('forum');
    this.loadPostForTopic();
    this.countPostForTopic();
    this.getCurrentTopic();
  }

  private getCurrentTopic(): void {
    this.forumTopicCtrl.getObject(this.currentPostTopicId)
      .subscribe(res => this.currentTopic = res,
        error => Utility.showViolationsIfOccurs(error));
  }

  private loadPostForTopic(page: number = this.currentPage): void {
    this.forumPostCtrl.getPaginatedTopicPosts(this.currentPostTopicId, page)
      .subscribe((forumPosts: ForumPost[]) => this.paginatedPost = forumPosts,
        error => Utility.showViolationsIfOccurs(error));
  }

  private countPostForTopic(func?: (pageNumbers: number) => any): void {
    this.forumPostCtrl.getTopicPostCount(this.currentPostTopicId)
      .subscribe((count: number) => {
          this.pageNumbers = [];
          for (let i = 0; i <= Math.floor((count - 1) / this.pageSize); i++) {
            this.pageNumbers.push(i);
          }
          if (func) {
            func(this.pageNumbers.length);
          }
        },
        error => Utility.showViolationsIfOccurs(error));
  }


  onBack(): void {
    this.location.back();
  }


  onChangePage(i: number, update?: boolean): void {
    if (this.currentPage !== i || update) {
      this.currentPage = i;
      this.loadPostForTopic(i);
    }
  }

  onClickSave(): void {
    const message: string = this.editorComponent.editorVal;
    if (message && this.isValidTextLength(message)) {
      this.createNewPost(message);
    } else {
      NotificationWrapComponent.sendAlert(new Alert(AlertType.WARNING, 'forum.to-short-message'));
    }
  }

  private createNewPost(message: string): void {
    const forumTopicObject = this.getForumTopicObject(message);
    this.saveNewPost(forumTopicObject);
  }


  private saveNewPost(newPost: ForumPost): void {
    this.forumPostCtrl.createObject(newPost).subscribe(res => {
        Utility.updateSuccessfullyNotification();
        this.countPostForTopic((pageNumbers: number) => {
          this.onChangePage(pageNumbers - 1, true);
          this.editorComponent.editorVal = '';
        });
      }
      , error => Utility.showViolationsIfOccurs(error));
  }

  private getForumTopicObject(message: string): ForumPost {
    return {
      text: message,
      forumTopic: {id: this.currentTopic.id},
      postCreator: {id: this.authService.getCurrentLoggedPerson().id},
      ...Utility.getDefaultBaseObject('forum-post')
    } as ForumPost;
  }

  onChangePostEvent(postValue: PostValue): void {
    if (postValue.text && this.isValidTextLength(postValue.text)) {
      const postObj = this.paginatedPost.find(s => s.id === postValue.id) as ForumPost;
      const postObjCopy = Utility.clone(postObj) as ForumPost; // weeak copy
      postObjCopy.text = postValue.text;

      this.forumPostCtrl.updateObject(this.mapForumTopicAsProxy(postObjCopy)).subscribe((res: ForumPost) => {
        const idx = this.paginatedPost.findIndex(s => s.id === res.id);
        if (idx >= 0) {
          this.paginatedPost[idx] = res;
        }
      }, error => Utility.showViolationsIfOccurs(error));
    } else {
      NotificationWrapComponent.sendAlert(new Alert(AlertType.WARNING, 'forum.to-short-message'));
    }
  }

  isValidTextLength(message: string): boolean {
    return message.length > 10;
  }

  private mapForumTopicAsProxy(post: ForumPost): ForumPost {
    post.postCreator = {id: post.postCreator.id} as Person;
    post.forumTopic = {id: post.forumTopic.id} as ForumTopic;
    return post;
  }

  onScrollToEditor(): void {
    setTimeout(() => {
      const rowElement: HTMLDivElement = document.getElementById('editor') as HTMLDivElement;
      rowElement.scrollIntoView({behavior: 'smooth'});
    }, 150);
  }

  onClickNew(): void {
    this.router.navigate(['/om/forum/new-topic/' + Utility.getIdFromObject(this.currentTopic.forumCategory)]);
  }

  onRemoveEvt(id: string): void {
    if (Utility.getObjectId(id) === Utility.getIdFromObject(this.currentTopic)) {
      this.deleteTopicAction(id);
    } else {
      this.deletePostAction(id);
    }
  }

  deletePostAction(id: string): void {
    this.modalService.injectDataToModal('Are you sure you want to delete the selected post?', 'common.warning');
    this.modalService.onConfirm(_ => {
      this.removePost(id);
      this.modalService.closeModal();
    });
    this.modalAction();
  }

  deleteTopicAction(id: string): void {
    this.modalService.injectDataToModal('Are you sure you want to delete the selected topic?', 'common.warning');
    this.modalService.onConfirm(_ => {
      this.removeTopic(id);
      this.modalService.closeModal();
    });
    this.modalAction();
  }

  private modalAction(): void {
    this.modalService.onCancel(_ => this.modalService.closeModal());
    this.modalService.openModal();
  }

  private removeTopic(id: string): void {
    this.forumTopicCtrl.deleteObject(Utility.getObjectId(id)).subscribe(res => {
      this.onBack();
    }, error => Utility.showViolationsIfOccurs(error));
  }

  private removePost(id: string): void {
    this.forumPostCtrl.deleteObject(Utility.getObjectId(id)).subscribe(res => {
      this.loadPostForTopic();
      this.countPostForTopic();
    }, error => Utility.showViolationsIfOccurs(error));
  }
}
