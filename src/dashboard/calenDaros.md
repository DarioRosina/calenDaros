# CalenDaros: un calendario desktop nato per restare vicino al lavoro di tutti i giorni

Ci sono strumenti che vogliono fare tutto: sincronizzarsi con dieci servizi, prevedere le nostre abitudini, riempire lo schermo di funzioni. E poi ci sono strumenti che partono da una domanda più semplice: come posso vedere i miei appuntamenti, capirli in fretta e gestirli senza distrazioni?

CalenDaros nasce da questa seconda idea. È un'applicazione desktop Java per la gestione degli appuntamenti, pensata per essere immediata, leggibile e concreta. Non prova a sostituire un intero ecosistema di produttività: vuole essere un calendario personale chiaro, installabile, utilizzabile anche senza IDE e abbastanza flessibile da accompagnare sia una consultazione rapida sia una pianificazione più dettagliata.

Il cuore del progetto è una vista mensile completa, dove ogni giorno trova il suo spazio e gli appuntamenti diventano parte visibile del mese. Accanto a questa c'è una modalità compatta, pensata per quando serve tenere il calendario a portata di mano senza occupare troppo spazio sul desktop. L'interfaccia punta su una navigazione semplice, filtri per categoria, evidenziazione dei giorni importanti e supporto multilingua tramite risorse dedicate.

## La versione 1.0.1: rendere il progetto più facile da usare

La versione 1.0.1, rilasciata il 7 aprile 2025, ha avuto un obiettivo molto pratico: abbassare la soglia d'ingresso.

Il progetto era già una piccola applicazione desktop funzionante, con visualizzazione del calendario, appuntamenti, vista estesa e modalità compatta. Con la 1.0.1 è arrivato un tassello importante per chi usa Windows: il file `compila_e_avvia.bat`. Una scelta semplice, ma significativa. Non tutti vogliono aprire Eclipse, configurare classpath o compilare manualmente da terminale. Avere uno script che compila i sorgenti, copia le risorse e avvia l'applicazione rende CalenDaros più accessibile, più vicino a un programma da provare subito.

Nella stessa direzione va anche il miglioramento del README, aggiornato con istruzioni più chiare per gli utenti Windows. La documentazione non è un accessorio: è il primo punto di contatto tra un progetto e chi prova a usarlo. Una guida più esplicita rende il software meno intimidatorio e più accogliente.

La 1.0.1 ha introdotto anche una riorganizzazione della struttura delle cartelle, con l'uso della cartella `i18n` per l'internazionalizzazione. È un dettaglio tecnico, ma racconta una direzione precisa: CalenDaros non vuole essere rigido. Vuole poter parlare più lingue, separando l'interfaccia dalla logica dell'applicazione e preparando il terreno per una crescita più ordinata.

## La versione 1.0.2: dal calendario che mostra al calendario che accompagna

Con la versione 1.0.2, rilasciata il 15 giugno 2026, CalenDaros cambia passo. Non si limita più a mostrare appuntamenti: comincia a comportarsi come uno strumento quotidiano, più fluido, più persistente, più vicino ai gesti naturali dell'utente.

La prima grande evoluzione è il salvataggio permanente degli appuntamenti su disco. È il passaggio che trasforma un prototipo utile in un'applicazione davvero utilizzabile. Gli appuntamenti non sono più dati temporanei o esempi da osservare: diventano informazioni dell'utente, conservate tra un avvio e l'altro. Per questo sono stati rimossi gli appuntamenti di esempio, lasciando spazio a un calendario personale, pulito, pronto a essere riempito con eventi reali.

Anche l'inserimento degli appuntamenti è diventato più naturale. Ora, nella vista estesa, basta cliccare su un'area vuota del giorno per aprire l'interfaccia di creazione. Il vecchio pulsante "Nuovo Appuntamento" non serve più, perché il gesto è già dentro al calendario. Si clicca dove si vuole aggiungere qualcosa, e il calendario risponde lì, con il contesto giusto.

La form è stata ripensata per essere più coerente: il campo Tipo è stato spostato dopo la descrizione, è stato aggiunto il tipo "Altro" come valore predefinito, i pulsanti Salva e Annulla sono stati organizzati in una colonna laterale e la visualizzazione dei dettagli usa la stessa logica della form. Anche il riquadro dei dettagli e quello di inserimento sono stati uniformati in altezza, riducendo salti visivi e rendendo l'interfaccia più stabile.

## Più controllo sugli eventi

La 1.0.2 introduce un modo più diretto di gestire gli appuntamenti già presenti. Sono arrivati i pulsanti Modifica ed Elimina con icone, insieme ai pulsanti Salva e Annulla anch'essi resi più riconoscibili. L'icona del cestino e quella dell'annullamento sono rosse, una scelta piccola ma efficace: le azioni distruttive o di uscita diventano immediatamente distinguibili.

Gli eventi possono essere trascinati da un giorno all'altro. Durante il trascinamento compare un feedback visuale, così l'utente capisce sempre cosa sta spostando e dove. Con `CTRL + trascinamento`, invece, l'evento viene duplicato. È una funzione preziosa per chi ha appuntamenti simili in giorni diversi: non bisogna reinserire tutto, basta copiare e adattare.

Anche l'inserimento dell'orario è più comodo, grazie a una combobox che va da `00:00` a `23:45` con intervalli di 15 minuti. Meno digitazione libera, meno errori, più velocità.

## Una sidebar più matura

Un'altra trasformazione importante riguarda la struttura dell'interfaccia. In modalità estesa, il mini calendario laterale non viene più mostrato: resta disponibile nella modalità compatta, dove ha più senso. La zona sinistra è diventata una vera sidebar, più ordinata e più funzionale.

L'ordine è stato ripensato: mese, pannello di navigazione, lingua, modalità compatta, sempre in primo piano e filtri. In basso trova spazio una sezione Help con i comandi disponibili, pensata per ricordare all'utente i gesti principali: clic per aggiungere, trascinamento per spostare, `CTRL + trascinamento` per duplicare.

La navigazione stessa è stata alleggerita: il pulsante "Oggi" è stato sostituito da un'icona circolare con un punto al centro. È un piccolo cambio di linguaggio visivo: meno testo, più immediatezza.

## Lingue, temi e finestra sempre visibile

La versione 1.0.2 lavora molto anche sulla personalizzazione. Il cambio lingua è diventato più chiaro grazie all'uso delle bandiere italiana e inglese al posto dell'icona generica del mappamondo. È stato aggiunto un toggle per il tema chiaro/scuro, con correzioni per mantenere leggibili i filtri anche passando tra modalità compatta, tema scuro e vista estesa.

È arrivata anche la modalità "Sempre in primo piano", utile per chi vuole tenere il calendario visibile sopra alle altre finestre. È una funzione piccola solo in apparenza: per un calendario desktop, poter restare presente mentre si lavora altrove è una qualità molto concreta.

Importa, Esporta e Tema sono stati trasformati in pulsanti a icona, ridimensionati e organizzati sulla stessa riga degli altri controlli. La sidebar diventa così più compatta, più pulita e più adatta a un uso frequente.

## Importazione, esportazione e dialogo con Google Calendar

CalenDaros 1.0.2 apre anche una porta verso l'esterno. La sidebar include ora i comandi per importare ed esportare file `.ics`, il formato comunemente usato dai calendari digitali. L'importazione consente di selezionare dal computer un file compatibile, mentre l'esportazione permette di creare un file che può essere importato anche in Google Calendar.

Questo non trasforma CalenDaros in un servizio cloud, e va bene così. Resta un'applicazione desktop semplice, ma diventa meno isolata. Può dialogare con altri strumenti quando serve, mantenendo però la sua identità locale e leggera.

## Una griglia mensile più precisa

Anche la vista del mese è stata resa più solida. La griglia 7x6 mostra sempre giorni del mese precedente e del mese successivo, in modo da riempire completamente lo spazio disponibile.

Sono dettagli che si notano soprattutto quando mancano. Una griglia piena, regolare, prevedibile rende il calendario più facile da leggere e più gradevole da usare.

In modalità compatta, i giorni con appuntamenti sono evidenziati con uno sfondo diverso, e il mini calendario mostra l'anteprima degli appuntamenti tramite tooltip. Anche nella vista estesa, passando il mouse sugli eventi, compare la descrizione. Il calendario comunica di più senza costringere l'utente ad aprire ogni dettaglio.

## Meno rumore, più cura

La 1.0.2 porta con sé anche pulizia interna. Alcuni `System.out.println` di debug ancora presenti in produzione sono stati ricondotti alla classe `Debug`, già prevista dal progetto. È una scelta da manutentore: il codice non deve solo funzionare, deve anche restare leggibile e governabile.

È stato aggiunto un link "CalenDaros v1.0.2" nella sezione Help, collegato alla pagina del progetto. Anche questo contribuisce a dare identità all'applicazione: non è solo una finestra con pulsanti, ma un progetto con una storia, una versione, un punto di riferimento.

## In sintesi

CalenDaros 1.0.1 ha reso il progetto più facile da avviare e più ordinato nella struttura. Ha migliorato l'esperienza iniziale, soprattutto per gli utenti Windows, e ha preparato il terreno per l'internazionalizzazione.

CalenDaros 1.0.2 ha invece lavorato sull'uso quotidiano: salvataggio persistente, creazione rapida degli appuntamenti, modifica più comoda, drag and drop, duplicazione, tema scuro, import/export `.ics`, sidebar riorganizzata, tooltip, icone, griglia mensile più precisa e una generale pulizia dell'interfaccia.

Il risultato è un calendario desktop che cresce senza perdere la sua idea originale: aiutare a vedere il tempo, organizzare gli appuntamenti e farlo con uno strumento semplice, vicino, comprensibile. Un progetto piccolo nel formato, ma con una direzione chiara: diventare ogni versione un po' più utile, un po' più curato, un po' più personale.
