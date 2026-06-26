# Changelog

## [1.0.6] - 2026-06-26

### Aggiunto
- Pulsante rapido per aprire Google Calendar dal pannello controlli, con icona moderna dedicata.
- Doppio click su un appuntamento nel calendario esteso per aprire direttamente la modalità di modifica.

### Modificato
- Quando si apre la modalità estesa sul mese corrente, la scrollbar porta automaticamente in vista il giorno corrente.

## [1.0.5] - 2026-06-22

### Aggiunto
- All'inserimento di un nuovo appuntamento, apertura automatica del menu Orari con selezione dell'orario opportuno in funzione dell'ora corrente.
- Scorciatoie da tastiera nella form appuntamento: `Tab` dalla descrizione apre il menu Tipo, `Tab` dal Tipo passa al pulsante Salva e `Invio` sul pulsante Salva registra l'appuntamento.

### Modificato
- La conferma di eliminazione appuntamento ora mostra giorno e ora dell'appuntamento che verrà eliminato.

## [1.0.4] - 2026-06-20

### Modificato
- Scurito lo sfondo dei pulsanti in modalità scura per renderlo coerente con il resto dell'interfaccia.
- Aggiornato `compila_e_avvia.bat` per generare un `CalenDaros.jar` autosufficiente, includendo FlatLaf, FlatLaf Extras e JSVG nel JAR.

### Corretto
- Risolto il tema scuro che tornava parzialmente chiaro quando `CalenDaros.jar` veniva avviato o spostato senza la cartella `lib`.

## [1.0.3] - 2026-06-19

### Aggiunto
- Supporto a FlatLaf Extras per usare icone SVG Material Symbols, con fallback alle icone disegnate in Java.
- Download automatico di `flatlaf.jar`, `flatlaf-extras.jar` e `jsvg.jar` nello script `compila_e_avvia.bat`.
- Salvataggio permanente delle dimensioni della modalità estesa in `~/.calenDaros/ui.properties`.

### Modificato
- Sostituite le icone dei controlli principali con Material Symbols, mantenendo le bandiere per il cambio lingua nello stile degli altri pulsanti.
- Uniformate dimensione, allineamento e spaziatura dei 9 pulsanti superiori della sidebar.
- Ridotta l'altezza dell'intestazione dei giorni nel calendario esteso, separandola dalla griglia dei giorni.
- Migliorati colori e contrasto delle icone nei temi chiaro e scuro.

### Corretto
- Ripristino dell'ultima dimensione della modalità estesa quando si torna dalla modalità compatta o si riapre l'applicazione.
- Cursore a mano sui pulsanti di modifica appuntamento e sui riquadri dei giorni cliccabili nel calendario esteso.

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
