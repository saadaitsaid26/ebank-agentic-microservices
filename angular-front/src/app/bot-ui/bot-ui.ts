import { Component, inject } from '@angular/core';
import {HttpClient, HttpDownloadProgressEvent, HttpEventType} from '@angular/common/http';
import {map, Observable} from 'rxjs';
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

  askAgentStream() {
    this.response$= this.http
      .get("http://localhost:9999/EBANK-BOT/chatStream?query="+this.query, {responseType:'text', observe: "events", reportProgress : true})
      .pipe(map(event => {
        switch (event.type){
          case HttpEventType.Sent:
            return {'type': 'Sent'}
          case HttpEventType.DownloadProgress:
            return {type: "Response", content : (event as HttpDownloadProgressEvent).partialText }
          case HttpEventType.Response:
            return {type : "Response", content : event.body}
          default :
            return {type : 'Other', data : event}
        }
      }))
  }

  protected readonly LoadingService = LoadingService;
}
