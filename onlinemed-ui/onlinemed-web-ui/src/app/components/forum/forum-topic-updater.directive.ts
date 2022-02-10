import {Directive} from '@angular/core';
import {AuthService} from '../../core/auth/auth.service';
import {ForumTopic} from '../../core/sdk/onlinemed-model';
import {Utility} from '../../shared/utilities/utility';
import {ForumTopicCtrl} from '../../core/sdk/onlinemed-controllers';

@Directive()
export abstract class ForumTopicUpdaterDirective {

  private updatePost: Map<string, boolean> = new Map<string, boolean>();
  protected currentUserLikePost: boolean;
  protected currentPersonId: string;

  protected constructor(protected authService: AuthService,
                        protected forumTopicCtrl: ForumTopicCtrl) {
    this.currentPersonId = Utility.getObjectId(this.authService.getCurrentLoggedPerson().id);
  }


  onTopicEvent(forumTopic: ForumTopic): void {
    this.updatePost.set(forumTopic.id, true);
    const id = this.currentPersonId;
    const idx = forumTopic.favorites.findIndex(s => s === id);
    if (idx > -1) {
      forumTopic.favorites.splice(idx, 1);
    } else {
      forumTopic.favorites.push(id);
    }
    this.forumTopicCtrl.updateObject(forumTopic).subscribe((res: ForumTopic) => {
      const topicIdx = this.getForumTopics().findIndex(topic => topic.id === res.id);
      if (topicIdx > -1) {
        this.getForumTopics()[topicIdx] = res;
      }
      this.updatePost.delete(forumTopic.id);
    }, error => {
      Utility.showViolationsIfOccurs(error);
      this.updatePost.delete(forumTopic.id);
    });
  }

  abstract getForumTopics(): ForumTopic[];

  isTopicInUpdateProcess(topicId: string): boolean {
    return this.updatePost.get(topicId) === true;
  }

  isCurrentPersonLikeTopic(likes: string[]): boolean {
    return likes.findIndex(tc => tc === this.currentPersonId) > -1;
  }
}
