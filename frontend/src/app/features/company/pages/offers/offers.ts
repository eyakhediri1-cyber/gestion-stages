import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import {
  InternshipOfferService,
  InternshipOfferRequest,
  InternshipOfferResponse
} from '../../../../core/offer/internship-offer.service';

@Component({
  selector: 'app-company-offers',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './offers.html',
  styleUrl: './offers.scss'
})
export class Offers implements OnInit {

  private offerService = inject(InternshipOfferService);

  offers: InternshipOfferResponse[] = [];

  loading = false;
  saving = false;
  showForm = false;
  editingId: number | null = null;

  errorMessage = '';
  successMessage = '';

  form: InternshipOfferRequest = this.emptyForm();

  ngOnInit(): void {
    this.loadOffers();
  }

  emptyForm(): InternshipOfferRequest {
    return {
      title: '',
      description: '',
      domain: '',
      location: '',
      internshipType: '',
      duration: '',
      startDate: '',
      applicationDeadline: '',
      requiredSkills: ''
    };
  }

  loadOffers(): void {
    this.loading = true;
    this.errorMessage = '';

    this.offerService.getMyOffers().subscribe({
      next: (data) => {
        this.offers = data;
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Impossible de charger les offres.';
        this.loading = false;
      }
    });
  }

  openCreateForm(): void {
    this.form = this.emptyForm();
    this.editingId = null;
    this.showForm = true;
    this.errorMessage = '';
    this.successMessage = '';
  }

  editOffer(offer: InternshipOfferResponse): void {
    this.editingId = offer.id;

    this.form = {
      title: offer.title,
      description: offer.description,
      domain: offer.domain,
      location: offer.location,
      internshipType: offer.internshipType,
      duration: offer.duration,
      startDate: offer.startDate,
      applicationDeadline: offer.applicationDeadline,
      requiredSkills: offer.requiredSkills
    };

    this.showForm = true;
    this.errorMessage = '';
    this.successMessage = '';
  }

  closeForm(): void {
    this.showForm = false;
    this.editingId = null;
    this.form = this.emptyForm();
  }

  saveOffer(): void {
    this.saving = true;
    this.errorMessage = '';
    this.successMessage = '';

    const request = { ...this.form };

    const operation = this.editingId !== null
      ? this.offerService.updateOffer(this.editingId, request)
      : this.offerService.createOffer(request);

    operation.subscribe({
      next: () => {
        this.successMessage = this.editingId !== null
          ? 'Offre modifiée avec succès !'
          : 'Offre créée avec succès !';

        this.saving = false;
        this.closeForm();
        this.loadOffers();
      },
      error: () => {
        this.errorMessage =
          'Une erreur est survenue lors de l’enregistrement.';
        this.saving = false;
      }
    });
  }

  togglePublication(offer: InternshipOfferResponse): void {
    this.errorMessage = '';
    this.successMessage = '';

    this.offerService.publishOffer(
      offer.id,
      !offer.published
    ).subscribe({
      next: () => {
        this.successMessage = offer.published
          ? 'Offre dépubliée avec succès.'
          : 'Offre publiée avec succès.';

        this.loadOffers();
      },
      error: () => {
        this.errorMessage =
          'Impossible de modifier le statut de publication.';
      }
    });
  }

  deleteOffer(offer: InternshipOfferResponse): void {
    const confirmed = window.confirm(
      `Voulez-vous vraiment supprimer l'offre "${offer.title}" ?`
    );

    if (!confirmed) return;

    this.errorMessage = '';
    this.successMessage = '';

    this.offerService.deleteOffer(offer.id).subscribe({
      next: () => {
        this.successMessage = 'Offre supprimée avec succès.';
        this.loadOffers();
      },
      error: () => {
        this.errorMessage = 'Impossible de supprimer cette offre.';
      }
    });
  }
}
