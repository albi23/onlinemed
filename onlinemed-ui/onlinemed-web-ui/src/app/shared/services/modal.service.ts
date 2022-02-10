import {EventEmitter, Injectable} from '@angular/core';
import {ModalComponent} from '../components/modal/modal.component';

@Injectable({
  providedIn: 'root'
})
export class ModalService {

  private modalRef: ModalComponent;
  modalIsOpen = false;

  constructor() {
  }

  setModalRef(modalRef: ModalComponent): void {
    this.modalRef = modalRef;
  }

  setFullModeView(): void {
    this.modalRef.isFullScreenModeWindow = true;
  }

  disableFullModeView(): void {
    this.modalRef.isFullScreenModeWindow = false;
  }

  openModal(): void {
    this.modalRef.isActive = true;
  }

  closeModal(): void {
    this.modalRef.isActive = false;
    this.disableFullModeView();
  }

  onConfirm(func: (...args: any) => void): void {
    this.modalRef.onConfirm.subscribe(_ => {
      func();
      this.makeStatelessEvents();
    });
  }

  onCancel(func: (...args: any) => void): void {
    this.modalRef.onCancel.subscribe(_ => {
      func();
      this.makeStatelessEvents();
    });
  }

  public injectDataToModal(body: string, title: string = 'common.information', cancelButtonText: string = 'common.cancel',
                           confirmButtonText: string = 'common.confirm'): void {
    this.modalRef.title = title;
    this.modalRef.body = body;
    this.modalRef.cancelButtonText = cancelButtonText;
    this.modalRef.confirmButtonText = confirmButtonText;
    this.makeStatelessEvents();
  }

  public injectDataToModal2(body: string, title: string = 'common.information',
                            confirmButtonText: string = 'common.confirm',
                            isCancelButton = false,
                            htmlBody = true): void {
    this.modalRef.title = title;
    this.modalRef.body = body;
    this.modalRef.isBodyAsHtml = htmlBody;
    this.modalRef.isCancelButton = isCancelButton;
    this.modalRef.confirmButtonText = confirmButtonText;
    this.makeStatelessEvents();
  }

  private makeStatelessEvents(): void {
    this.modalRef.onCancel = new EventEmitter<boolean>();
    this.modalRef.onConfirm = new EventEmitter<boolean>();
  }
}
