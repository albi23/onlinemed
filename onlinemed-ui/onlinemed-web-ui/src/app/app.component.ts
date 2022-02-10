import {Component, OnInit} from '@angular/core';
import {TranslateService} from './core/translations/translate.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'Online Med';

  constructor(private translationService: TranslateService) {
    this.translationService.loadLanguages();
  }

  ngOnInit(): void {
  }


}
