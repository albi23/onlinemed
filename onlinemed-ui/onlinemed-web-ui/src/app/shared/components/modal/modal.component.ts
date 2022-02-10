import {Component, EventEmitter, OnInit} from '@angular/core';
import {ModalService} from '../../services/modal.service';
import {SideBarService} from '../../services/side-bar.service';
import {BehaviorSubject} from 'rxjs';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styles: []
})
export class ModalComponent implements OnInit{
  onConfirm: EventEmitter<boolean>;
  onCancel: EventEmitter<boolean>;
  title = 'common.information';
  body = '';
  cancelButtonText = 'common.cancel';
  confirmButtonText = 'common.confirm';
  isActive = false;
  isCancelButton = true;
  isBodyAsHtml = false;
  menuOpen = true;
  isFullScreenModeWindow = false;
  private menuStatus = new BehaviorSubject<boolean>(true);


  constructor(private modalService: ModalService,
              private sideBarService: SideBarService) {
    this.modalService.setModalRef(this);
    this.sideBarService.registerObserver(this.menuStatus);
  }

  ngOnInit(): void {
    this.setStatus();
  }

  onConfirmAction(): void {
    this.onConfirm.emit(false);
  }

  onCancelAction(): void {
    this.onCancel.emit(true);
  }

  setStatus(): void {
    this.menuStatus.subscribe(res => this.menuOpen = res);
  }
}
