import {AfterViewInit, ComponentFactoryResolver, Directive, ElementRef, Injector, Type, ViewChild} from '@angular/core';
import {BaseObject} from '../../../core/sdk/onlinemed-model';
import {SelectableItemsDirective} from '../../directives/selectable-items.directive';
import {TranslateService} from '../../../core/translations/translate.service';
import {Utility} from '../../utilities/utility';

@Directive()
// tslint:disable-next-line:directives-class-suffix
export abstract class BaseTable<T extends BaseObject> extends SelectableItemsDirective<T> implements AfterViewInit{

  @ViewChild('pageButtons') pageRouts: ElementRef;
  protected unSortableColumns: string[] = [];
  lastPageNumber: number;
  editedItem: T | null;
  editDisabled = true;
  currentInjector: Injector;
  currentComponent: Type<any>;
  specificColumnName: string;
  private currentPageNumber = 0;
  pageSize = 5;
  sortByColumn: any;
  ascending = true;
  pagination = true;

  protected constructor(protected translateService: TranslateService,
                        protected resolver: ComponentFactoryResolver) {
    super();
  }

  getAdditionalCSSClass(): string {
    return '';
  }

  public abstract getColumnNumber(): number;

  public abstract getColumns(): string[];

  public abstract getValueFromItem(item: T, column: string): any;

  public abstract updateValues(item: T, column: string, newValue: string): any;

  public abstract editItem(editedItem: T): void;

  public abstract cancelEdition(): void;

  public abstract getColumnTranslation(column: string): string;

  public abstract sortItems(): void;

  protected abstract getItemsNumber(): number;

  public abstract getCurrentPage(): T[];

  public abstract deleteItemAction(item: T): void;

  public abstract updateItem(item: T): void;

  get pageNumber(): number {
    return this.currentPageNumber;
  }

  set pageNumber(value: number) {
    this.currentPageNumber = value;
    if (this.currentPageNumber > this.getLastPageNumber()) {
      this.currentPageNumber = 0;
    }
  }

  public getLastPageNumber(): number {
    if (this.getItemsNumber() === 0) {
      return 0;
    }
    this.lastPageNumber = Math.floor((this.getItemsNumber() - 1) / this.pageSize);
    return this.lastPageNumber;
  }

  public changeSelectionAction(): void {
    if (!this.changeSelection) {
      for (const currentPage of this.getCurrentPage()) {
        this.removeFromSelected(currentPage);
      }
    } else {
      for (const currentPage of this.getCurrentPage()) {
        this.addToSelected(currentPage);
      }
    }
  }

  public getThumbnail(item: T): any {
    // TODO Yet to be implemented
    return item;
  }


  public sortByChanged(sortByColumn: string): void {
    if (this.isSortableColumn(sortByColumn)) {
      if (this.sortByColumn !== sortByColumn) {
        this.sortByColumn = sortByColumn;
        this.ascending = true;
      } else {
        this.ascending = !this.ascending;
      }
      this.sortItems();
    }
  }

  public isSortableColumn(sortByColumn): boolean {
    if (sortByColumn[`id`]) {
      sortByColumn = Utility.getObjectType(sortByColumn[`id`]);
    }
    return this.unSortableColumns.findIndex(col => col === sortByColumn) === -1;
  }

  public getColumnLabel(column: string): string {
    if (!this.sortByColumn) {
      this.sortByColumn = this.getColumns()[0];
    }
    let result = this.getColumnTranslation(column);
    if (this.isSortableColumn(column)) {
      if (column === this.sortByColumn) {
        if (this.ascending) {
          result = '<i class="fa fa-sort-up"></i> ' + result;
        } else {
          result = '<i class="fa fa-sort-down"></i> ' + result;
        }
      }
    }
    return result;
  }

  // TO-DO Fix translations
  public getPageDescription(): string {
    const firstItemOnPageNumber = this.pageNumber * this.pageSize;
    if (this.getCurrentPage().length < 1) {
      return this.translateService.translate('common.page') + ' ' + (this.pageNumber + 1) + ' of ' + (this.getLastPageNumber() + 1)
        + '  ' + this.translateService.translate('common.items') + ' ' + firstItemOnPageNumber + ' of ' + this.getItemsNumber();
    } else {
      return this.translateService.translate('common.page') + ' ' + (this.pageNumber + 1) + ' of ' + (this.getLastPageNumber() + 1)
        + '  ' + this.translateService.translate('common.items') + ' ' + (firstItemOnPageNumber + 1) + '-' + (firstItemOnPageNumber
          + this.getCurrentPage().length) + ' of ' + this.getItemsNumber();
    }
  }

  public getPadding(): number[] {
    const padding: number[] = [];
    const currentPageSize = this.getCurrentPage().length;
    for (let i = 0; i < (this.pageSize - currentPageSize); i++) {
      padding.push(i);
    }
    return padding;
  }

  public isEmpty(): boolean {
    return this.getItemsNumber() === 0;
  }

  public isFirstPage(): boolean {
    return this.pageNumber === 0;
  }

  public isLastPage(): boolean {
    return this.pageNumber === this.getLastPageNumber();
  }

  public goToFirstPage(): number {
    this.changeSelection = false;
    return this.pageNumber = 0;
  }

  public goToLastPage(): number {
    this.changeSelection = false;
    return this.pageNumber = this.getLastPageNumber();
  }

  public previousPage(): void {
    if (this.pageNumber > 0) {
      this.changeSelection = false;
      this.pageNumber--;
    }
  }

  public nextPage(): void {
    if (this.pageNumber < this.getLastPageNumber()) {
      this.changeSelection = false;
      this.pageNumber++;
    }
  }

  getAdditionalRowClass(item: T): any | string {
    if (this.selectedItems[item.id]) {
      return 'bv-selected-list-item';
    }
  }

  checkScreenSize(): boolean {
    return Utility.isSmallScreen();
  }


  ngAfterViewInit(): void {
    if (this.pagination) {
      const nodes: HTMLElement[] = this.pageRouts.nativeElement.children;
      const translationsMapping = [
        [0, 'move-to-begin'], [1, 'move-to-previous'],
        [3, 'move-to-next'], [4, 'move-to-last']
      ];
      for (const i of translationsMapping) {
        nodes[i[0]].setAttribute('data-tooltip', this.translateService.translate('common.' + i[1] as string));
      }
    }
  }
}
