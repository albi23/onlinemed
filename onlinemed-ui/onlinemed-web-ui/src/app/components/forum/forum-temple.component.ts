import {Component} from '@angular/core';

@Component({
  selector: 'app-forum-temple',
  template: `
    <div class="forum-container__header">
      <div>
        <h2>Online Med Forum</h2>
        <h6>{{'forum.forum-desc' | translate}}</h6>
      </div>
    </div>
  `,
  styles: [
  ]
})
export class ForumTempleComponent  {
  constructor() { }
}
