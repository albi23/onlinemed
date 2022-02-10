import {AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {TranslateService} from '../../core/translations/translate.service';
import {DrugEquivalentsCtrl} from '../../core/sdk/onlinemed-controllers';
import {NotificationWrapComponent} from '../../shared/components/notification-box/notification-wrap.component';
import {Alert, AlertType} from '../../shared/classes/alert';
import {Utility} from '../../shared/utilities/utility';
import {DrugHints, DrugInfo} from '../../core/sdk/model-dto';
import {DrugInfoDTO, DrugInfoTableComponent} from './drug-info-table.component';

@Component({
  selector: 'app-drug-equivalents',
  templateUrl: './drug-equivalents.component.html',
  styles: []
})
export class DrugEquivalentsComponent implements OnInit, AfterViewInit {
  @ViewChild('input') searchInput: ElementRef;
  @ViewChild('drugEquivalentsContainer') drugEquivalentsContainer: ElementRef;
  @ViewChild('drugInfoTable') drugInfoTable: DrugInfoTableComponent;
  isDrugInfoTableActive = false;
  isActiveSpinner = false;
  resultDrugHints: DrugHints[] | null;
  drugInfoDTOS: DrugInfoDTO[];

  constructor(private translateService: TranslateService,
              private drugEquivalentsCtrl: DrugEquivalentsCtrl) {
  }

  ngOnInit(): void {
    this.translateService.loadTranslationModule('drug-equivalents');
  }

  ngAfterViewInit(): void {
    this.bindKey();
  }


  getPlaceHolder(): string {
    return this.translateService.translate('drug-equivalents.search-placeholder');
  }

  onClickSearch(): void {
    this.isDrugInfoTableActive = false;
    const message = this.searchInput.nativeElement.value;
    if (!message || message.isEmpty()) {
      NotificationWrapComponent.sendAlert(new Alert(AlertType.WARNING, 'drug-equivalents.empty-search-fraze'));
      return;
    }
    this.searchInput.nativeElement.value = '';
    this.isActiveSpinner = true;
    this.drugEquivalentsCtrl.searchDrugHints(message).subscribe((resp) => {
      this.resultDrugHints = resp;
      this.isActiveSpinner = false;
    }, error => {
      Utility.showViolationsIfOccurs(error);
      this.isActiveSpinner = false;
    });
  }

  private loadTableData(reqParam: string): void {
    this.isActiveSpinner = true;
    this.drugEquivalentsCtrl.searchDrugInfo(reqParam).subscribe((res: DrugInfo[]) => {
        this.drugInfoDTOS = res.map(drugInfo => {
          return {id: '0', version: 0, ...drugInfo} as DrugInfoDTO;
        });

        this.isDrugInfoTableActive = true;
        this.isActiveSpinner = false;
      },
      error => {
        this.isActiveSpinner = false;
        Utility.showViolationsIfOccurs(error);
      });
  }

  onSelectHint(indexHint: number): void {
    const url = (this.resultDrugHints as DrugHints[])[indexHint].redirectUrl;
    this.resultDrugHints = null;
    this.loadTableData(url);
  }


  onLinkEmit(link: string): void {
    this.isDrugInfoTableActive = false;
    this.loadTableData(link);
  }

  private bindKey(): void {
    this.drugEquivalentsContainer.nativeElement.addEventListener(
      'keydown', (evt) => this.listener(evt), false);
  }

  private listener(evt: KeyboardEvent): void {
    switch (evt.key) {
      case 'Enter':
        return this.onClickSearch();
    }
  }
}
