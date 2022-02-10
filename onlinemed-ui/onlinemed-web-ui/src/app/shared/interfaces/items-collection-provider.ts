import {BaseObject} from '../../core/sdk/onlinemed-model';

export interface ItemsCollectionProvider<T extends BaseObject> {
  selectedItems: { [id: string]: T };

  getItemClone(item: T): any;

  itemCreated(item: T): void;

  itemUpdated(item: T): void;

  itemDeleted(item: T): void;
}
