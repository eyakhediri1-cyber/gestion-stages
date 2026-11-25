import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  CompanyService,
  CompanyProfileRequest
} from '../../../../core/company/company.service';

@Component({
  selector: 'app-company-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.html',
  styleUrl: './profile.scss'
})
export class Profile implements OnInit {

  private companyService = inject(CompanyService);

  loading = false;
  saving = false;
  errorMessage = '';
  successMessage = '';
  verified = false;

  profile: CompanyProfileRequest = {
    companyName: '',
    industry: '',
    description: '',
    website: '',
    address: '',
    phone: '',
    logoUrl: ''
  };

  ngOnInit(): void {
    this.loadProfile();
  }

  loadProfile(): void {
    this.loading = true;
    this.errorMessage = '';

    this.companyService.getMyProfile().subscribe({
      next: (response) => {
        this.profile = {
          companyName: response.companyName ?? '',
          industry: response.industry ?? '',
          description: response.description ?? '',
          website: response.website ?? '',
          address: response.address ?? '',
          phone: response.phone ?? '',
          logoUrl: response.logoUrl ?? ''
        };

        this.verified = response.verified;
        this.loading = false;
      },
      error: () => {
        this.errorMessage =
          'Impossible de charger le profil entreprise.';
        this.loading = false;
      }
    });
  }

  updateProfile(): void {
    this.saving = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.companyService.updateMyProfile(this.profile).subscribe({
      next: (response) => {
        this.profile = {
          companyName: response.companyName ?? '',
          industry: response.industry ?? '',
          description: response.description ?? '',
          website: response.website ?? '',
          address: response.address ?? '',
          phone: response.phone ?? '',
          logoUrl: response.logoUrl ?? ''
        };

        this.verified = response.verified;
        this.successMessage = 'Profil mis à jour avec succès !';
        this.saving = false;
      },
      error: () => {
        this.errorMessage =
          'Erreur lors de la mise à jour du profil.';
        this.saving = false;
      }
    });
  }
}
