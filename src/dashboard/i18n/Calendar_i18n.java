package dashboard.i18n;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;

/**
 * Resource class for calendar localization.
 * Provides a Java-based alternative to properties files.
 */
public class Calendar_i18n {
    private static final Map<String, String> italianResources = new HashMap<>();
    private static final Map<String, String> englishResources = new HashMap<>();
    private static Map<String, String> resources = italianResources;
    private static String currentLanguage = "it";
    
    // Color constants
    private static final Map<String, Color> colorResources = new HashMap<>();
    
    // Dimension constants
    private static final Map<String, Dimension> dimensionResources = new HashMap<>();
    
    static {
        loadItalianResources();
        loadEnglishResources();

        // Initialize color resources
        colorResources.put("color.weekend_color_bg", new Color(220, 20, 60));
        colorResources.put("color.day_hover_color_bg", new Color(240, 240, 240));
        colorResources.put("color.day_selected_color_bd", new Color(229, 243, 255));
        colorResources.put("color.day_has_appointments_color_bg", new Color(255, 245, 205));
        colorResources.put("color.today_highlight_color", new Color(0, 120, 215));
        colorResources.put("color.navigation_button_color_bg", new Color(240, 240, 240));
        colorResources.put("color.navigation_button_color", new Color(50, 50, 50));
        colorResources.put("color.navigation_button_color_bd", new Color(214, 217, 223));
        colorResources.put("color.navigation_month_color", new Color(50, 50, 50));
        colorResources.put("color.mini_calendar_color_bg", new Color(255, 255, 255));
        colorResources.put("color.day_selected_color_bg", new Color(229, 243, 255));
        colorResources.put("color.day_of_week_color_bg", new Color(255, 255, 255));
        colorResources.put("color.empty_day_color_bg", new Color(245, 245, 245));
        colorResources.put("color.meeting_color_bg", new Color(200, 230, 255));
        colorResources.put("color.lunch_color_bg", new Color(255, 230, 230));
        colorResources.put("color.conference_color_bg", new Color(230, 255, 230));
        colorResources.put("color.course_color_bg", new Color(245, 230, 255));
        colorResources.put("color.other_color_bg", new Color(235, 235, 235));
        colorResources.put("color.header_color_bd", new Color(130, 130, 130));
        
        // Initialize dimension resources
        dimensionResources.put("dimension.mini_calendar", new Dimension(200, 200));
    }

    private static void loadItalianResources() {
        italianResources.put("app.title", "Calendario Appuntamenti");
        italianResources.put("dialog.appointments_title", "Appuntamenti");
        italianResources.put("dialog.appointments_for", "Appuntamenti del");
        italianResources.put("dialog.no_appointments", "Nessun appuntamento per questo giorno");
        italianResources.put("button.close", "Chiudi");
        italianResources.put("button.cancel", "Annulla");
        italianResources.put("button.save", "Salva");
        italianResources.put("button.edit", "Modifica");
        italianResources.put("button.delete", "Elimina");
        italianResources.put("button.import", "Importa");
        italianResources.put("button.import_tooltip", "Importa eventi da Google Calendar");
        italianResources.put("button.export", "Esporta");
        italianResources.put("button.export_tooltip", "Esporta eventi in formato .ics per Google Calendar");
        italianResources.put("button.google_calendar", "Google Calendar");
        italianResources.put("button.google_calendar_tooltip", "Apri Google Calendar");
        italianResources.put("button.dark_theme", "Tema scuro");
        italianResources.put("button.light_theme", "Tema chiaro");
        italianResources.put("button.theme_tooltip", "Passa dal tema chiaro al tema scuro");
        italianResources.put("button.language", "English");
        italianResources.put("button.today", "Oggi");
        italianResources.put("button.new_appointment", "Nuovo Appuntamento");
        italianResources.put("button.print_calendar", "Stampa Calendario");
        italianResources.put("button.your_mode", "Modalità");
        italianResources.put("button.compact_mode", "Modalità Compatta");
        italianResources.put("button.extended_mode", "Modalità Estesa");
        italianResources.put("button.always_on_top_off", "Sempre in primo piano: sì");
        italianResources.put("button.always_on_top_on", "Sempre in primo piano: no");
        italianResources.put("day.monday", "Lunedì");
        italianResources.put("day.tuesday", "Martedì");
        italianResources.put("day.wednesday", "Mercoledì");
        italianResources.put("day.thursday", "Giovedì");
        italianResources.put("day.friday", "Venerdì");
        italianResources.put("day.saturday", "Sabato");
        italianResources.put("day.sunday", "Domenica");
        italianResources.put("month.january", "Gennaio");
        italianResources.put("month.february", "Febbraio");
        italianResources.put("month.march", "Marzo");
        italianResources.put("month.april", "Aprile");
        italianResources.put("month.may", "Maggio");
        italianResources.put("month.june", "Giugno");
        italianResources.put("month.july", "Luglio");
        italianResources.put("month.august", "Agosto");
        italianResources.put("month.september", "Settembre");
        italianResources.put("month.october", "Ottobre");
        italianResources.put("month.november", "Novembre");
        italianResources.put("month.december", "Dicembre");
        italianResources.put("label.filters", "Filtri");
        italianResources.put("label.help", "Help");
        italianResources.put("help.new_event", "Click su un giorno: nuovo evento");
        italianResources.put("help.select_event", "Click su un evento: dettagli");
        italianResources.put("help.move_event", "Trascina evento: sposta");
        italianResources.put("help.copy_event", "CTRL + trascina evento: duplica");
        italianResources.put("label.details", "Dettagli");
        italianResources.put("label.select_appointment", "Seleziona un appuntamento per visualizzare i dettagli.");
        italianResources.put("form.time", "Ora");
        italianResources.put("form.title", "Titolo");
        italianResources.put("form.type", "Tipo");
        italianResources.put("form.description", "Descrizione");
        italianResources.put("form.required_fields", "Inserisci almeno ora e titolo dell'appuntamento.");
        italianResources.put("appointment.meeting", "Riunione");
        italianResources.put("appointment.lunch", "Pranzo");
        italianResources.put("appointment.conference", "Conferenza");
        italianResources.put("appointment.course", "Corso");
        italianResources.put("appointment.other", "Altro");
        italianResources.put("details.appointment", "Appuntamento: {0}");
        italianResources.put("details.day", "Giorno: {0} {1} {2}");
        italianResources.put("details.time", "Ora: {0}");
        italianResources.put("details.description", "Descrizione: {0}");
        italianResources.put("info.feature_not_implemented", "La funzionalità di {0} non è ancora implementata.");
        italianResources.put("info.title", "Informazione");
        italianResources.put("dialog.import_title", "Importa calendario");
        italianResources.put("dialog.import_success", "Importazione completata: {0} eventi importati, {1} duplicati ignorati.");
        italianResources.put("dialog.export_title", "Esporta calendario");
        italianResources.put("dialog.export_success", "Esportazione completata: {0} eventi esportati.");
        italianResources.put("dialog.export_empty", "Non ci sono eventi da esportare.");
        italianResources.put("confirm.delete_appointment", "Eliminare l''appuntamento del {0} alle {1}?");
        italianResources.put("confirm.overwrite_file", "Il file esiste già. Vuoi sovrascriverlo?");
        italianResources.put("error.icon_load", "Impossibile caricare l''icona: {0}");
        italianResources.put("error.appointments_load", "Impossibile caricare gli appuntamenti salvati: {0}");
        italianResources.put("error.appointments_save", "Impossibile salvare gli appuntamenti su disco: {0}");
        italianResources.put("error.import", "Impossibile importare il file .ics: {0}");
        italianResources.put("error.import_extension", "Seleziona un file con estensione .ics.");
        italianResources.put("error.export", "Impossibile esportare il file .ics: {0}");
        italianResources.put("error.open_link", "Impossibile aprire il link: {0}");
    }

    private static void loadEnglishResources() {
        englishResources.put("app.title", "Appointment Calendar");
        englishResources.put("dialog.appointments_title", "Appointments");
        englishResources.put("dialog.appointments_for", "Appointments for");
        englishResources.put("dialog.no_appointments", "No appointments for this day");
        englishResources.put("button.close", "Close");
        englishResources.put("button.cancel", "Cancel");
        englishResources.put("button.save", "Save");
        englishResources.put("button.edit", "Edit");
        englishResources.put("button.delete", "Delete");
        englishResources.put("button.import", "Import");
        englishResources.put("button.import_tooltip", "Import events from Google Calendar");
        englishResources.put("button.export", "Export");
        englishResources.put("button.export_tooltip", "Export events as an .ics file for Google Calendar");
        englishResources.put("button.google_calendar", "Google Calendar");
        englishResources.put("button.google_calendar_tooltip", "Open Google Calendar");
        englishResources.put("button.dark_theme", "Dark theme");
        englishResources.put("button.light_theme", "Light theme");
        englishResources.put("button.theme_tooltip", "Switch between light and dark theme");
        englishResources.put("button.language", "Italiano");
        englishResources.put("button.today", "Today");
        englishResources.put("button.new_appointment", "New Appointment");
        englishResources.put("button.print_calendar", "Print Calendar");
        englishResources.put("button.your_mode", "Mode");
        englishResources.put("button.compact_mode", "Compact Mode");
        englishResources.put("button.extended_mode", "Extended Mode");
        englishResources.put("button.always_on_top_off", "Always on top: yes");
        englishResources.put("button.always_on_top_on", "Always on top: no");
        englishResources.put("day.monday", "Monday");
        englishResources.put("day.tuesday", "Tuesday");
        englishResources.put("day.wednesday", "Wednesday");
        englishResources.put("day.thursday", "Thursday");
        englishResources.put("day.friday", "Friday");
        englishResources.put("day.saturday", "Saturday");
        englishResources.put("day.sunday", "Sunday");
        englishResources.put("month.january", "January");
        englishResources.put("month.february", "February");
        englishResources.put("month.march", "March");
        englishResources.put("month.april", "April");
        englishResources.put("month.may", "May");
        englishResources.put("month.june", "June");
        englishResources.put("month.july", "July");
        englishResources.put("month.august", "August");
        englishResources.put("month.september", "September");
        englishResources.put("month.october", "October");
        englishResources.put("month.november", "November");
        englishResources.put("month.december", "December");
        englishResources.put("label.filters", "Filters");
        englishResources.put("label.help", "Help");
        englishResources.put("help.new_event", "Click a day: new event");
        englishResources.put("help.select_event", "Click an event: details");
        englishResources.put("help.move_event", "Drag event: move");
        englishResources.put("help.copy_event", "CTRL + drag event: duplicate");
        englishResources.put("label.details", "Details");
        englishResources.put("label.select_appointment", "Select an appointment to view its details.");
        englishResources.put("form.time", "Time");
        englishResources.put("form.title", "Title");
        englishResources.put("form.type", "Type");
        englishResources.put("form.description", "Description");
        englishResources.put("form.required_fields", "Enter at least the appointment time and title.");
        englishResources.put("appointment.meeting", "Meeting");
        englishResources.put("appointment.lunch", "Lunch");
        englishResources.put("appointment.conference", "Conference");
        englishResources.put("appointment.course", "Course");
        englishResources.put("appointment.other", "Other");
        englishResources.put("details.appointment", "Appointment: {0}");
        englishResources.put("details.day", "Day: {0} {1} {2}");
        englishResources.put("details.time", "Time: {0}");
        englishResources.put("details.description", "Description: {0}");
        englishResources.put("info.feature_not_implemented", "The {0} feature is not implemented yet.");
        englishResources.put("info.title", "Information");
        englishResources.put("dialog.import_title", "Import calendar");
        englishResources.put("dialog.import_success", "Import completed: {0} events imported, {1} duplicates skipped.");
        englishResources.put("dialog.export_title", "Export calendar");
        englishResources.put("dialog.export_success", "Export completed: {0} events exported.");
        englishResources.put("dialog.export_empty", "There are no events to export.");
        englishResources.put("confirm.delete_appointment", "Delete the appointment on {0} at {1}?");
        englishResources.put("confirm.overwrite_file", "The file already exists. Do you want to overwrite it?");
        englishResources.put("error.icon_load", "Unable to load icon: {0}");
        englishResources.put("error.appointments_load", "Unable to load saved appointments: {0}");
        englishResources.put("error.appointments_save", "Unable to save appointments to disk: {0}");
        englishResources.put("error.import", "Unable to import the .ics file: {0}");
        englishResources.put("error.import_extension", "Select a file with the .ics extension.");
        englishResources.put("error.export", "Unable to export the .ics file: {0}");
        englishResources.put("error.open_link", "Unable to open the link: {0}");
    }

    /**
     * Gets a string from the resource bundle.
     * 
     * @param key the resource key
     * @return the string for the given key
     * @throws MissingResourceException if no object for the given key can be found
     */
    public static String getString(String key) {
        if (resources.containsKey(key)) {
            return resources.get(key);
        }
        throw new MissingResourceException("Can't find resource for key " + key, 
                                        Calendar_i18n.class.getName(), key);
    }

    public static void toggleLanguage() {
        if ("it".equals(currentLanguage)) {
            currentLanguage = "en";
            resources = englishResources;
        } else {
            currentLanguage = "it";
            resources = italianResources;
        }
    }

    public static boolean isCurrentLanguageItalian() {
        return "it".equals(currentLanguage);
    }

    public static boolean isAppointmentType(String type, String key) {
        return italianResources.get(key).equals(type) || englishResources.get(key).equals(type);
    }
    
    /**
     * Gets a color from the resource bundle.
     * 
     * @param key the resource key
     * @return the color for the given key
     * @throws MissingResourceException if no object for the given key can be found
     */
    public static Color getColor(String key) {
        if (colorResources.containsKey(key)) {
            return colorResources.get(key);
        }
        throw new MissingResourceException("Can't find color resource for key " + key, 
                                        Calendar_i18n.class.getName(), key);
    }
    
    /**
     * Gets a dimension from the resource bundle.
     * 
     * @param key the resource key
     * @return the dimension for the given key
     * @throws MissingResourceException if no object for the given key can be found
     */
    public static Dimension getDimension(String key) {
        if (dimensionResources.containsKey(key)) {
            return dimensionResources.get(key);
        }
        throw new MissingResourceException("Can't find dimension resource for key " + key, 
                                        Calendar_i18n.class.getName(), key);
    }
}
