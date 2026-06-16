# Changelog

## [1.0.2] - 2026-06-15
### Aggiunto
- Creazione, modifica ed eliminazione appuntamenti direttamente dalla form integrata nel pannello dettagli.
- Salvataggio permanente degli appuntamenti su disco e rimozione degli eventi di esempio.
- Supporto drag & drop per spostare gli eventi; con `Ctrl` + trascinamento l'evento viene duplicato.
- Importazione ed esportazione eventi in formato `.ics`.
- Tema chiaro/scuro, toggle "Sempre in primo piano" e cambio lingua con bandiere italiana/inglese.
- Sezione Help nella sidebar con comandi rapidi e link a "CalenDaros v1.0.2".

### Modificato
- Sidebar riorganizzata con navigazione mese, controlli rapidi, filtri e Help.
- Modalità estesa senza minicalendario laterale; modalità compatta ridimensionata e con giorni contenenti eventi evidenziati.
- Griglia mensile sempre completa 7x6, con giorni del mese precedente e successivo.
- Form appuntamento semplificata: tipo dopo descrizione, tipo predefinito "Altro", ora selezionabile ogni 15 minuti e pulsanti con icone.
- Tooltip descrittivi su calendario esteso e compatto.

### Corretto
- Colori e sfondi coerenti tra tema chiaro/scuro, modalità compatta/estesa e filtri.
- Instradati i messaggi di debug tramite `Debug` al posto di `System.out.println`.
- Migliorata la leggibilità della sezione Help e delle icone di annullamento/eliminazione.

## [1.0.1] - 2025-04-07
### Modificato
- Aggiunto file 'compila_e_avvia.bat' per facilitare l'esecuzione su Windows
- Modifica struttura delle cartelle: uso della cartella i18n per internazionalizzazione.
- Migliorato README con istruzioni per utenti Windows

## [1.0.0] - 2025-04-05
### Aggiunto
- Prima versione dell'app "Calendario Appuntamenti"
- Funzionalità base: visualizzazione calendario in modalità estesa e compatta, visualizzazione appuntamenti.
