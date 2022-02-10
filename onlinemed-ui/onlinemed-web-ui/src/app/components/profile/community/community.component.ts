import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-community',
  templateUrl: './community.component.html',
  styles: [
  ]
})
export class CommunityComponent implements OnInit {

  @Input() commentsNr = 0;
  @Input() memberDate = 0;
  @Input() lastLoginDate = 0;

  constructor() { }

  ngOnInit(): void {
  }

  formatTimestamp(time: number): string {
    return new Intl.DateTimeFormat('en-GB').format(time);
  }

}
