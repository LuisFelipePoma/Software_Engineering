import { Component, inject } from '@angular/core';

import { Router } from '@angular/router';
import { AuthService } from '../../user/services/auth.service';


@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  private authService = inject(AuthService);
  private router = inject(Router);

  get user() {
    return this.authService.user;
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/']);
  }
}
