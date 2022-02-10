import {ComponentFactoryResolver, Directive, Input} from '@angular/core';
import {BaseObject} from '../../../core/sdk/onlinemed-model';
import {BaseTable} from './base-table';
import {TranslateService} from '../../../core/translations/translate.service';
import {Utility} from '../../utilities/utility';

@Directive()
// tslint:disable-next-line:directive-class-suffix
export abstract class SmallTable<T extends BaseObject> extends BaseTable<T> {
  items: T[] = [];

  protected constructor(protected translateService: TranslateService,
                        protected resolver: ComponentFactoryResolver){
    super(translateService, resolver);
  }

  @Input() set itemsInput(items: T[]) {
    this.items = items;
    if (items) {
      this.sortItems();
    }
  }

  public abstract getItems(): T[];

  itemCreated(item: T): void {
    this.selectedItemsClones[item.id] = (Utility.clone(item) as T);
    this.getItems().push(item);
    this.sortItems();
    this.setActiveItem(item);
  }

  itemUpdated(item: T): void {
    const index = this.getItems().findIndex(nextItem => nextItem.id === item.id);
    if (index > -1) {
      this.getItems()[index] = item;
      this.selectedItemsClones[item.id] = (Utility.clone(item) as T);
      this.sortItems();
    }
  }

  itemDeleted(item: T): void {
    const index = this.getItems().findIndex(nextItem => nextItem.id === item.id);
    if (index > -1) {
      this.getItems().splice(index, 1);
    }
  }

  public getItemsNumber(): number {
    return this.getItems().length;
  }

  public getCurrentPage(): T[] {
    return this.getItems().slice(this.pageNumber * this.pageSize, (this.pageNumber + 1) * this.pageSize);
  }

  public compare(item1: T, item2: T): number {
    const direction = this.ascending ? 1 : -1;
    const a = this.getValueFromItem(item1, this.sortByColumn);
    const b = this.getValueFromItem(item2, this.sortByColumn);
    const first = (a !== null && a !== undefined) ? a : b;
    const second = (first === a) ? b : a;
    if (first === undefined || first === null) {
      return 0;
    }

    if ('number' === (typeof first) && second) {
      return direction * (first - second);
    }

    return direction * first.localeCompare(second);

  }

  public sortItems(): void {
    this.getItems().sort((a, b) => this.compare(a, b));
  }
}
