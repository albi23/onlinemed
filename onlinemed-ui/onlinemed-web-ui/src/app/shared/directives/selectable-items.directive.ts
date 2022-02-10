import {Directive, EventEmitter, Output} from '@angular/core';
import {BaseObject} from '../../core/sdk/onlinemed-model';
import {ItemsCollectionProvider} from '../interfaces/items-collection-provider';
import {Utility} from '../utilities/utility';

@Directive()
export abstract class SelectableItemsDirective<T extends BaseObject> implements ItemsCollectionProvider<T> {
  @Output() activeItemChanged = new EventEmitter<T>();

  selectedItems: { [id: string]: T } = {};
  selectedItemsClones: { [id: string]: T } = {};
  ignoreGuardFlag = false;
  changeSelection = false;
  activeItem: T;
  checkBoxMandatory = true;
  selectionEnabled = true;
  multiSelectionEnabled = true;

  protected constructor() {
  }

  abstract itemCreated(item: T): void;

  abstract itemUpdated(item: T): void;

  abstract itemDeleted(item: T): void;

  abstract onChange(): void;

  getItemClone(item: T): T {
    if (Utility.isNewObject(item)) {
      return item;
    }
    return this.selectedItemsClones[item.id];
  }

  handleRowClick(item: T): void {
    this.changeOneSelectionAction(item);
  }

  public changeOneSelectionAction(item: T): void {
    if (this.selectionEnabled) {
      if (this.multiSelectionEnabled) {
        if (!this.selectedItems[item.id]) {
          this.addToSelected(item);
        } else {
          this.removeFromSelected(item);
        }
      } else {
        this.selectedItems = {};
        this.selectedItemsClones = {};
        this.addToSelected(item);
      }
    }
  }

  findNewActiveItem(): void {
    let index;
    for (const i of Object.keys(this.selectedItems)) {
      index = i;
    }
    this.setActiveItem(this.selectedItems[index]);
  }

  setActiveItem(item: T): void {
    this.activeItem = item;
    this.activeItemChanged.emit(item);
    this.onChange();
  }

  isActive(item: T): boolean {
    return (this.activeItem) && item.id === this.activeItem.id;
  }


  addToSelectedGuard(item: T): void {
    this.selectedItems[item.id] = item;
    this.selectedItemsClones[item.id] = (Utility.clone(item) as T);
    this.setActiveItem(item);
  }

  removeFromSelectedGuard(item: T): void {
    let changeItem = false;
    if (this.isActive(item)) {
      changeItem = true;
    }
    delete this.selectedItems[item.id];
    delete this.selectedItemsClones[item.id];
    if (changeItem || Object.keys(this.selectedItems).length === 0) {
      this.findNewActiveItem();
    }
  }

  removeAllSelected(): void {
    this.selectedItems = {};
    this.selectedItemsClones = {};
  }

  addToSelected(item: T): void {
    this.addToSelectedGuard(item);
  }

  removeFromSelected(item: T): void {
    this.removeFromSelectedGuard(item);
  }
}
