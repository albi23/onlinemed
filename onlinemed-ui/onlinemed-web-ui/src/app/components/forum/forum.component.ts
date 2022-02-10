import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {TranslateService} from '../../core/translations/translate.service';

export enum Tabs {
  Categories = 'Categories',
  AllPosts = 'All posts',
}

@Component({
  selector: 'app-forum',
  templateUrl: './forum.component.html',
  styles: []
})
export class ForumComponent implements OnInit {

  forumTabs: { key: string }[] = [
    { key: 'forum.categories'},
    { key: 'forum.all-post'}
  ];
  selectedLocationIndex = 0;

  constructor(private router: Router,
              private translateService: TranslateService) {
  }

  ngOnInit(): void {
    this.translateService.loadTranslations('forum');
    this.onClickTab(this.forumTabs[0].key, 0);
  }


  onClickTab(location: string, index: number): void {
    this.selectedLocationIndex = index;
    this.router.navigate(['/om/forum/' + location.split('.')[1]]);
  }
}
