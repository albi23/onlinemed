import {Injectable} from '@angular/core';
import {ForumCategory} from '../../core/sdk/onlinemed-model';

@Injectable({
  providedIn: 'root'
})
export class ForumCategoryService {

  private categories: ForumCategory[] = [];

  constructor() {
  }

  getCategories(): ForumCategory[] {
    return this.categories || [];
  }

  setCategories(categories: ForumCategory[]): void {
    this.categories = categories;
  }
}
