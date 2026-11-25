
import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../../core/auth/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class Login {

  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  email = '';
  password = '';
  role = 'STUDENT';
  showPassword = false;

  errorMessage = '';
  successMessage = '';
  isLoading = false;

  onSubmit(): void {

    this.errorMessage = '';
    this.successMessage = '';

    if (!this.email || !this.password) {
      this.errorMessage = 'Veuillez remplir tous les champs.';
      return;
    }

    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.email)) {
      this.errorMessage = 'Veuillez saisir une adresse email valide.';
      return;
    }

    this.isLoading = true;

    this.authService.login({
      email: this.email.trim().toLowerCase(),
      password: this.password
    }).subscribe({

      next: (response) => {
        this.isLoading = false;

        this.successMessage =
          `Bienvenue ${response.fullName} ! Connexion réussie.`;

        console.log('Utilisateur connecté :', {
          userId: response.userId,
          role: response.role
        });

      },

      error: (error) => {
        this.isLoading = false;

        if (error.status === 401 || error.status === 403) {
          this.errorMessage = 'Email ou mot de passe incorrect.';
        } else if (error.status === 0) {
          this.errorMessage =
            'Impossible de contacter le serveur. Vérifiez que le backend est démarré.';
        } else {
          this.errorMessage =
            error.error?.message || 'Une erreur est survenue.';
        }
      }

    });
  }
}
