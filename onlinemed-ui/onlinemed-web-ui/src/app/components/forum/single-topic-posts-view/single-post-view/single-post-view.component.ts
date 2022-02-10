import {AfterViewInit, ChangeDetectorRef, Component, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import {ForumPost} from '../../../../core/sdk/onlinemed-model';
import {Utility} from '../../../../shared/utilities/utility';
import {AuthService} from '../../../../core/auth/auth.service';
import {EditorComponent} from '../../../../shared/components/editor/editor.component';

export interface PostValue {
  id: string;
  text: string;
}

@Component({
  selector: 'app-single-post-view',
  templateUrl: './single-post-view.component.html',
  styles: []
})
export class SinglePostViewComponent implements AfterViewInit {

  @Input() post: ForumPost;
  @Input() editPrivileges: boolean;
  @Output() editedPost: EventEmitter<PostValue> = new EventEmitter<PostValue>();
  @Output() onReplayEvt: EventEmitter<void> = new EventEmitter<void>();
  @Output() onRemoveEvt: EventEmitter<string> = new EventEmitter<string>();
  @ViewChild('postEditor') postEditor: EditorComponent;

  loggedId: string;
  isEditMode = false;
  edited = false;


  constructor(private authService: AuthService,
              private cd: ChangeDetectorRef) {
    this.loggedId = this.authService.getCurrentLoggedPerson().id;
  }


  ngAfterViewInit(): void {
    this.cd.detectChanges();
  }

  formatTimestamp(time: number): string {
    return Utility.formatTimestamp(time);
  }

  getTimeAgoString(timestamp: number): string {
    return Utility.getTimeAgoString(timestamp).concat(' ago');
  }

  onEditClick(): void {
    this.isEditMode = true;
  }

  onCancel(): void {
    this.isEditMode = false;
  }

  onSave(): void {
    this.editedPost.emit({id: this.post.id, text: this.postEditor.editorVal});
    this.onCancel();
  }

  onReplay(): void {
    this.onReplayEvt.emit();
  }

  onRemove(): void {
    this.onRemoveEvt.emit(this.post.id);
  }
}
