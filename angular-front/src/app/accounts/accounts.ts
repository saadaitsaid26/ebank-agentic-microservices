import { Component, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {AsyncPipe} from '@angular/common';
import {Account, AccountListState, RequestStatus} from '../../model/account.model';
import {catchError, map, Observable, of} from 'rxjs';

@Component({
  imports: [
    AsyncPipe
  ],
  selector: 'app-accounts',
  styleUrl: './accounts.css',
  templateUrl: './accounts.html',
})
export class Accounts {
  private http  = inject(HttpClient);
  accounts$: Observable<AccountListState> = this.http.get<Account[]>
    ("http://localhost:9999/EBANK-SERVICE/accounts")
    .pipe(
      map(resp=> {
        return {accounts: resp, status:RequestStatus.SUCCESS}
      }),
      catchError((err, caught) => {
        return of({accounts: [], status : RequestStatus.ERROR, errorMessage : err.statusText })
      })
    );

  protected readonly RequestStatus = RequestStatus;
}
