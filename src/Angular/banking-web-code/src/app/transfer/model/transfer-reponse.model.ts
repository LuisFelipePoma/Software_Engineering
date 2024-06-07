import { AccountResponse } from "../../account/model/account-response.model";

export interface TransferResponse{
    id:number;
    targetAccount: AccountResponse;
    amount: number;
    description:string;
}