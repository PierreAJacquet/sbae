import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { Incident } from '../models/incident.model';
import {SearchFilter} from '../models/search-filter.model';
import {Page} from '../models/page.model';

@Injectable({ providedIn: 'root' })
export class IncidentService {

  private readonly apiUrl = '/api/incidents/search-incidents';

  constructor(private readonly http: HttpClient) {}

  /**
   * Recherche tous les incidents correspondants aux filtres
   * et calcule le temps d'exécution.
   */
  searchWithTiming(filters: SearchFilter, page: number = 0, size: number = 10): Observable<{ page: Page<Incident>, duration: number }> {
    const startTime = performance.now();

    // Ajout des paramètres de pagination dans l'URL
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    // On envoie 'filters' directement comme corps de la requête POST
    return this.http.post<Page<Incident>>(this.apiUrl, filters, { params }).pipe(
      map(responsePage => {
        const endTime = performance.now();
        return {
          page: responsePage,
          duration: (endTime - startTime) / 1000
        };
      })
    );
  }

}
