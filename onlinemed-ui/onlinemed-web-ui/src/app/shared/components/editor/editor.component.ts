import {AfterViewInit, Component, Input, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {HtmlEditorService, ImageService, LinkService, ToolbarService} from '@syncfusion/ej2-angular-richtexteditor';


@Component({
  selector: 'app-editor',
  templateUrl: './editor.component.html',
  providers: [ToolbarService, LinkService, ImageService, HtmlEditorService],
  styles: []
})
export class EditorComponent implements OnInit, AfterViewInit {
  @ViewChild('valueTemplate') valueRef: TemplateRef<any>;

  @Input() set editorValue(value: string) {
    if (value) {
      this.editorVal = value;
    }
  }

  public editorVal = '';
  public tools: object = {
    items: ['Bold', 'Italic', 'Underline', 'StrikeThrough', '|',
      'FontSize', '|',
      'LowerCase', 'UpperCase', '|',
      'Formats', '|', 'OrderedList', 'UnorderedList', '|',
      'CreateLink',
      '|', 'Print', 'SourceCode']
  };

  constructor() {
  }

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
  }


}
