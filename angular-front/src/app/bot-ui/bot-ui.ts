import { Component, inject } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AsyncPipe} from '@angular/common';
import {MarkdownComponent} from 'ngx-markdown';
import {FormsModule} from '@angular/forms';
import {LoadingService} from '../services/loading';

@Component({
  imports: [
    AsyncPipe,
    MarkdownComponent,
    FormsModule
  ],
  selector: 'app-bot-ui',
  styleUrl: './bot-ui.css',
  templateUrl: './bot-ui.html',
})
export class BotUi {
  query : any;
  http = inject(HttpClient);
  public loadService = inject(LoadingService);
  response$ : Observable<any> | undefined;
  askAgent() {
    this.response$= this.http
      .get("http://localhost:9999/EBANK-BOT/chat?query="+this.query, {responseType:'text'})
  }

  protected readonly LoadingService = LoadingService;
}
