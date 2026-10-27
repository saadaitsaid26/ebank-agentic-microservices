export interface Account{
  id: string,
  balance: number,
  createdAt:string,
  customerId?:number
  type: string
}
export enum RequestStatus {
  SUCCESS, ERROR
}
export interface AccountListState{
  accounts:Account[],
  status : RequestStatus,
  errorMessage? : String
}
