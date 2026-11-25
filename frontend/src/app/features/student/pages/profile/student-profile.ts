import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import {
  StudentService,
  StudentProfile
} from '../../../../core/student/student.service';

@Component({
  selector: 'app-student-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './student-profile.html',
  styleUrl: './student-profile.scss'
})
export class StudentProfileComponent implements OnInit {

  profile: StudentProfile = {
    id: 0,
    fullName: '',
    email: '',
    university: '',
    specialization: '',
    studyLevel: '',
    phone: '',
    cvUrl: '',
    linkedinUrl: '',
    githubUrl: ''
  };

  loading = true;
  saving = false;
  successMessage = '';
  errorMessage = '';

  constructor(private studentService: StudentService) {}

  ngOnInit(): void {
    this.loadProfile();
  }

  loadProfile(): void {
    this.loading = true;

    this.studentService.getMyProfile().subscribe({
      next: (data: StudentProfile) => {
        this.profile = data;
        this.loading = false;
      },
      error: (error: any) => {
        this.errorMessage =
          error.error?.message || 'Impossible de charger le profil.';
        this.loading = false;
      }
    });
  }

  saveProfile(): void {
    this.saving = true;
    this.successMessage = '';
    this.errorMessage = '';

    this.studentService.updateMyProfile(this.profile).subscribe({
      next: (data: StudentProfile) => {
        this.profile = data;
        this.successMessage = 'Profil mis à jour avec succès !';
        this.saving = false;
      },
      error: (error: any) => {
        this.errorMessage =
          error.error?.message || 'Erreur lors de la sauvegarde.';
        this.saving = false;
      }
    });
  }
}
