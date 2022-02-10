import {Component, ComponentFactoryResolver, EventEmitter, Injector, Input, OnInit, Output, Type} from '@angular/core';
import {BaseObject} from '../../core/sdk/onlinemed-model';
import {DrugInfo} from '../../core/sdk/model-dto';
import {TranslateService} from '../../core/translations/translate.service';
import {BigTableComponent} from '../../shared/components/base-table/big-table';
import {DrugInfoRefEditComponent, EDIT_DRUG_TOKEN, EditDrugInfo} from './drug-info-ref-edit/drug-info-ref-edit.component';

export interface DrugInfoDTO extends BaseObject, DrugInfo {
  id: '0';
  version: 0;
}

@Component({
  selector: 'app-drug-info-table',
  templateUrl: '../../shared/components/base-table/base-table.component.html',
  styles: []
})
export class DrugInfoTableComponent extends BigTableComponent<DrugInfoDTO> implements OnInit {

  @Output() linkEmitter: EventEmitter<string> = new EventEmitter<string>();

  @Input() set setPageItems(drugInfoDTO: DrugInfoDTO[]) {
    if (this.pageItems.length === 0 && drugInfoDTO) {
      this.pageItems = drugInfoDTO;
      this.pageSize = drugInfoDTO.length;
    }
  }

  pagination = false;
  checkBoxMandatory = false;
  selectionEnabled = false;
  multiSelectionEnabled = false;
  editDisabled = true;
  columns: any[] = ['drugName', 'drugForm', 'dose', 'box', 'payment', 'patientPrice', 'fullPrice', 'urlEquivalent'];
  unSortableColumns: string[] = [...this.columns];
  specificColumnName = 'urlEquivalent';
  currentComponent: Type<DrugInfoRefEditComponent> = DrugInfoRefEditComponent;

  constructor(protected translateService: TranslateService,
              protected resolver: ComponentFactoryResolver,
              private inj: Injector) {
    super(translateService, resolver);
  }

  protected executeCountQuery(): void {
  }

  ngOnInit(): void {
  }


  cancelEdition(): void {
  }

  deleteItemAction(item: DrugInfoDTO): void {
  }

  editItem(editedItem: DrugInfoDTO): void {
  }

  getColumnNumber(): number {
    return this.columns.length;
  }

  getColumnTranslation(column: string): string {
    return this.translateService.translate('drug-equivalents.' + column);
  }

  getColumns(): string[] {
    return this.columns;
  }

  getValueFromItem(item: DrugInfoDTO, column: string): any {
    switch (column) {
      case 'drugName':
        return item.drugName;
      case 'urlEquivalent':
        return '';
      case 'drugForm':
        return item.drugForm;
      case 'dose':
        return item.dose;
      case 'box':
        return item.box;
      case 'payment':
        return item.payment;
      case 'patientPrice':
        return item.patientPrice;
      case 'fullPrice':
        return item.fullPrice;
    }
  }

  itemCreated(item: DrugInfoDTO): void {
  }

  onChange(): void {
  }

  updateItem(item: DrugInfoDTO): void {
  }

  updateValues(item: DrugInfoDTO, column: string, newValue: string): any {
  }


  executeQuery(): void {
  }

  defineExtraCondition(item: DrugInfoDTO): boolean {
    this.createDynamicComponent(item);
    return super.defineExtraCondition(item);
  }

  private createDynamicComponent(item: DrugInfoDTO): void {
    this.currentInjector = Injector.create({
      providers: [
        {provide: EditDrugInfo, deps: [EDIT_DRUG_TOKEN]},
        {provide: EDIT_DRUG_TOKEN, useValue: {link: item.urlEquivalent, linkEmitter: this.linkEmitter}},
      ],
      parent: this.inj,
    });
  }
}
