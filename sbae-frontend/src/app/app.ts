import {ChangeDetectorRef, Component} from '@angular/core';
import { CommonModule } from '@angular/common';
import {ReactiveFormsModule, FormGroup, FormControl, Validators} from '@angular/forms';
import { IncidentService } from './services/incident.service';
import { Incident } from './models/incident.model';
import { SearchFilter } from './models/search-filter.model';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class AppComponent {

  // Initialisation du formulaire avec les 6 champs du SearchFilter
  // TODO Validateur pour les controles sur le formulaire
  searchForm = new FormGroup({
    title: new FormControl(''),
    description: new FormControl(''),
    severity: new FormControl(''),
    lastName: new FormControl(''),
    firstName: new FormControl(''),
    email: new FormControl('', [Validators.email])
  });

  results: Incident[] = [];
  searchDuration: number | null = null;
  loading = false;

  constructor(private incidentService: IncidentService,
              private cdr: ChangeDetectorRef) {}

  onSearch() {
    if (this.searchForm.invalid) {
      return;
    }

    this.loading = true;
    const filters = this.searchForm.value as SearchFilter;

    // Appel au service pour les résultats et la mesure de performance
    this.incidentService.searchWithTiming(filters).subscribe({
      next: (response) => {
        console.log('1. Réponse brute API:', response);
        this.results = [...response.data];
        console.log('2. Contenu de this.results:', this.results);

        // Temps de calcul côté front
        this.searchDuration = response.duration;
        this.loading = false;
        // Force angular à détecter les changements suite à la réponse de l'api
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Erreur API:', err);
        this.loading = false;
        // Forcer pour enlever le loader en cas d'erreur
        this.cdr.detectChanges();
      }
    });
  }
}
