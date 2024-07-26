import { Component, inject } from '@angular/core';
import { AccountService } from './services/account.service';
import { AccountResponse } from './interfaces/account-response.interface';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrl: './account.component.css'
})
export class AccountComponent {

  private accountServivce = inject(AccountService);

  displayedColumns: string[] = ['id', 'accountNumber', 'balance', 'actions'];
  dataSource: MatTableDataSource<AccountResponse> = new MatTableDataSource();

  ngOnInit() {
    this.loadAccounts();
  }
  
  loadAccounts() {
    console.log("Cargando cuentas..."+this.dataSource.data);
    this.accountServivce.getAccountsByUser()
      .subscribe(accounts => {
        this.dataSource.data = accounts;
        console.log("Cuentas cargadas: ", accounts);
      });
  }


  
}
