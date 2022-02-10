import {AfterViewInit, Component, ElementRef, EventEmitter, Input, Output, ViewChild} from '@angular/core';

export interface Message {
  subject: string;
  message: string;
}

@Component({
  selector: 'app-message-sender',
  templateUrl: './message-sender.component.html',
  styles: []
})
export class MessageSenderComponent implements AfterViewInit {

  @Input() set nonEditableSubject(subject: string) {
    if (!this.subjectTemplate) {
      this.subjectTemplate = subject;
    }
  }

  @Input() set setMessageTemplate(messageTemplate: string) {
    if (!this.messageTemplate) {
      this.messageTemplate = messageTemplate;
    }
  }

  @Output() closeEvt: EventEmitter<void> = new EventEmitter<void>();
  @Output() onSendEvt: EventEmitter<Message> = new EventEmitter<Message>();

  @ViewChild('message') message: ElementRef;
  @ViewChild('subject') subject: ElementRef;

  messageTemplate: string;
  subjectTemplate: string;

  constructor() {
  }

  ngAfterViewInit(): void {
    if (this.messageTemplate) {
      this.message.nativeElement.value = this.messageTemplate;
    }
    if (this.subjectTemplate) {
      this.subject.nativeElement.value = this.subjectTemplate;
    }
  }


  onClickCancel(): void {
    this.closeEvt.emit();
  }

  onClickSubmit(): void {
    const subject = this.subject.nativeElement.value;
    const message = this.message.nativeElement.value;
    this.onSendEvt.emit({subject, message} as Message);
  }
}
