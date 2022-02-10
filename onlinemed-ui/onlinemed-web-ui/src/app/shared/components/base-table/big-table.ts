import {BaseTable} from './base-table';
import {BaseObject} from '../../../core/sdk/onlinemed-model';
import {Utility} from '../../utilities/utility';

export abstract class BigTableComponent<T extends BaseObject> extends BaseTable<T> {
  private loadedPageNumber = -1;
  private countExecutedForPage = -1;
  private numberOfItems = 0;
  protected pageItems: T[] = [];

  get itemsNumber(): number {
    return this.numberOfItems;
  }

  set itemsNumber(value: number) {
    this.numberOfItems = value;
    if (this.getLastPageNumber() < this.pageNumber) {
      this.goToFirstPage();
    }
  }

  itemCreated(item: T): void {
    this.numberOfItems++;
    this.executeQuery();
    this.selectedItemsClones[item.id] = (Utility.clone(item) as T);
  }

  itemUpdated(item: T): void {
    this.executeQuery();
    this.executeCountQuery();
    this.selectedItemsClones[item.id] = (Utility.clone(item) as T);
  }

  itemDeleted(item: T): void {
    this.numberOfItems--;
    this.executeQuery();
  }

  public sortItems(): void {
    this.executeQuery();
  }

  protected getItemsNumber(): number {
    if ((this.countExecutedForPage !== this.pageNumber) &&
      (this.pageNumber === this.lastPageNumber || this.pageNumber === (this.lastPageNumber - 1))) {
      this.executeCountQuery();
    }
    if (this.countExecutedForPage === -1) {
      this.executeCountQuery();
    }
    this.countExecutedForPage = this.pageNumber;

    return this.numberOfItems;
  }

  public getCurrentPage(): T[] {
    if (this.pageNumber !== this.loadedPageNumber) {
      this.loadedPageNumber = this.pageNumber;
      this.executeQuery();
    }
    return this.pageItems;
  }

  refresh(): void {
    this.executeQuery();
    this.executeCountQuery();
  }

  changeSelectionAction(): void {
    super.changeSelectionAction();
  }

  abstract executeQuery(): void;

  protected abstract executeCountQuery(): void;

  defineExtraCondition(item: T): boolean{
    return true;
  }
}
