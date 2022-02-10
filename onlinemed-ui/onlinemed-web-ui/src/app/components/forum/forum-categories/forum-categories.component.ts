import {Component, OnInit} from '@angular/core';
import {ForumCategory} from '../../../core/sdk/onlinemed-model';
import {ForumCategoryService} from '../../../shared/services/forum-category.service';
import {Utility} from '../../../shared/utilities/utility';
import {ForumCategoryCtrl} from '../../../core/sdk/onlinemed-controllers';
import {Router} from '@angular/router';

@Component({
  selector: 'app-forum-categories',
  templateUrl: './forum-categories.component.html',
  styles: []
})
export class ForumCategoriesComponent implements OnInit {

  categories: ForumCategory[] = [];

  constructor(private forumCategoryCtrl: ForumCategoryCtrl,
              private forumCategoryService: ForumCategoryService,
              private router: Router) {
  }

  ngOnInit(): void {
    if (this.forumCategoryService.getCategories().isEmpty()) {
      this.forumCategoryCtrl.findAll().subscribe(res => {
          this.categories = res || [];
          this.forumCategoryService.setCategories([...res]);
        }
        , error => Utility.showViolationsIfOccurs(error));
    } else {
      this.categories = this.forumCategoryService.getCategories();
    }
  }

  onClickCategory(categoryId: string): void {
    this.router.navigate(['/om/forum/category-topic/' + Utility.getObjectId(categoryId)]);
  }
}
