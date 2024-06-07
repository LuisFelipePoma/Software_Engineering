import { Component } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { TransferResponse } from './model/transfer-reponse.model';



const ELEMENT_DATA: TransferResponse[] = [
  {
    id: 1,
    targetAccount: {
      id: 123,
      accountNumber: '1234567890',
      balance: 1000,
      ownerName: 'John Doe',
      ownerEmail: 'johndoe@example.com'
    },
    amount: 500,
    description: 'Transfer'
  },
  {
    id: 2,
    targetAccount: {
      id: 123,
      accountNumber: '1234567890',
      balance: 1000,
      ownerName: 'Henry Antonio',
      ownerEmail: 'hmendo81@example.com'
    },
    amount: 500,
    description: 'Transfer'
  }
];

@Component({
  selector: 'app-transfer',
  templateUrl: './transfer.component.html',
  styleUrl: './transfer.component.css'
})
export class TransferComponent {
  displayedColumns: string[] = ['id', 'accountNumber', 'amount', 'description'];
  
  dataSource: MatTableDataSource<TransferResponse> =
	new MatTableDataSource<TransferResponse>();

  
  ngOnInit() {
    this.populateDataSource();    
  }


  populateDataSource() {
    this.dataSource.data = ELEMENT_DATA;
    console.log(this.dataSource.data);
  }
}
