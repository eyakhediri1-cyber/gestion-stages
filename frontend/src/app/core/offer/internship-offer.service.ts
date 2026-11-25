import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface InternshipOfferRequest {
  title: string;
  description: string;
  domain: string;
  location: string;
  internshipType: string;
  duration: string;
  startDate: string;
  applicationDeadline: string;
  requiredSkills: string;
}

export interface InternshipOfferResponse
  extends InternshipOfferRequest {
  id: number;
  companyId: number;
  companyName: string;
  published: boolean;
  createdAt: string;
  updatedAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class InternshipOfferService {

  private http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/api/offers';

  getMyOffers(): Observable<InternshipOfferResponse[]> {
    return this.http.get<InternshipOfferResponse[]>(
      `${this.apiUrl}/my`
    );
  }

  getOffer(id: number): Observable<InternshipOfferResponse> {
    return this.http.get<InternshipOfferResponse>(
      `${this.apiUrl}/${id}`
    );
  }

  createOffer(
    request: InternshipOfferRequest
  ): Observable<InternshipOfferResponse> {
    return this.http.post<InternshipOfferResponse>(
      this.apiUrl,
      request
    );
  }

  updateOffer(
    id: number,
    request: InternshipOfferRequest
  ): Observable<InternshipOfferResponse> {
    return this.http.put<InternshipOfferResponse>(
      `${this.apiUrl}/${id}`,
      request
    );
  }

  publishOffer(
    id: number,
    published: boolean
  ): Observable<InternshipOfferResponse> {
    return this.http.patch<InternshipOfferResponse>(
      `${this.apiUrl}/${id}/publish`,
      null,
      { params: { published: String(published) } }
    );
  }

  deleteOffer(id: number): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/${id}`
    );
  }
}
