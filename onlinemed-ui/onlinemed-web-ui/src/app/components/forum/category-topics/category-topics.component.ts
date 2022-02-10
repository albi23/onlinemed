import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ForumTopic} from '../../../core/sdk/onlinemed-model';
import {ForumTopicCtrl} from '../../../core/sdk/onlinemed-controllers';
import {Utility} from '../../../shared/utilities/utility';
import {TranslateService} from '../../../core/translations/translate.service';
import {AuthService} from '../../../core/auth/auth.service';
import {ForumTopicUpdaterDirective} from '../forum-topic-updater.directive';

@Component({
  selector: 'app-forum-topics',
  templateUrl: './category-topics.component.html',
  styles: []
})
export class CategoryTopicsComponent extends ForumTopicUpdaterDirective implements OnInit {

  currentCategoryId: string;
  categoryTopics: ForumTopic[] = [];
  constructor(private router: Router,
              private route: ActivatedRoute,
              private translateService: TranslateService,
              protected authService: AuthService,
              protected forumTopicCtrl: ForumTopicCtrl) {
    super(authService, forumTopicCtrl);
    this.route.params.subscribe(param => this.currentCategoryId = param.id);
  }

  ngOnInit(): void {
    this.translateService.loadTranslationModule('forum');
    this.forumTopicCtrl.getCategoryTopics(this.currentCategoryId)
      .subscribe((topics: ForumTopic[]) => {
          this.categoryTopics = topics;
        },
        error => Utility.showViolationsIfOccurs(error));
  }

  onBackClick(): void {
    this.router.navigate(['/om/forum/categories']);
  }

  onClickNew(): void {
    this.router.navigate(['/om/forum/new-topic/' + Utility.getObjectId(this.currentCategoryId)]);
  }

  getForumTopics(): ForumTopic[] {
    return this.categoryTopics;
  }

}
