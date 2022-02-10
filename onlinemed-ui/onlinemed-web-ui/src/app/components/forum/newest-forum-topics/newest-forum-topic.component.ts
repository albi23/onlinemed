import {Component, OnInit} from '@angular/core';
import {ForumTopicCtrl} from '../../../core/sdk/onlinemed-controllers';
import {ForumTopic} from '../../../core/sdk/onlinemed-model';
import {Utility} from '../../../shared/utilities/utility';
import {AuthService} from '../../../core/auth/auth.service';
import {ForumTopicUpdaterDirective} from '../forum-topic-updater.directive';

@Component({
  selector: 'app-all-posts',
  templateUrl: './newest-forum-topic.component.html',
  styles: []
})
export class NewestForumTopicComponent extends ForumTopicUpdaterDirective implements OnInit {

  forumTopicsPage: ForumTopic[] = [];

  constructor(protected authService: AuthService,
              protected forumTopicCtrl: ForumTopicCtrl) {
    super(authService, forumTopicCtrl);
  }

  ngOnInit(): void {
    this.forumTopicCtrl.getNewestTopics().subscribe(res => {
      this.forumTopicsPage = res;

    }, error => Utility.showViolationsIfOccurs(error));
  }

  getForumTopics(): ForumTopic[] {
    return this.forumTopicsPage;
  }




}
