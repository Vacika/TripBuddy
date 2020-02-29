import {Component, EventEmitter, OnInit, Output} from "@angular/core";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'nav-menu',
  templateUrl: './nav-menu.component.html',
  styleUrls: ['./nav-menu.component.scss']
})
export class NavMenuComponent  {
  @Output() switchLangEmitter = new EventEmitter<string>();

  switchLang(lang: string) {
    this.switchLangEmitter.emit(lang);
  }
}
