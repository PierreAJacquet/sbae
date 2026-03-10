-- Indispensable pour les recherches "LIKE %...%" performantes
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- Indexation des colonnes de recherche
CREATE INDEX idx_incident_title_trgm ON incident USING gin (title gin_trgm_ops);
CREATE INDEX idx_incident_description_trgm ON incident USING gin (description gin_trgm_ops);
CREATE INDEX idx_incident_severity ON incident (severity);

-- Index sur les colonnes de la table Personne (clés étrangères et noms)
CREATE INDEX idx_person_last_names ON person (last_name);
CREATE INDEX idx_person_first_names ON person (first_name);
CREATE INDEX idx_person_email ON person (email);