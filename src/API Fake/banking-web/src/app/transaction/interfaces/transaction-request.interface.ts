export interface TransactionRequest{
    id:number;
    sourceAccount: String
    targetAccount: String
    amount: number;
    description:string
}