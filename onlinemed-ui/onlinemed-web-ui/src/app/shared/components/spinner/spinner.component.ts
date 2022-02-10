import {Component, Input, OnInit} from '@angular/core';
import {SideBarService} from '../../services/side-bar.service';
import {BehaviorSubject} from 'rxjs';

@Component({
  selector: 'app-spinner',
  templateUrl: './spinner.component.html',
  styles: []
})
export class SpinnerComponent implements OnInit{

  @Input() isActive = false;
  menuOpen = false;
  private menuStatus = new BehaviorSubject<boolean>(true);


  constructor(private si: SideBarService) {
    this.si.registerObserver(this.menuStatus);
  }

  ngOnInit(): void {
    this.setStatus();
  }

  setStatus(): void{
    this.menuStatus.subscribe(res => this.menuOpen = res);
  }
}
