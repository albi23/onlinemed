import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ForumTopic} from '../../../core/sdk/onlinemed-model';
import {Router} from '@angular/router';
import {Utility} from '../../../shared/utilities/utility';

@Component({
  selector: 'app-single-topic-view',
  templateUrl: './single-topic-view.component.html',
  styles: [
  ]
})
export class SingleTopicViewComponent implements OnInit {

  @Input() topic: ForumTopic;
  @Input() isUpdated =  false;
  @Input() isLiked =  false;
  @Output() topicEvent: EventEmitter<ForumTopic> = new EventEmitter<ForumTopic>();

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  getTimeAgoString(timestamp: number): string {
    return Utility.getTimeAgoString(timestamp);
  }

  extractsHasztags(topic: ForumTopic): string[]{
    return topic.hasztags.split(';');
  }

  onClickTopic(id: string): void {
    this.router.navigate(['/om/forum/post/' + Utility.getObjectId(id)]);
  }

  onClickHearth(topic: ForumTopic): void {
    this.topicEvent.emit(topic);
  }

}
