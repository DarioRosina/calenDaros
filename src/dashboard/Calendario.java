package dashboard;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import dashboard.i18n.Calendar_i18n;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.Painter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Function;

/**
 * Applicazione calendario per la visualizzazione e gestione degli appuntamenti.
 * Fornisce un'interfaccia grafica con un mini calendario per la navigazione
 * e un pannello principale per visualizzare gli appuntamenti del mese.
 */
public class Calendario extends JFrame {
    // Calendar components
    private Calendar calendar;
    private JLabel monthLabel;
    private JButton prevButton;
    private JButton nextButton;
    private JButton todayButton;
    
    // Left panel components
    private MiniCalendarPanel miniCalendarPanel;
    private JPanel leftPanel;
    private JPanel navigationPanel;
    private JPanel controlsPanel;
    private JPanel helpPanel;
    
    // Checkbox filters
    private JCheckBox meetingsCheckbox;
    private JCheckBox lunchCheckbox;
    private JCheckBox conferenceCheckbox;
    private JCheckBox courseCheckbox;
    private JCheckBox otherCheckbox;
    
    // Main panel components
    private JPanel mainPanel;
    private JPanel appointmentPanel;
    private JPanel dayOfWeekHeaderPanel;
    private JPanel appointmentGridPanel;
    private JPanel detailsContentPanel;
    private JScrollPane appointmentScrollPane;
    private JPanel todayDayPanel;
    
    /**
     * Text area that displays details about the selected appointment.
     */
    private JTextArea appointmentDetails;
    private ArrayList<CalendarAppointment> customAppointments = new ArrayList<>();
    
    // Costanti per i colori
    private static final Color WEEKEND_COLOR_BG = Calendar_i18n.getColor("color.weekend_color_bg");
    private static final Color NAVIGATION_BUTTON_COLOR_BG = Calendar_i18n.getColor("color.navigation_button_color_bg");
    private static final Color NAVIGATION_BUTTON_COLOR = Calendar_i18n.getColor("color.navigation_button_color");
    private static final Color NAVIGATION_BUTTON_COLOR_BD = Calendar_i18n.getColor("color.navigation_button_color_bd");
    private static final Color NAVIGATION_MONTH_COLOR = Calendar_i18n.getColor("color.navigation_month_color");
    private static final Color MINI_CALENDAR_COLOR_BG = Calendar_i18n.getColor("color.mini_calendar_color_bg");
    private static final Color DAY_SELECTED_COLOR_BD = Calendar_i18n.getColor("color.day_selected_color_bd");
    private static final Color DAY_OF_WEEK_COLOR_BG = Calendar_i18n.getColor("color.day_of_week_color_bg");
    
    private static final Color MEETING_COLOR_BG = Calendar_i18n.getColor("color.meeting_color_bg");
    private static final Color LUNCH_COLOR_BG = Calendar_i18n.getColor("color.lunch_color_bg");
    private static final Color CONFERENCE_COLOR_BG = Calendar_i18n.getColor("color.conference_color_bg");
    private static final Color COURSE_COLOR_BG = Calendar_i18n.getColor("color.course_color_bg");
    private static final Color OTHER_COLOR_BG = Calendar_i18n.getColor("color.other_color_bg");
    private static final Color HEADER_COLOR_BD = Calendar_i18n.getColor("color.header_color_bd");
    // Costanti per dimensioni
    private static final Dimension MINI_CALENDAR_SIZE = Calendar_i18n.getDimension("dimension.mini_calendar");
    private static final Dimension LEFT_SIDEBAR_SIZE = new Dimension(170, 250);
    private static final Dimension LEFT_COMPACT_SIZE = new Dimension(250, 250);
    private static final Dimension DETAILS_PANEL_SIZE = new Dimension(0, 185);
    private static final Dimension NAVIGATION_ICON_BUTTON_SIZE = new Dimension(42, 28);
    private static final int MATERIAL_ICON_SIZE = 16;
    private static final int MONTH_ARROW_ICON_SIZE = 20;
    private static final Color ICON_EDIT_COLOR = new Color(234, 179, 8);
    private static final Color ICON_SAVE_COLOR = new Color(22, 163, 74);
    private static final Color ICON_DANGER_COLOR = new Color(220, 38, 38);
    private static final Color ICON_PIN_ON_COLOR = new Color(22, 163, 74);
    private static final Color ICON_PIN_OFF_COLOR = new Color(220, 38, 38);
    private static final String GOOGLE_CALENDAR_URL = "https://calendar.google.com/calendar";
    private static final String VERSION_PAGE_URL = "https://www.darioros.it/posts/calenDaros";
    private static final Path APPOINTMENTS_FILE = Paths.get(
        System.getProperty("user.home"), ".calenDaros", "appointments.properties");
    private static final Path UI_SETTINGS_FILE = Paths.get(
        System.getProperty("user.home"), ".calenDaros", "ui.properties");
    
    // Flag per tracciare la modalità di visualizzazione corrente
    private boolean compactMode = true;
    private boolean alwaysOnTopMode = false;
    private boolean darkMode = false;
    private Dimension extendedWindowSize = new Dimension(1020, 885);

    private static class CalendarAppointment {
        private final int year;
        private final int month;
        private final int day;
        private final String time;
        private final String title;
        private final String description;
        private final String type;

        private CalendarAppointment(int year, int month, int day, String time, String title, String description, String type) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.time = time;
            this.title = title;
            this.description = description;
            this.type = type;
        }
    }

    private class GradientPanel extends JPanel {
        private GradientPanel(LayoutManager layout) {
            super(layout);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            GradientPaint gradient = new GradientPaint(
                0, 0, getGradientStartColor(),
                getWidth(), Math.max(getHeight(), 1), getGradientEndColor()
            );
            g2.setPaint(gradient);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Creates a standard border for day panels
     */
    private Border createStandardDayBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(getBorderColor()),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        );
    }
    
    /**
     * Creates a highlighted border for the current day.
     * Used by updateAppointmentPanel() to highlight today's date.
     * @return A compound border with the today highlight color
     */
    private Border createTodayBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 120, 215)),
            BorderFactory.createEmptyBorder(3, 3, 3, 3)
        );
    }

    /**
     * Creates a header border for day of week labels
     */
    private Border createDayOfWeekHeaderBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, getBorderColor()),
            BorderFactory.createEmptyBorder(5, 2, 5, 2)
        );
    }

    private Color getAppBackground() {
        return darkMode ? new Color(24, 26, 32) : Color.WHITE;
    }

    private Color getGradientStartColor() {
        return darkMode ? new Color(19, 24, 38) : new Color(236, 247, 255);
    }

    private Color getGradientEndColor() {
        return darkMode ? new Color(39, 32, 55) : new Color(249, 241, 255);
    }

    private Color getPanelBackground() {
        return darkMode ? new Color(31, 34, 42) : new Color(238, 238, 238);
    }

    private Color getTextColor() {
        return darkMode ? new Color(232, 235, 242) : Color.BLACK;
    }

    private Color getMutedTextColor() {
        return darkMode ? new Color(145, 152, 166) : Color.GRAY;
    }

    private Color getControlBackground() {
        return darkMode ? new Color(35, 39, 49) : NAVIGATION_BUTTON_COLOR_BG;
    }

    private Color getControlHoverBackground() {
        return darkMode ? new Color(47, 52, 64) : NAVIGATION_BUTTON_COLOR_BG.darker();
    }

    private Color getControlForeground() {
        return darkMode ? new Color(245, 247, 250) : NAVIGATION_BUTTON_COLOR;
    }

    private Color getBorderColor() {
        return darkMode ? new Color(74, 81, 97) : NAVIGATION_BUTTON_COLOR_BD;
    }

    private Color getFocusBorderColor() {
        return darkMode ? new Color(125, 211, 252) : new Color(37, 99, 235);
    }

    private Color getHeaderBackground() {
        return darkMode ? new Color(43, 48, 61) : new Color(232, 242, 250);
    }

    private Color getSelectedDayBackground() {
        return darkMode ? new Color(45, 73, 100) : new Color(221, 239, 255);
    }

    private Color getDayBackground(boolean selected, boolean today, boolean currentMonth) {
        if (selected) {
            return getSelectedDayBackground();
        }
        if (today) {
            return darkMode ? new Color(40, 55, 70) : new Color(255, 251, 233);
        }
        if (!currentMonth) {
            return darkMode ? new Color(35, 38, 47) : new Color(248, 250, 252);
        }
        return darkMode ? new Color(43, 47, 58) : new Color(255, 255, 255);
    }

    private Color getWeekendTextColor(boolean currentMonth) {
        if (darkMode) {
            return currentMonth ? new Color(255, 135, 148) : new Color(174, 91, 104);
        }
        return currentMonth ? new Color(220, 20, 60) : new Color(180, 95, 105);
    }

    private Color getLinkColor() {
        return darkMode ? new Color(117, 184, 255) : new Color(0, 95, 170);
    }

    // Abilita log per stampare a console la dimensione dei pannelli
    private boolean debug = false;
    private final StartupPosition startupPosition;

    private enum StartupPosition {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        CENTER
    }
    
    /**
     * Costruttore della classe Calendario.
     * Inizializza l'interfaccia grafica e configura tutti i componenti necessari.
     */
    public Calendario() {
        this(false);
    }

    public Calendario(boolean alwaysOnTopMode) {
        this(alwaysOnTopMode, StartupPosition.TOP_LEFT, false, true);
    }

    public Calendario(boolean alwaysOnTopMode, StartupPosition startupPosition) {
        this(alwaysOnTopMode, startupPosition, false, true);
    }

    public Calendario(boolean alwaysOnTopMode, StartupPosition startupPosition, boolean darkMode, boolean compactMode) {
        this.alwaysOnTopMode = alwaysOnTopMode;
        this.startupPosition = startupPosition;
        this.darkMode = darkMode;
        this.compactMode = compactMode;
        loadUiSettingsFromDisk();
        configureTooltips();
        initializeFrame();
        initializeCalendar();
        createMainLayout();
        setupEventListeners();
        finalizeSetup();
    }
    
    private void initializeFrame() {
        setTitle(dashboard.i18n.Calendar_i18n.getString("app.title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupAccelerator();
        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        setMinimumSize(new Dimension(224, 224));
        setAlwaysOnTop(alwaysOnTopMode);
        setLocationRelativeTo(null);
        
        // Set application icon
        try {
            // Load the icon image
            ImageIcon icon = new ImageIcon(getClass().getResource("/dashboard/img/calendar.png"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            // If icon loading fails, log the error but continue
            System.err.println(MessageFormat.format(
                Calendar_i18n.getString("error.icon_load"), e.getMessage()));
        }
    }

    private void initializeCalendar() {
        // Initialize calendar instance
        calendar = Calendar.getInstance();
        loadAppointmentsFromDisk();
        
        // Create main panel
        mainPanel = new GradientPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
    } 

    private void createNavigationPanel() {
        navigationPanel = new JPanel(new BorderLayout(5, 0));
        markSidebarComponent(navigationPanel);
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Create month label
        monthLabel = new JLabel("", SwingConstants.CENTER);
        monthLabel.setFont(new Font("Arial", Font.BOLD, 16));
        monthLabel.setForeground(getTextColor());
        monthLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        
        // Create navigation buttons panel
        JPanel navButtonsPanel = createToolbarButtonPanel();
        markSidebarComponent(navButtonsPanel);
        
        // Style the navigation buttons
        prevButton = createStyledButton("", getControlBackground());
        setMonthArrowIcon(prevButton, "chevron_left", "<<");
        todayButton = createStyledButton("", getControlBackground());
        todayButton.setIcon(createTodayIcon());
        todayButton.setToolTipText(Calendar_i18n.getString("button.today"));
        todayButton.getAccessibleContext().setAccessibleName(Calendar_i18n.getString("button.today"));
        styleNavigationIconButton(todayButton);
        nextButton = createStyledButton("", getControlBackground());
        setMonthArrowIcon(nextButton, "chevron_right", ">>");
        
        // Add buttons to panel
        navButtonsPanel.add(prevButton);
        navButtonsPanel.add(todayButton);
        navButtonsPanel.add(nextButton);
        
        // Add month label at the top and navigation buttons at the bottom
        navigationPanel.add(monthLabel, BorderLayout.NORTH);
        navigationPanel.add(navButtonsPanel, BorderLayout.CENTER);
    }
    
    /**
     * Creates a styled button with custom colors and rounded borders
     */
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(bgColor);
        button.setForeground(getControlForeground());
        button.setFont(new Font("Arial", Font.BOLD, 12));
        configureButtonFocusStyle(button, 10);
        
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(getControlHoverBackground());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(getControlBackground());
            }
        });
        
        return button;
    }

    private void createMiniCalendarPanel() {
        // Create the mini calendar panel using the dedicated class
        miniCalendarPanel = new MiniCalendarPanel(calendar, monthLabel);
        miniCalendarPanel.setBackground(getAppBackground());
        
        // Set references for appointment updates
        if (appointmentDetails == null) {
            Debug.log("ERROR: appointmentDetails is null when setting up mini calendar");
        } else {
            Debug.log("Setting appointment details to mini calendar");
        }
        miniCalendarPanel.setAppointmentDetails(appointmentDetails);
        miniCalendarPanel.setAppointmentPanelUpdater(() -> updateAppointmentPanel());
        miniCalendarPanel.setAppointmentDayChecker(day -> compactMode && hasVisibleAppointmentsOnDay(day));
        miniCalendarPanel.setAppointmentPreviewProvider(day -> compactMode ? createAppointmentPreviewTooltip(day) : null);
        miniCalendarPanel.setVisible(compactMode);
    }

    private void createMainLayout() {
        // Create left panel for mini calendar and controls
        leftPanel = new JPanel(new BorderLayout(5, 10));
        updateLeftPanelSize();
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, getBorderColor()),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // Create and setup navigation panel
        createNavigationPanel();
        
        // Create details panel first
        createDetailsPanel();
        
        // Create mini calendar section
        createMiniCalendarPanel();
        
        // Create controls section
        createControlsPanel();
        
        // Add components to left panel
        leftPanel.add(controlsPanel, BorderLayout.NORTH);
        leftPanel.add(miniCalendarPanel, BorderLayout.CENTER);
        
        // Create and setup main appointment panel
        createAppointmentPanel();
        mainPanel.setVisible(!compactMode);
        
        // Add panels to frame
        add(leftPanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void updateLeftPanelSize() {
        Dimension size = compactMode ? LEFT_COMPACT_SIZE : LEFT_SIDEBAR_SIZE;
        leftPanel.setPreferredSize(size);
        leftPanel.setMinimumSize(size);
        leftPanel.setMaximumSize(new Dimension(size.width, Integer.MAX_VALUE));
    }

    private void setupEventListeners() {
        // Add button listeners
        prevButton.addActionListener(e -> {
            calendar.add(Calendar.MONTH, -1);
            miniCalendarPanel.updateDisplay();
            updateAppointmentPanel();
        });
        
        nextButton.addActionListener(e -> {
            calendar.add(Calendar.MONTH, 1);
            miniCalendarPanel.updateDisplay();
            updateAppointmentPanel();
        });
        
        todayButton.addActionListener(e -> {
            // Instead of creating a new Calendar instance, update the current one
            Calendar today = Calendar.getInstance();
            calendar.set(Calendar.YEAR, today.get(Calendar.YEAR));
            calendar.set(Calendar.MONTH, today.get(Calendar.MONTH));
            calendar.set(Calendar.DAY_OF_MONTH, today.get(Calendar.DAY_OF_MONTH));
            
            // Update displays
            miniCalendarPanel.updateDisplay();
            updateAppointmentPanel();
            scrollTodayIntoViewIfCurrentMonth();
        });

        // Add component size logger
        ComponentListener sizeLogger = new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (debug) {
                    Debug.logPanelSizes(Calendario.this, mainPanel, navigationPanel, miniCalendarPanel, 
                                    appointmentPanel, controlsPanel);
                }
            }
        };
        
        miniCalendarPanel.addComponentListener(sizeLogger);
        appointmentPanel.addComponentListener(sizeLogger);
        controlsPanel.addComponentListener(sizeLogger);
        mainPanel.addComponentListener(sizeLogger);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (!compactMode && mainPanel != null && mainPanel.isVisible()) {
                    rememberExtendedWindowSize();
                }
            }
        });
    }

    private void finalizeSetup() {
        // Initial updates
        miniCalendarPanel.updateDisplay();
        updateAppointmentPanel();
        
        // Apply debug colors if debug mode is on
        if (debug) {
            Debug.applyDebugColors(mainPanel, navigationPanel, miniCalendarPanel, appointmentPanel, controlsPanel);
        }
        
        // Pack the frame to respect component sizes
        pack();

        // Reset to desired size after packing
        setSize(compactMode ? new Dimension(getCompactWindowWidth(), 400) : extendedWindowSize);
        applyStartupPosition();
        if (!compactMode) {
            scrollTodayIntoViewIfCurrentMonth();
        }
    }

    private void applyStartupPosition() {
        if (startupPosition == StartupPosition.CENTER) {
            setLocationRelativeTo(null);
            return;
        }

        GraphicsConfiguration graphicsConfiguration = getGraphicsConfiguration();
        Rectangle screenBounds = graphicsConfiguration.getBounds();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(graphicsConfiguration);
        Rectangle usableBounds = new Rectangle(
            screenBounds.x + screenInsets.left,
            screenBounds.y + screenInsets.top,
            screenBounds.width - screenInsets.left - screenInsets.right,
            screenBounds.height - screenInsets.top - screenInsets.bottom
        );

        Dimension windowSize = getSize();
        int x = usableBounds.x;
        int y = usableBounds.y;

        switch (startupPosition) {
            case TOP_RIGHT:
                x = usableBounds.x + usableBounds.width - windowSize.width;
                break;
            case BOTTOM_LEFT:
                y = usableBounds.y + usableBounds.height - windowSize.height;
                break;
            case BOTTOM_RIGHT:
                x = usableBounds.x + usableBounds.width - windowSize.width;
                y = usableBounds.y + usableBounds.height - windowSize.height;
                break;
            case TOP_LEFT:
            default:
                break;
        }

        setLocation(Math.max(usableBounds.x, x), Math.max(usableBounds.y, y));
    }
    
    private void setupAccelerator() {
        KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_X, 
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
        getRootPane().registerKeyboardAction(
            e -> System.exit(0),
            keyStroke,
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }
    
    private void updateAppointmentPanel() {
        appointmentGridPanel.removeAll();
        todayDayPanel = null;
        updateDayOfWeekHeaderPanel();
        
        Calendar firstVisibleDay = (Calendar) calendar.clone();
        firstVisibleDay.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOffset = (firstVisibleDay.get(Calendar.DAY_OF_WEEK) + 5) % 7;
        if (firstDayOffset == 0) {
            firstDayOffset = 7;
        }
        firstVisibleDay.add(Calendar.DAY_OF_MONTH, -firstDayOffset);

        Calendar today = Calendar.getInstance();

        for (int cell = 0; cell < 42; cell++) {
            Calendar visibleDay = (Calendar) firstVisibleDay.clone();
            visibleDay.add(Calendar.DAY_OF_MONTH, cell);

            final int currentYear = visibleDay.get(Calendar.YEAR);
            final int currentMonth = visibleDay.get(Calendar.MONTH);
            final int currentDay = visibleDay.get(Calendar.DAY_OF_MONTH);
            boolean isCurrentMonth = currentYear == calendar.get(Calendar.YEAR)
                    && currentMonth == calendar.get(Calendar.MONTH);

            JPanel dayPanel = new JPanel();
            dayPanel.setLayout(new BoxLayout(dayPanel, BoxLayout.Y_AXIS));
            dayPanel.setBorder(createStandardDayBorder());
            dayPanel.putClientProperty("calendarDay", currentDay);
            dayPanel.putClientProperty("calendarMonth", currentMonth);
            dayPanel.putClientProperty("calendarYear", currentYear);
            dayPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Verifica se questo è il giorno selezionato
            boolean isSelectedDay = currentYear == calendar.get(Calendar.YEAR)
                    && currentMonth == calendar.get(Calendar.MONTH)
                    && currentDay == calendar.get(Calendar.DAY_OF_MONTH);
            
            // Evidenzia il giorno corrente
            boolean isToday = currentYear == today.get(Calendar.YEAR) &&
                            currentMonth == today.get(Calendar.MONTH) &&
                            currentDay == today.get(Calendar.DAY_OF_MONTH);
            if (isToday && isCurrentMonth) {
                todayDayPanel = dayPanel;
            }
            
            if (isSelectedDay) {
                Debug.logCalendarSelection("Found selected day in appointment panel", calendar);
                // Highlight the selected day with a special border
                dayPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 120, 215), 2, true),
                    BorderFactory.createEmptyBorder(2, 2, 2, 2)
                ));
                // Also set a background color to make it more visible
                dayPanel.setBackground(getDayBackground(true, isToday, isCurrentMonth));
                dayPanel.setOpaque(true);
            } else if (isToday) {
                dayPanel.setBorder(createTodayBorder());
                dayPanel.setBackground(getDayBackground(false, true, isCurrentMonth));
                dayPanel.setOpaque(true);
            } else {
                dayPanel.setBorder(createStandardDayBorder());
                dayPanel.setBackground(getDayBackground(false, false, isCurrentMonth));
                dayPanel.setOpaque(true);
            }
            
            // Aggiungi numero del giorno
            JLabel dayLabel = new JLabel(String.valueOf(currentDay));
            dayLabel.setFont(new Font("Arial", Font.BOLD, 14));
            if (!isCurrentMonth) {
                dayLabel.setForeground(getMutedTextColor());
            } else {
                dayLabel.setForeground(getTextColor());
            }
            
            // Set weekend days in red
            int dayOfWeek = visibleDay.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                dayLabel.setForeground(getWeekendTextColor(isCurrentMonth));
            }
            
            dayPanel.add(dayLabel);
            dayPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!compactMode && e.getSource() == dayPanel) {
                        calendar.set(Calendar.YEAR, currentYear);
                        calendar.set(Calendar.MONTH, currentMonth);
                        calendar.set(Calendar.DAY_OF_MONTH, currentDay);
                        miniCalendarPanel.updateDisplay();
                        updateAppointmentPanel();
                        showNewAppointmentForm(currentDay);
                    }
                }
            });
    
            addCustomAppointments(dayPanel, currentYear, currentMonth, currentDay);
    
            appointmentGridPanel.add(dayPanel);
        }

        appointmentGridPanel.revalidate();
        appointmentGridPanel.repaint();
    }

    private void addCustomAppointments(JPanel dayPanel, int year, int month, int day) {
        List<CalendarAppointment> appointmentsForDay = new ArrayList<>();
        for (CalendarAppointment appointment : customAppointments) {
            if (appointment.year == year &&
                    appointment.month == month &&
                    appointment.day == day &&
                    isAppointmentTypeVisible(appointment.type)) {
                appointmentsForDay.add(appointment);
            }
        }

        appointmentsForDay.sort(Comparator.comparingInt(appointment -> parseTimeToMinutes(appointment.time)));
        for (CalendarAppointment appointment : appointmentsForDay) {
            Appuntamenti.addAppointment(dayPanel, appointment.time, appointment.title, appointment.description,
                getAppointmentColor(appointment.type), calendar, appointmentDetails,
                (time, title, description, selectedDay) -> showAppointmentDetails(appointment),
                () -> showAppointmentEditor(appointment),
                (targetDate, duplicate) -> {
                    if (duplicate) {
                        duplicateAppointmentToDate(appointment, targetDate[0], targetDate[1], targetDate[2]);
                    } else {
                        moveAppointmentToDate(appointment, targetDate[0], targetDate[1], targetDate[2]);
                    }
                });
        }
    }

    private int parseTimeToMinutes(String time) {
        if (time == null || !time.matches("\\d{1,2}:\\d{2}")) {
            return Integer.MAX_VALUE;
        }

        String[] parts = time.split(":", 2);
        try {
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);
            if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
                return Integer.MAX_VALUE;
            }
            return hour * 60 + minute;
        } catch (NumberFormatException e) {
            return Integer.MAX_VALUE;
        }
    }

    private boolean isAppointmentTypeVisible(String type) {
        if (Calendar_i18n.isAppointmentType(type, "appointment.meeting")) {
            return meetingsCheckbox.isSelected();
        }
        if (Calendar_i18n.isAppointmentType(type, "appointment.lunch")) {
            return lunchCheckbox.isSelected();
        }
        if (Calendar_i18n.isAppointmentType(type, "appointment.conference")) {
            return conferenceCheckbox.isSelected();
        }
        if (Calendar_i18n.isAppointmentType(type, "appointment.course")) {
            return courseCheckbox.isSelected();
        }
        return otherCheckbox.isSelected();
    }

    private boolean hasVisibleAppointmentsOnDay(int day) {
        for (CalendarAppointment appointment : customAppointments) {
            if (appointment.year == calendar.get(Calendar.YEAR) &&
                    appointment.month == calendar.get(Calendar.MONTH) &&
                    appointment.day == day &&
                    isAppointmentTypeVisible(appointment.type)) {
                return true;
            }
        }
        return false;
    }

    private String createAppointmentPreviewTooltip(int day) {
        StringBuilder tooltip = new StringBuilder(
            "<html><body style='width:180px; padding:4px; font-family:Arial; font-size:11px'>");
        boolean hasAppointments = false;
        List<CalendarAppointment> appointmentsForDay = new ArrayList<>();

        for (CalendarAppointment appointment : customAppointments) {
            if (appointment.year == calendar.get(Calendar.YEAR) &&
                    appointment.month == calendar.get(Calendar.MONTH) &&
                    appointment.day == day &&
                    isAppointmentTypeVisible(appointment.type)) {
                appointmentsForDay.add(appointment);
            }
        }

        appointmentsForDay.sort(Comparator.comparingInt(appointment -> parseTimeToMinutes(appointment.time)));
        for (CalendarAppointment appointment : appointmentsForDay) {
            if (hasAppointments) {
                tooltip.append("<br>");
            }
            String description = appointment.description == null ? "" : appointment.description;
            tooltip.append("<b>")
                .append(escapeHtml(appointment.time == null ? "" : appointment.time))
                .append("</b> <span style='font-weight:bold'>")
                .append(escapeHtml(appointment.title == null ? "" : appointment.title))
                .append("</span>");
            if (!description.trim().isEmpty()) {
                tooltip.append("<br><span style='font-size:9px; color:#4B5563'>")
                    .append(escapeHtml(description))
                    .append("</span>");
            }
            hasAppointments = true;
        }

        if (!hasAppointments) {
            return null;
        }

        tooltip.append("</body></html>");
        return tooltip.toString();
    }

    private Color getAppointmentColor(String type) {
        if (Calendar_i18n.isAppointmentType(type, "appointment.lunch")) {
            return LUNCH_COLOR_BG;
        }
        if (Calendar_i18n.isAppointmentType(type, "appointment.conference")) {
            return CONFERENCE_COLOR_BG;
        }
        if (Calendar_i18n.isAppointmentType(type, "appointment.course")) {
            return COURSE_COLOR_BG;
        }
        if (Calendar_i18n.isAppointmentType(type, "appointment.other")) {
            return OTHER_COLOR_BG;
        }
        return MEETING_COLOR_BG;
    }

    private String getLocalizedAppointmentType(String type) {
        if (Calendar_i18n.isAppointmentType(type, "appointment.meeting")) {
            return Calendar_i18n.getString("appointment.meeting");
        }
        if (Calendar_i18n.isAppointmentType(type, "appointment.lunch")) {
            return Calendar_i18n.getString("appointment.lunch");
        }
        if (Calendar_i18n.isAppointmentType(type, "appointment.conference")) {
            return Calendar_i18n.getString("appointment.conference");
        }
        if (Calendar_i18n.isAppointmentType(type, "appointment.course")) {
            return Calendar_i18n.getString("appointment.course");
        }
        return Calendar_i18n.getString("appointment.other");
    }

    private void showAppointmentDetails(String time, String title, String description, int selectedDay) {
        showAppointmentDetails(time, title, description, Calendar_i18n.getString("appointment.other"), selectedDay);
    }

    private void showAppointmentDetails(String time, String title, String description, String type, int selectedDay) {
        calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
        detailsContentPanel.removeAll();
        detailsContentPanel.add(createAppointmentForm(selectedDay, time, title, description, type, false), BorderLayout.CENTER);
        detailsContentPanel.revalidate();
        detailsContentPanel.repaint();
    }

    private void showAppointmentDetails(CalendarAppointment appointment) {
        calendar.set(Calendar.YEAR, appointment.year);
        calendar.set(Calendar.MONTH, appointment.month);
        calendar.set(Calendar.DAY_OF_MONTH, appointment.day);
        detailsContentPanel.removeAll();
        detailsContentPanel.add(createAppointmentForm(
            appointment.day,
            appointment.time,
            appointment.title,
            appointment.description,
            appointment.type,
            false,
            appointment
        ), BorderLayout.CENTER);
        detailsContentPanel.revalidate();
        detailsContentPanel.repaint();
    }

    private void showAppointmentEditor(CalendarAppointment appointment) {
        calendar.set(Calendar.YEAR, appointment.year);
        calendar.set(Calendar.MONTH, appointment.month);
        calendar.set(Calendar.DAY_OF_MONTH, appointment.day);
        detailsContentPanel.removeAll();
        detailsContentPanel.add(createAppointmentForm(
            appointment.day,
            appointment.time,
            appointment.title,
            appointment.description,
            appointment.type,
            true,
            appointment
        ), BorderLayout.CENTER);
        detailsContentPanel.revalidate();
        detailsContentPanel.repaint();
    }

    private void showNewAppointmentForm(int day) {
        detailsContentPanel.removeAll();
        detailsContentPanel.add(createNewAppointmentForm(day), BorderLayout.CENTER);
        detailsContentPanel.revalidate();
        detailsContentPanel.repaint();
    }

    private JPanel createNewAppointmentForm(int day) {
        return createAppointmentForm(
            day,
            getCurrentQuarterHourTime(),
            "",
            "",
            Calendar_i18n.getString("appointment.other"),
            true,
            null,
            true
        );
    }

    private JPanel createAppointmentForm(int day, String time, String title, String description, String type, boolean editable) {
        return createAppointmentForm(day, time, title, description, type, editable, null);
    }

    private JPanel createAppointmentForm(int day, String time, String title, String description, String type,
            boolean editable, CalendarAppointment appointment) {
        return createAppointmentForm(day, time, title, description, type, editable, appointment, false);
    }

    private JPanel createAppointmentForm(int day, String time, String title, String description, String type,
            boolean editable, CalendarAppointment appointment, boolean openTimeCombo) {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField timeField = new JTextField(time);
        JTextField titleField = new JTextField(title);
        JComboBox<String> typeCombo = new JComboBox<>(new String[] {
            Calendar_i18n.getString("appointment.meeting"),
            Calendar_i18n.getString("appointment.lunch"),
            Calendar_i18n.getString("appointment.conference"),
            Calendar_i18n.getString("appointment.course"),
            Calendar_i18n.getString("appointment.other")
        });
        typeCombo.setSelectedItem(getLocalizedAppointmentType(type));
        JTextArea descriptionArea = new JTextArea(3, 20);
        descriptionArea.setText(description);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        timeField.setEditable(editable);
        titleField.setEditable(editable);
        descriptionArea.setEditable(editable);
        typeCombo.setEnabled(editable);
        if (editable) {
            bindDescriptionTabToTypeCombo(descriptionArea, typeCombo);
        }

        addFormRow(formPanel, gbc, 0, Calendar_i18n.getString("form.time"), createTimeInputPanel(timeField, editable, openTimeCombo, titleField));
        addFormRow(formPanel, gbc, 1, Calendar_i18n.getString("form.title"), titleField);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        formPanel.add(new JLabel(Calendar_i18n.getString("form.description")), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(new JScrollPane(descriptionArea), gbc);
        addFormRow(formPanel, gbc, 3, Calendar_i18n.getString("form.type"), typeCombo);

        if (editable) {
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
            JButton cancelButton = createIconButton(createCancelIcon(), Calendar_i18n.getString("button.cancel"));
            JButton saveButton = createIconButton(createSaveIcon(), Calendar_i18n.getString("button.save"));
            cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            bindTypeComboToSaveButton(typeCombo, saveButton);
            bindSaveButtonEnter(saveButton);
            buttonPanel.add(saveButton);
            buttonPanel.add(Box.createVerticalStrut(6));
            buttonPanel.add(cancelButton);
            buttonPanel.add(Box.createVerticalStrut(8));
            buttonPanel.add(createDayMonthPanel(day));

            gbc.gridx = 2;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.gridheight = 4;
            gbc.weightx = 0;
            gbc.weighty = 1;
            gbc.fill = GridBagConstraints.VERTICAL;
            gbc.anchor = GridBagConstraints.NORTH;
            formPanel.add(buttonPanel, gbc);
            gbc.gridheight = 1;
            gbc.weighty = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.CENTER;

            cancelButton.addActionListener(e -> {
                if (appointment != null) {
                    showAppointmentDetails(appointment);
                } else {
                    showEmptyAppointmentForm();
                }
            });
            saveButton.addActionListener(e -> {
                if (appointment != null) {
                    updateAppointment(appointment, day, timeField, titleField, descriptionArea, typeCombo);
                } else {
                    saveNewAppointment(day, timeField, titleField, descriptionArea, typeCombo);
                }
            });
            if (!openTimeCombo) {
                SwingUtilities.invokeLater(() -> titleField.requestFocusInWindow());
            }
        } else if (appointment != null) {
            JPanel actionPanel = new JPanel();
            actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));

            JButton editButton = createIconButton(createPencilIcon(), Calendar_i18n.getString("button.edit"));
            JButton deleteButton = createIconButton(createTrashIcon(), Calendar_i18n.getString("button.delete"));
            editButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            actionPanel.add(editButton);
            actionPanel.add(Box.createVerticalStrut(6));
            actionPanel.add(deleteButton);
            actionPanel.add(Box.createVerticalStrut(8));
            actionPanel.add(createDayMonthPanel(day));

            gbc.gridx = 2;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.gridheight = 4;
            gbc.weightx = 0;
            gbc.weighty = 1;
            gbc.fill = GridBagConstraints.VERTICAL;
            gbc.anchor = GridBagConstraints.NORTH;
            formPanel.add(actionPanel, gbc);
            gbc.gridheight = 1;
            gbc.weighty = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.CENTER;

            editButton.addActionListener(e -> showAppointmentEditor(appointment));
            deleteButton.addActionListener(e -> deleteAppointment(appointment));
        }

        applyThemeToComponent(formPanel);
        return formPanel;
    }

    private void bindDescriptionTabToTypeCombo(JTextArea descriptionArea, JComboBox<String> typeCombo) {
        descriptionArea.setFocusTraversalKeysEnabled(false);
        InputMap inputMap = descriptionArea.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap actionMap = descriptionArea.getActionMap();
        String actionKey = "openTypeCombo";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), actionKey);
        actionMap.put(actionKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                typeCombo.putClientProperty("openedFromDescriptionTab", Boolean.TRUE);
                Timer clearOpeningGuard = new Timer(600, event ->
                    typeCombo.putClientProperty("openedFromDescriptionTab", Boolean.FALSE));
                clearOpeningGuard.setRepeats(false);
                clearOpeningGuard.start();
                typeCombo.requestFocusInWindow();
                openComboPopupAfterFocus(typeCombo);
            }
        });
    }

    private void openComboPopupAfterFocus(JComboBox<String> comboBox) {
        Timer timer = new Timer(250, e -> {
            comboBox.requestFocusInWindow();
            comboBox.showPopup();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void bindTypeComboToSaveButton(JComboBox<String> typeCombo, JButton saveButton) {
        typeCombo.setFocusTraversalKeysEnabled(false);
        typeCombo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    typeCombo.putClientProperty("openedFromDescriptionTab", Boolean.FALSE);
                }
            }
        });
        InputMap inputMap = typeCombo.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap actionMap = typeCombo.getActionMap();
        String actionKey = "focusSaveButton";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), actionKey);
        actionMap.put(actionKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Boolean.TRUE.equals(typeCombo.getClientProperty("openedFromDescriptionTab"))) {
                    return;
                }
                if (typeCombo.isPopupVisible()) {
                    typeCombo.setPopupVisible(false);
                }
                saveButton.requestFocusInWindow();
            }
        });
    }

    private void bindSaveButtonEnter(JButton saveButton) {
        InputMap inputMap = saveButton.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap actionMap = saveButton.getActionMap();
        String actionKey = "pressSaveButton";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), actionKey);
        actionMap.put(actionKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveButton.doClick();
            }
        });
    }

    private JPanel createDayMonthPanel(int day) {
        JPanel datePanel = new JPanel();
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.Y_AXIS));
        datePanel.setOpaque(false);
        datePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        datePanel.add(createDayNumberLabel(day));
        datePanel.add(createMonthNumberLabel());
        return datePanel;
    }

    private JLabel createDayNumberLabel(int day) {
        JLabel dayLabel = new JLabel(String.valueOf(day), SwingConstants.CENTER);
        dayLabel.setFont(new Font("Arial", Font.BOLD, 16));
        dayLabel.setForeground(getTextColor());
        dayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dayLabel.setMinimumSize(new Dimension(32, 22));
        dayLabel.setPreferredSize(new Dimension(32, 22));
        dayLabel.setMaximumSize(new Dimension(32, 22));
        return dayLabel;
    }

    private JLabel createMonthNumberLabel() {
        JLabel monthLabel = new JLabel(String.format("%02d", calendar.get(Calendar.MONTH) + 1), SwingConstants.CENTER);
        monthLabel.setFont(new Font("Arial", Font.BOLD, 12));
        monthLabel.setForeground(getTextColor());
        monthLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        monthLabel.setMinimumSize(new Dimension(32, 18));
        monthLabel.setPreferredSize(new Dimension(32, 18));
        monthLabel.setMaximumSize(new Dimension(32, 18));
        return monthLabel;
    }

    private JPanel createTimeInputPanel(JTextField timeField, boolean editable) {
        return createTimeInputPanel(timeField, editable, false);
    }

    private JPanel createTimeInputPanel(JTextField timeField, boolean editable, boolean openTimeCombo) {
        return createTimeInputPanel(timeField, editable, openTimeCombo, null);
    }

    private JPanel createTimeInputPanel(JTextField timeField, boolean editable, boolean openTimeCombo, JTextField focusAfterSelection) {
        JPanel timePanel = new JPanel(new BorderLayout(6, 0));
        JComboBox<String> timeCombo = new JComboBox<>(createQuarterHourOptions());
        timeCombo.setSelectedItem(normalizeTimeSelection(timeField.getText()));
        timeCombo.setEnabled(editable);
        bindTimeComboKeyboardNavigation(timeCombo);
        timeCombo.addActionListener(e -> {
            Object selectedTime = timeCombo.getSelectedItem();
            if (selectedTime != null) {
                timeField.setText(selectedTime.toString());
                if (focusAfterSelection != null && !isTimeComboKeyboardNavigation(timeCombo)) {
                    SwingUtilities.invokeLater(() -> focusAfterSelection.requestFocusInWindow());
                }
            }
        });

        timePanel.add(timeField, BorderLayout.CENTER);
        timePanel.add(timeCombo, BorderLayout.EAST);

        if (editable && openTimeCombo) {
            SwingUtilities.invokeLater(() -> {
                timeCombo.requestFocusInWindow();
                timeCombo.showPopup();
            });
        }

        return timePanel;
    }

    private void bindTimeComboKeyboardNavigation(JComboBox<String> timeCombo) {
        timeCombo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (timeCombo.isPopupVisible() && isVerticalArrowKey(e)) {
                    timeCombo.putClientProperty("timeComboKeyboardNavigation", Boolean.TRUE);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (isVerticalArrowKey(e)) {
                    SwingUtilities.invokeLater(() ->
                        timeCombo.putClientProperty("timeComboKeyboardNavigation", Boolean.FALSE));
                }
            }
        });
    }

    private boolean isTimeComboKeyboardNavigation(JComboBox<String> timeCombo) {
        return Boolean.TRUE.equals(timeCombo.getClientProperty("timeComboKeyboardNavigation"));
    }

    private boolean isVerticalArrowKey(KeyEvent e) {
        return e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN;
    }

    private String[] createQuarterHourOptions() {
        String[] options = new String[24 * 4];
        int index = 0;
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 15) {
                options[index++] = String.format("%02d:%02d", hour, minute);
            }
        }
        return options;
    }

    private String normalizeTimeSelection(String time) {
        if (time == null || !time.matches("\\d{1,2}:\\d{2}")) {
            return "09:00";
        }

        String[] parts = time.split(":", 2);
        try {
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);
            if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
                return "09:00";
            }
            int roundedMinute = (minute / 15) * 15;
            return String.format("%02d:%02d", hour, roundedMinute);
        } catch (NumberFormatException e) {
            return "09:00";
        }
    }

    private String getCurrentQuarterHourTime() {
        LocalTime now = LocalTime.now();
        int totalMinutes = now.getHour() * 60 + now.getMinute();
        int roundedMinutes = ((totalMinutes + 14) / 15) * 15;
        int normalizedMinutes = roundedMinutes % (24 * 60);
        return String.format("%02d:%02d", normalizedMinutes / 60, normalizedMinutes % 60);
    }

    private JButton createIconButton(Icon icon, String tooltip) {
        JButton button = new JButton(icon);
        button.setToolTipText(tooltip);
        button.getAccessibleContext().setAccessibleName(tooltip);
        button.setPreferredSize(new Dimension(32, 28));
        button.setMaximumSize(new Dimension(32, 28));
        button.setBackground(getControlBackground());
        button.setForeground(getControlForeground());
        button.setFocusPainted(false);
        configureButtonFocusStyle(button, 6);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void configureButtonFocusStyle(JButton button, int horizontalPadding) {
        button.putClientProperty("horizontalPadding", horizontalPadding);
        updateButtonFocusBorder(button);
        button.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                updateButtonFocusBorder(button);
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateButtonFocusBorder(button);
            }
        });
    }

    private void updateButtonFocusBorder(JButton button) {
        int horizontalPadding = 10;
        Object padding = button.getClientProperty("horizontalPadding");
        if (padding instanceof Integer) {
            horizontalPadding = (Integer) padding;
        }
        Color borderColor = button.hasFocus() ? getFocusBorderColor() : getBorderColor();
        int borderWidth = button.hasFocus() ? 2 : 1;
        int verticalPadding = button.hasFocus() ? 3 : 4;
        int adjustedHorizontalPadding = Math.max(0, horizontalPadding - (borderWidth - 1));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor, borderWidth, true),
            BorderFactory.createEmptyBorder(verticalPadding, adjustedHorizontalPadding, verticalPadding, adjustedHorizontalPadding)
        ));
    }

    private Icon createMaterialIcon(String name, Color color, Icon fallback) {
        return createMaterialIcon(name, MATERIAL_ICON_SIZE, color, fallback);
    }

    private Icon createMaterialIcon(String name, int size, Color color, Icon fallback) {
        try {
            Class<?> svgIconClass = Class.forName("com.formdev.flatlaf.extras.FlatSVGIcon");
            Constructor<?> constructor = svgIconClass.getConstructor(String.class, int.class, int.class);
            Object icon = constructor.newInstance("dashboard/img/material/" + name + ".svg", size, size);
            applyMaterialIconColor(svgIconClass, icon, color);
            return icon instanceof Icon ? (Icon) icon : fallback;
        } catch (ReflectiveOperationException | LinkageError e) {
            return fallback;
        }
    }

    private Icon createSvgIcon(String resourcePath, int size, Icon fallback) {
        try {
            Class<?> svgIconClass = Class.forName("com.formdev.flatlaf.extras.FlatSVGIcon");
            Constructor<?> constructor = svgIconClass.getConstructor(String.class, int.class, int.class);
            Object icon = constructor.newInstance(resourcePath, size, size);
            return icon instanceof Icon ? (Icon) icon : fallback;
        } catch (ReflectiveOperationException | LinkageError e) {
            return fallback;
        }
    }

    private void applyMaterialIconColor(Class<?> svgIconClass, Object icon, Color color)
            throws ReflectiveOperationException {
        Class<?> colorFilterClass = Class.forName("com.formdev.flatlaf.extras.FlatSVGIcon$ColorFilter");
        Constructor<?> colorFilterConstructor = colorFilterClass.getConstructor(Function.class);
        Object colorFilter = colorFilterConstructor.newInstance((Function<Color, Color>) original -> color);
        Method setColorFilterMethod = svgIconClass.getMethod("setColorFilter", colorFilterClass);
        setColorFilterMethod.invoke(icon, colorFilter);
    }

    private Icon createPencilIcon() {
        Color color = ICON_EDIT_COLOR;
        Icon fallback = new Icon() {
            @Override
            public int getIconWidth() {
                return 16;
            }

            @Override
            public int getIconHeight() {
                return 16;
            }

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.rotate(Math.toRadians(90), 8, 8);

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.translate(x + 8, y-22);
                g2.rotate(Math.toRadians(-45), 8, 8);

                BasicStroke thick = new BasicStroke(1.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
                BasicStroke thin  = new BasicStroke(0.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
                Color outline = new Color(26, 26, 26);

                // === GOMMA (rosa) ===
                RoundRectangle2D eraser = new RoundRectangle2D.Float(4f, 0.5f, 8f, 3f, 2f, 2f);
                g2.setColor(new Color(242, 139, 130));
                g2.fill(eraser);
                g2.setColor(outline);
                g2.setStroke(thick);
                g2.draw(eraser);

                // === FASCETTA (grigio metallo, 2 anelli) ===
                g2.setColor(new Color(176, 184, 193));
                g2.fillRect(5, 3, 6, 3);
                g2.setColor(outline);
                g2.setStroke(thick);
                g2.draw(new RoundRectangle2D.Float(4.5f, 3.2f, 7f, 1.3f, 1f, 1f));
                g2.draw(new RoundRectangle2D.Float(4.5f, 4.7f, 7f, 1.3f, 1f, 1f));

                // === CORPO (giallo) ===
                g2.setColor(new Color(255, 214, 0));
                g2.fillRect(4, 6, 8, 7);
                g2.setColor(outline);
                g2.setStroke(thick);
                g2.drawRect(4, 6, 8, 7);
                g2.setStroke(thin);
                g2.drawLine(7, 6, 7, 13);
                g2.drawLine(10, 6, 10, 13);

                // === PUNTA (legno beige) ===
                Polygon tip = new Polygon(
                    new int[] { 4, 8, 12 },
                    new int[] { 13, 16, 13 },
                    3
                );
                g2.setColor(new Color(242, 201, 138));
                g2.fillPolygon(tip);
                g2.setColor(outline);
                g2.setStroke(thick);
                g2.drawPolygon(tip);
                g2.setStroke(thin);
                g2.drawLine(7, 13, 8, 15);
                g2.drawLine(10, 13, 8, 15);

                // === MINA (grigio ardesia) ===
                Polygon mine = new Polygon(
                    new int[] { 6, 8, 10 },
                    new int[] { 15, 16, 15 },
                    3
                );
                g2.setColor(new Color(74, 85, 104));
                g2.fillPolygon(mine);
                g2.setColor(outline);
                g2.setStroke(thin);
                g2.drawPolygon(mine);

                g2.dispose();
            }
        };
        return createMaterialIcon("edit", color, fallback);
    }

    private Icon createSaveIcon() {
        Color color = ICON_SAVE_COLOR;
        Icon fallback = new Icon() {
            @Override
            public int getIconWidth() {
                return 16;
            }

            @Override
            public int getIconHeight() {
                return 16;
            }

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawRoundRect(x + 3, y + 2, 10, 12, 2, 2);
                g2.drawLine(x + 5, y + 2, x + 5, y + 6);
                g2.drawLine(x + 5, y + 6, x + 11, y + 6);
                g2.drawLine(x + 11, y + 2, x + 11, y + 6);
                g2.drawLine(x + 5, y + 14, x + 5, y + 10);
                g2.drawLine(x + 5, y + 10, x + 11, y + 10);
                g2.drawLine(x + 11, y + 10, x + 11, y + 14);
                g2.dispose();
            }
        };
        return createMaterialIcon("save", color, fallback);
    }

    private Icon createCancelIcon() {
        Color color = ICON_DANGER_COLOR;
        Icon fallback = new Icon() {
            @Override
            public int getIconWidth() {
                return 16;
            }

            @Override
            public int getIconHeight() {
                return 16;
            }

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(x + 4, y + 4, x + 12, y + 12);
                g2.drawLine(x + 12, y + 4, x + 4, y + 12);
                g2.dispose();
            }
        };
        return createMaterialIcon("close", color, fallback);
    }

    private Icon createTodayIcon() {
        Color color = getControlForeground();
        Icon fallback = new Icon() {
            @Override
            public int getIconWidth() {
                return 16;
            }

            @Override
            public int getIconHeight() {
                return 16;
            }

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawOval(x + 3, y + 3, 10, 10);
                g2.fillOval(x + 7, y + 7, 3, 3);
                g2.dispose();
            }
        };
        return createMaterialIcon("calendar_today", color, fallback);
    }

    private Icon createLanguageIcon(boolean italianFlag) {
        Icon fallback = new Icon() {
            @Override
            public int getIconWidth() {
                return 16;
            }

            @Override
            public int getIconHeight() {
                return 16;
            }

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int flagX = x + 1;
                int flagY = y + 3;
                int width = 14;
                int height = 10;
                RoundRectangle2D flagShape = new RoundRectangle2D.Float(flagX, flagY, width, height, 3, 3);
                Shape oldClip = g2.getClip();
                g2.setClip(flagShape);

                if (italianFlag) {
                    g2.setColor(new Color(0, 146, 70));
                    g2.fillRect(flagX, flagY, 5, height);
                    g2.setColor(Color.WHITE);
                    g2.fillRect(flagX + 5, flagY, 5, height);
                    g2.setColor(new Color(206, 43, 55));
                    g2.fillRect(flagX + 10, flagY, 4, height);
                } else {
                    g2.setColor(new Color(1, 33, 105));
                    g2.fillRect(flagX, flagY, width, height);
                    g2.setColor(Color.WHITE);
                    g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
                    g2.drawLine(flagX, flagY, flagX + width, flagY + height);
                    g2.drawLine(flagX + width, flagY, flagX, flagY + height);
                    g2.setColor(new Color(200, 16, 46));
                    g2.setStroke(new BasicStroke(1.4f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
                    g2.drawLine(flagX, flagY, flagX + width, flagY + height);
                    g2.drawLine(flagX + width, flagY, flagX, flagY + height);
                    g2.setColor(Color.WHITE);
                    g2.fillRect(flagX + 5, flagY, 4, height);
                    g2.fillRect(flagX, flagY + 3, width, 4);
                    g2.setColor(new Color(200, 16, 46));
                    g2.fillRect(flagX + 6, flagY, 2, height);
                    g2.fillRect(flagX, flagY + 4, width, 2);
                }

                g2.setClip(oldClip);
                g2.setColor(darkMode ? new Color(108, 116, 132) : new Color(156, 163, 175));
                g2.setStroke(new BasicStroke(1f));
                g2.draw(flagShape);
                g2.dispose();
            }
        };
        return fallback;
    }

    private Icon createViewModeIcon(boolean compact) {
        Color color = getControlForeground();
        Icon fallback = new Icon() {
            @Override
            public int getIconWidth() {
                return 16;
            }

            @Override
            public int getIconHeight() {
                return 16;
            }

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                if (compact) {
                    g2.drawRect(x + 5, y + 2, 6, 12);
                    g2.drawLine(x + 5, y + 5, x + 11, y + 5);
                    g2.drawLine(x + 5, y + 8, x + 11, y + 8);
                    g2.drawLine(x + 5, y + 11, x + 11, y + 11);
                } else {
                    g2.drawRect(x + 2, y + 3, 12, 10);
                    g2.drawLine(x + 6, y + 3, x + 6, y + 13);
                    g2.drawLine(x + 10, y + 3, x + 10, y + 13);
                    g2.drawLine(x + 2, y + 8, x + 14, y + 8);
                }
                g2.dispose();
            }
        };
        return createMaterialIcon(compact ? "close_fullscreen" : "open_in_full", color, fallback);
    }

    private Icon createAlwaysOnTopIcon(boolean active) {
        Color color = active ? ICON_PIN_ON_COLOR : ICON_PIN_OFF_COLOR;
        Icon fallback = new Icon() {
            @Override
            public int getIconWidth() {
                return 16;
            }

            @Override
            public int getIconHeight() {
                return 16;
            }

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(226, 58, 14));
                g2.fillOval(x + 5, y + 1, 6, 5);
                g2.fillRoundRect(x + 4, y + 4, 8, 5, 2, 2);
                g2.fillOval(x + 3, y + 6, 10, 5);

                g2.setColor(new Color(184, 45, 10));
                g2.fillRect(x + 8, y + 4, 4, 5);
                g2.fillArc(x + 8, y + 6, 5, 5, -90, 180);

                g2.setColor(new Color(94, 120, 132));
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(x + 8, y + 10, x + 8, y + 14);
                g2.setColor(new Color(168, 188, 196));
                g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(x + 8, y + 10, x + 8, y + 13);

                if (!active) {
                    g2.setStroke(new BasicStroke(3.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.setColor(getControlBackground());
                    g2.drawLine(x + 2, y + 2, x + 14, y + 14);
                    g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.setColor(new Color(210, 45, 45));
                    g2.drawLine(x + 2, y + 2, x + 14, y + 14);
                }
                g2.dispose();
            }
        };
        return createMaterialIcon(active ? "push_pin" : "keep_off", color, fallback);
    }

    private Icon createImportIcon() {
        Color color = getControlForeground();
        Icon fallback = new Icon() {
            @Override
            public int getIconWidth() {
                return 16;
            }

            @Override
            public int getIconHeight() {
                return 16;
            }

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(x + 8, y + 2, x + 8, y + 10);
                g2.drawLine(x + 5, y + 7, x + 8, y + 10);
                g2.drawLine(x + 11, y + 7, x + 8, y + 10);
                g2.drawLine(x + 3, y + 13, x + 13, y + 13);
                g2.drawLine(x + 3, y + 10, x + 3, y + 13);
                g2.drawLine(x + 13, y + 10, x + 13, y + 13);
                g2.dispose();
            }
        };
        return createMaterialIcon("file_download", color, fallback);
    }

    private Icon createExportIcon() {
        Color color = getControlForeground();
        Icon fallback = new Icon() {
            @Override
            public int getIconWidth() {
                return 16;
            }

            @Override
            public int getIconHeight() {
                return 16;
            }

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(x + 8, y + 11, x + 8, y + 3);
                g2.drawLine(x + 5, y + 6, x + 8, y + 3);
                g2.drawLine(x + 11, y + 6, x + 8, y + 3);
                g2.drawLine(x + 3, y + 13, x + 13, y + 13);
                g2.drawLine(x + 3, y + 10, x + 3, y + 13);
                g2.drawLine(x + 13, y + 10, x + 13, y + 13);
                g2.dispose();
            }
        };
        return createMaterialIcon("file_upload", color, fallback);
    }

    private Icon createExternalCalendarIcon() {
        Color color = getControlForeground();
        Icon fallback = new Icon() {
            @Override
            public int getIconWidth() {
                return 16;
            }

            @Override
            public int getIconHeight() {
                return 16;
            }

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawRoundRect(x + 2, y + 5, 10, 9, 2, 2);
                g2.drawLine(x + 2, y + 8, x + 12, y + 8);
                g2.drawLine(x + 5, y + 3, x + 5, y + 6);
                g2.drawLine(x + 9, y + 3, x + 9, y + 6);
                g2.drawLine(x + 9, y + 2, x + 14, y + 2);
                g2.drawLine(x + 14, y + 2, x + 14, y + 7);
                g2.drawLine(x + 10, y + 6, x + 14, y + 2);
                g2.dispose();
            }
        };
        return createSvgIcon("dashboard/img/google_calendar_icon.svg", MATERIAL_ICON_SIZE, fallback);
    }

    private Icon createThemeIcon(boolean dark) {
        Color color = getControlForeground();
        Icon fallback = new Icon() {
            @Override
            public int getIconWidth() {
                return 16;
            }

            @Override
            public int getIconHeight() {
                return 16;
            }

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                if (dark) {
                    g2.fillArc(x + 3, y + 2, 11, 11, 80, 230);
                    g2.setColor(getControlBackground());
                    g2.fillOval(x + 7, y + 1, 8, 10);
                } else {
                    g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawOval(x + 5, y + 5, 6, 6);
                    g2.drawLine(x + 8, y + 1, x + 8, y + 3);
                    g2.drawLine(x + 8, y + 13, x + 8, y + 15);
                    g2.drawLine(x + 1, y + 8, x + 3, y + 8);
                    g2.drawLine(x + 13, y + 8, x + 15, y + 8);
                    g2.drawLine(x + 3, y + 3, x + 4, y + 4);
                    g2.drawLine(x + 12, y + 12, x + 13, y + 13);
                    g2.drawLine(x + 12, y + 4, x + 13, y + 3);
                    g2.drawLine(x + 3, y + 13, x + 4, y + 12);
                }
                g2.dispose();
            }
        };
        return createMaterialIcon(dark ? "dark_mode" : "light_mode", color, fallback);
    }

    private Icon createTrashIcon() {
        Color color = ICON_DANGER_COLOR;
        Icon fallback = new Icon() {
            @Override
            public int getIconWidth() {
                return 16;
            }

            @Override
            public int getIconHeight() {
                return 16;
            }

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(x + 5, y + 4, x + 11, y + 4);
                g2.drawLine(x + 7, y + 2, x + 9, y + 2);
                g2.drawLine(x + 3, y + 5, x + 13, y + 5);
                g2.drawRect(x + 5, y + 6, 6, 8);
                g2.drawLine(x + 7, y + 8, x + 7, y + 12);
                g2.drawLine(x + 9, y + 8, x + 9, y + 12);
                g2.dispose();
            }
        };
        return createMaterialIcon("delete", color, fallback);
    }

    private void showEmptyAppointmentForm() {
        detailsContentPanel.removeAll();
        detailsContentPanel.add(createAppointmentForm(
            calendar.get(Calendar.DAY_OF_MONTH),
            "",
            "",
            "",
            Calendar_i18n.getString("appointment.other"),
            false
        ), BorderLayout.CENTER);
        detailsContentPanel.revalidate();
        detailsContentPanel.repaint();
    }

    private void addFormRow(JPanel formPanel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        formPanel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(field, gbc);
    }

    private void saveNewAppointment(int day, JTextField timeField, JTextField titleField,
            JTextArea descriptionArea, JComboBox<String> typeCombo) {
        String time = timeField.getText().trim();
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String type = (String) typeCombo.getSelectedItem();

        if (time.isEmpty() || title.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                Calendar_i18n.getString("form.required_fields"),
                Calendar_i18n.getString("info.title"),
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (description.isEmpty()) {
            description = title;
        }

        CalendarAppointment appointment = new CalendarAppointment(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            day,
            time,
            title,
            description,
            type
        );
        customAppointments.add(appointment);
        saveAppointmentsToDisk();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        updateAppointmentPanel();
        showAppointmentDetails(appointment);
    }

    private void updateAppointment(CalendarAppointment appointment, int day, JTextField timeField, JTextField titleField,
            JTextArea descriptionArea, JComboBox<String> typeCombo) {
        String time = timeField.getText().trim();
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String type = (String) typeCombo.getSelectedItem();

        if (time.isEmpty() || title.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                Calendar_i18n.getString("form.required_fields"),
                Calendar_i18n.getString("info.title"),
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (description.isEmpty()) {
            description = title;
        }

        CalendarAppointment updatedAppointment = new CalendarAppointment(
            appointment.year,
            appointment.month,
            day,
            time,
            title,
            description,
            type
        );

        int index = customAppointments.indexOf(appointment);
        if (index >= 0) {
            customAppointments.set(index, updatedAppointment);
        }

        saveAppointmentsToDisk();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        updateAppointmentPanel();
        showAppointmentDetails(updatedAppointment);
    }

    private void deleteAppointment(CalendarAppointment appointment) {
        String appointmentDate = appointment.day + " "
            + Month.fromCalendarMonth(appointment.month).getDisplayName() + " "
            + appointment.year;
        int choice = JOptionPane.showConfirmDialog(this,
            MessageFormat.format(Calendar_i18n.getString("confirm.delete_appointment"),
                appointmentDate, appointment.time),
            Calendar_i18n.getString("button.delete"),
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        customAppointments.remove(appointment);
        saveAppointmentsToDisk();
        updateAppointmentPanel();
        showEmptyAppointmentForm();
    }

    private void moveAppointmentToDate(CalendarAppointment appointment, int targetYear, int targetMonth, int targetDay) {
        if (appointment.year == targetYear && appointment.month == targetMonth && appointment.day == targetDay) {
            return;
        }

        CalendarAppointment movedAppointment = new CalendarAppointment(
            targetYear,
            targetMonth,
            targetDay,
            appointment.time,
            appointment.title,
            appointment.description,
            appointment.type
        );

        int index = customAppointments.indexOf(appointment);
        if (index < 0) {
            return;
        }

        customAppointments.set(index, movedAppointment);
        saveAppointmentsToDisk();
        calendar.set(Calendar.YEAR, targetYear);
        calendar.set(Calendar.MONTH, targetMonth);
        calendar.set(Calendar.DAY_OF_MONTH, targetDay);
        miniCalendarPanel.updateDisplay();
        updateAppointmentPanel();
        showAppointmentDetails(movedAppointment);
    }

    private void duplicateAppointmentToDate(CalendarAppointment appointment, int targetYear, int targetMonth, int targetDay) {
        CalendarAppointment duplicatedAppointment = new CalendarAppointment(
            targetYear,
            targetMonth,
            targetDay,
            appointment.time,
            appointment.title,
            appointment.description,
            appointment.type
        );

        customAppointments.add(duplicatedAppointment);
        saveAppointmentsToDisk();
        calendar.set(Calendar.YEAR, targetYear);
        calendar.set(Calendar.MONTH, targetMonth);
        calendar.set(Calendar.DAY_OF_MONTH, targetDay);
        miniCalendarPanel.updateDisplay();
        updateAppointmentPanel();
        showAppointmentDetails(duplicatedAppointment);
    }

    private void importAppointmentsFromIcs() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(Calendar_i18n.getString("dialog.import_title"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("iCalendar (*.ics)", "ics"));

        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        Path selectedFile = fileChooser.getSelectedFile().toPath();
        if (!selectedFile.getFileName().toString().toLowerCase().endsWith(".ics")) {
            JOptionPane.showMessageDialog(this,
                Calendar_i18n.getString("error.import_extension"),
                Calendar_i18n.getString("dialog.import_title"),
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<CalendarAppointment> importedAppointments = readAppointmentsFromIcs(selectedFile);
            int importedCount = 0;
            int skippedCount = 0;
            CalendarAppointment firstImportedAppointment = null;

            for (CalendarAppointment appointment : importedAppointments) {
                if (containsAppointment(appointment)) {
                    skippedCount++;
                } else {
                    customAppointments.add(appointment);
                    if (firstImportedAppointment == null) {
                        firstImportedAppointment = appointment;
                    }
                    importedCount++;
                }
            }

            if (firstImportedAppointment != null) {
                saveAppointmentsToDisk();
                calendar.set(Calendar.YEAR, firstImportedAppointment.year);
                calendar.set(Calendar.MONTH, firstImportedAppointment.month);
                calendar.set(Calendar.DAY_OF_MONTH, firstImportedAppointment.day);
                miniCalendarPanel.updateDisplay();
                updateAppointmentPanel();
                showAppointmentDetails(firstImportedAppointment);
            }

            JOptionPane.showMessageDialog(this,
                MessageFormat.format(Calendar_i18n.getString("dialog.import_success"), importedCount, skippedCount),
                Calendar_i18n.getString("dialog.import_title"),
                JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                MessageFormat.format(Calendar_i18n.getString("error.import"), e.getMessage()),
                Calendar_i18n.getString("dialog.import_title"),
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void exportAppointmentsToIcs() {
        if (customAppointments.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                Calendar_i18n.getString("dialog.export_empty"),
                Calendar_i18n.getString("dialog.export_title"),
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(Calendar_i18n.getString("dialog.export_title"));
        fileChooser.setSelectedFile(new java.io.File("calenDaros.ics"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("iCalendar (*.ics)", "ics"));

        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        Path exportFile = ensureIcsExtension(fileChooser.getSelectedFile().toPath());
        if (Files.exists(exportFile)) {
            int choice = JOptionPane.showConfirmDialog(this,
                Calendar_i18n.getString("confirm.overwrite_file"),
                Calendar_i18n.getString("dialog.export_title"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            if (choice != JOptionPane.YES_OPTION) {
                return;
            }
        }

        try {
            Files.write(exportFile, createIcsContent().getBytes(StandardCharsets.UTF_8));
            JOptionPane.showMessageDialog(this,
                MessageFormat.format(Calendar_i18n.getString("dialog.export_success"), customAppointments.size()),
                Calendar_i18n.getString("dialog.export_title"),
                JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                MessageFormat.format(Calendar_i18n.getString("error.export"), e.getMessage()),
                Calendar_i18n.getString("dialog.export_title"),
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private Path ensureIcsExtension(Path exportFile) {
        String fileName = exportFile.getFileName().toString();
        if (fileName.toLowerCase().endsWith(".ics")) {
            return exportFile;
        }
        return exportFile.resolveSibling(fileName + ".ics");
    }

    private String createIcsContent() {
        StringBuilder ics = new StringBuilder();
        String lineSeparator = "\r\n";

        ics.append("BEGIN:VCALENDAR").append(lineSeparator);
        ics.append("VERSION:2.0").append(lineSeparator);
        ics.append("PRODID:-//calenDaros//Calendar Export//IT").append(lineSeparator);
        ics.append("CALSCALE:GREGORIAN").append(lineSeparator);
        ics.append("METHOD:PUBLISH").append(lineSeparator);

        String nowUtc = ZonedDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"));
        for (CalendarAppointment appointment : customAppointments) {
            appendIcsEvent(ics, appointment, nowUtc, lineSeparator);
        }

        ics.append("END:VCALENDAR").append(lineSeparator);
        return ics.toString();
    }

    private void appendIcsEvent(StringBuilder ics, CalendarAppointment appointment, String nowUtc, String lineSeparator) {
        ZonedDateTime startDateTime = createAppointmentDateTime(appointment);
        ZonedDateTime endDateTime = startDateTime.plusHours(1);
        DateTimeFormatter utcFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");
        String uid = UUID.nameUUIDFromBytes((
            appointment.year + "|" + appointment.month + "|" + appointment.day + "|"
                + appointment.time + "|" + appointment.title + "|" + appointment.description
        ).getBytes(StandardCharsets.UTF_8)).toString() + "@calendarios";

        ics.append("BEGIN:VEVENT").append(lineSeparator);
        appendIcsLine(ics, "UID:" + uid, lineSeparator);
        appendIcsLine(ics, "DTSTAMP:" + nowUtc, lineSeparator);
        appendIcsLine(ics, "DTSTART:" + startDateTime.withZoneSameInstant(ZoneId.of("UTC")).format(utcFormatter), lineSeparator);
        appendIcsLine(ics, "DTEND:" + endDateTime.withZoneSameInstant(ZoneId.of("UTC")).format(utcFormatter), lineSeparator);
        appendIcsLine(ics, "SUMMARY:" + escapeIcsText(appointment.title), lineSeparator);
        appendIcsLine(ics, "DESCRIPTION:" + escapeIcsText(appointment.description), lineSeparator);
        ics.append("END:VEVENT").append(lineSeparator);
    }

    private ZonedDateTime createAppointmentDateTime(CalendarAppointment appointment) {
        int hour = 0;
        int minute = 0;
        String[] timeParts = appointment.time.split(":", 2);
        try {
            hour = Integer.parseInt(timeParts[0]);
            if (timeParts.length > 1) {
                minute = Integer.parseInt(timeParts[1]);
            }
        } catch (NumberFormatException e) {
            hour = 0;
            minute = 0;
        }

        LocalDateTime localDateTime = LocalDateTime.of(
            appointment.year,
            appointment.month + 1,
            appointment.day,
            Math.max(0, Math.min(23, hour)),
            Math.max(0, Math.min(59, minute))
        );
        return localDateTime.atZone(ZoneId.systemDefault());
    }

    private void appendIcsLine(StringBuilder ics, String line, String lineSeparator) {
        int maxLineLength = 73;
        while (line.length() > maxLineLength) {
            ics.append(line, 0, maxLineLength).append(lineSeparator);
            line = " " + line.substring(maxLineLength);
        }
        ics.append(line).append(lineSeparator);
    }

    private String escapeIcsText(String text) {
        return (text == null ? "" : text)
            .replace("\\", "\\\\")
            .replace(";", "\\;")
            .replace(",", "\\,")
            .replace("\r\n", "\\n")
            .replace("\n", "\\n")
            .replace("\r", "\\n");
    }

    private List<CalendarAppointment> readAppointmentsFromIcs(Path icsFile) throws IOException {
        List<String> lines = unfoldIcsLines(Files.readAllLines(icsFile, StandardCharsets.UTF_8));
        List<CalendarAppointment> appointments = new ArrayList<>();
        Map<String, String> event = null;

        for (String line : lines) {
            if ("BEGIN:VEVENT".equals(line)) {
                event = new HashMap<>();
            } else if ("END:VEVENT".equals(line)) {
                CalendarAppointment appointment = createAppointmentFromIcsEvent(event);
                if (appointment != null) {
                    appointments.add(appointment);
                }
                event = null;
            } else if (event != null) {
                int separatorIndex = line.indexOf(':');
                if (separatorIndex > 0) {
                    String property = line.substring(0, separatorIndex);
                    String value = line.substring(separatorIndex + 1);
                    String key = property.split(";", 2)[0].toUpperCase();
                    event.put(key, value);
                    event.put(key + ".PROPERTY", property);
                }
            }
        }

        return appointments;
    }

    private List<String> unfoldIcsLines(List<String> lines) {
        List<String> unfoldedLines = new ArrayList<>();

        for (String line : lines) {
            if ((line.startsWith(" ") || line.startsWith("\t")) && !unfoldedLines.isEmpty()) {
                int lastIndex = unfoldedLines.size() - 1;
                unfoldedLines.set(lastIndex, unfoldedLines.get(lastIndex) + line.substring(1));
            } else {
                unfoldedLines.add(line);
            }
        }

        return unfoldedLines;
    }

    private CalendarAppointment createAppointmentFromIcsEvent(Map<String, String> event) {
        if (event == null || "CANCELLED".equalsIgnoreCase(event.get("STATUS"))) {
            return null;
        }

        String startValue = event.get("DTSTART");
        String summary = unescapeIcsText(event.getOrDefault("SUMMARY", "")).trim();
        if (startValue == null || summary.isEmpty()) {
            return null;
        }

        Calendar startDate = parseIcsStartDate(event.getOrDefault("DTSTART.PROPERTY", "DTSTART"), startValue);
        if (startDate == null) {
            return null;
        }

        String description = unescapeIcsText(event.getOrDefault("DESCRIPTION", "")).trim();
        if (description.isEmpty()) {
            description = summary;
        }

        return new CalendarAppointment(
            startDate.get(Calendar.YEAR),
            startDate.get(Calendar.MONTH),
            startDate.get(Calendar.DAY_OF_MONTH),
            String.format("%02d:%02d", startDate.get(Calendar.HOUR_OF_DAY), startDate.get(Calendar.MINUTE)),
            summary,
            description,
            Calendar_i18n.getString("appointment.other")
        );
    }

    private Calendar parseIcsStartDate(String property, String value) {
        try {
            ZoneId localZone = ZoneId.systemDefault();
            ZonedDateTime dateTime;

            if (property.toUpperCase().contains("VALUE=DATE")) {
                LocalDate date = LocalDate.parse(value, DateTimeFormatter.BASIC_ISO_DATE);
                dateTime = date.atStartOfDay(localZone);
            } else if (value.endsWith("Z")) {
                LocalDateTime utcDateTime = LocalDateTime.parse(
                    value.substring(0, value.length() - 1),
                    DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")
                );
                dateTime = utcDateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(localZone);
            } else {
                ZoneId eventZone = extractIcsZone(property, localZone);
                LocalDateTime localDateTime = LocalDateTime.parse(
                    value,
                    DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")
                );
                dateTime = localDateTime.atZone(eventZone).withZoneSameInstant(localZone);
            }

            Calendar parsedDate = Calendar.getInstance();
            parsedDate.setTimeInMillis(dateTime.toInstant().toEpochMilli());
            return parsedDate;
        } catch (RuntimeException e) {
            return null;
        }
    }

    private ZoneId extractIcsZone(String property, ZoneId fallbackZone) {
        String marker = "TZID=";
        int zoneStart = property.toUpperCase().indexOf(marker);
        if (zoneStart < 0) {
            return fallbackZone;
        }

        int valueStart = zoneStart + marker.length();
        int valueEnd = property.indexOf(';', valueStart);
        String zoneId = valueEnd >= 0 ? property.substring(valueStart, valueEnd) : property.substring(valueStart);

        try {
            return ZoneId.of(zoneId);
        } catch (RuntimeException e) {
            return fallbackZone;
        }
    }

    private String unescapeIcsText(String text) {
        StringBuilder unescapedText = new StringBuilder();
        boolean escaping = false;

        for (int i = 0; i < text.length(); i++) {
            char current = text.charAt(i);
            if (escaping) {
                if (current == 'n' || current == 'N') {
                    unescapedText.append('\n');
                } else {
                    unescapedText.append(current);
                }
                escaping = false;
            } else if (current == '\\') {
                escaping = true;
            } else {
                unescapedText.append(current);
            }
        }

        if (escaping) {
            unescapedText.append('\\');
        }

        return unescapedText.toString();
    }

    private boolean containsAppointment(CalendarAppointment importedAppointment) {
        for (CalendarAppointment appointment : customAppointments) {
            if (appointment.year == importedAppointment.year
                    && appointment.month == importedAppointment.month
                    && appointment.day == importedAppointment.day
                    && appointment.time.equals(importedAppointment.time)
                    && appointment.title.equals(importedAppointment.title)
                    && appointment.description.equals(importedAppointment.description)) {
                return true;
            }
        }
        return false;
    }

    private void loadAppointmentsFromDisk() {
        if (!Files.exists(APPOINTMENTS_FILE)) {
            return;
        }

        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(APPOINTMENTS_FILE)) {
            properties.load(input);
            int count = Integer.parseInt(properties.getProperty("count", "0"));
            customAppointments.clear();

            for (int i = 0; i < count; i++) {
                CalendarAppointment appointment = readAppointment(properties, i);
                if (appointment != null) {
                    customAppointments.add(appointment);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println(MessageFormat.format(
                Calendar_i18n.getString("error.appointments_load"), e.getMessage()));
        }
    }

    private CalendarAppointment readAppointment(Properties properties, int index) {
        String prefix = "appointment." + index + ".";
        try {
            int year = Integer.parseInt(properties.getProperty(prefix + "year"));
            int month = Integer.parseInt(properties.getProperty(prefix + "month"));
            int day = Integer.parseInt(properties.getProperty(prefix + "day"));
            String time = properties.getProperty(prefix + "time", "");
            String title = properties.getProperty(prefix + "title", "");
            String description = properties.getProperty(prefix + "description", title);
            String type = properties.getProperty(prefix + "type", Calendar_i18n.getString("appointment.other"));

            if (time.isEmpty() || title.isEmpty()) {
                return null;
            }

            return new CalendarAppointment(year, month, day, time, title, description, type);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void saveAppointmentsToDisk() {
        Properties properties = new Properties();
        properties.setProperty("count", String.valueOf(customAppointments.size()));

        for (int i = 0; i < customAppointments.size(); i++) {
            CalendarAppointment appointment = customAppointments.get(i);
            String prefix = "appointment." + i + ".";
            properties.setProperty(prefix + "year", String.valueOf(appointment.year));
            properties.setProperty(prefix + "month", String.valueOf(appointment.month));
            properties.setProperty(prefix + "day", String.valueOf(appointment.day));
            properties.setProperty(prefix + "time", appointment.time);
            properties.setProperty(prefix + "title", appointment.title);
            properties.setProperty(prefix + "description", appointment.description);
            properties.setProperty(prefix + "type", appointment.type);
        }

        try {
            Files.createDirectories(APPOINTMENTS_FILE.getParent());
            try (OutputStream output = Files.newOutputStream(APPOINTMENTS_FILE)) {
                properties.store(output, "calenDaros appointments");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                MessageFormat.format(Calendar_i18n.getString("error.appointments_save"), e.getMessage()),
                Calendar_i18n.getString("info.title"),
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void loadUiSettingsFromDisk() {
        if (!Files.exists(UI_SETTINGS_FILE)) {
            return;
        }

        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(UI_SETTINGS_FILE)) {
            properties.load(input);
            int width = Integer.parseInt(properties.getProperty("extended.width", String.valueOf(extendedWindowSize.width)));
            int height = Integer.parseInt(properties.getProperty("extended.height", String.valueOf(extendedWindowSize.height)));
            if (width >= 640 && height >= 480) {
                extendedWindowSize = new Dimension(width, height);
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Impossibile caricare le preferenze UI: " + e.getMessage());
        }
    }

    private void saveUiSettingsToDisk() {
        Properties properties = new Properties();
        properties.setProperty("extended.width", String.valueOf(extendedWindowSize.width));
        properties.setProperty("extended.height", String.valueOf(extendedWindowSize.height));

        try {
            Files.createDirectories(UI_SETTINGS_FILE.getParent());
            try (OutputStream output = Files.newOutputStream(UI_SETTINGS_FILE)) {
                properties.store(output, "calenDaros UI settings");
            }
        } catch (IOException e) {
            System.err.println("Impossibile salvare le preferenze UI: " + e.getMessage());
        }
    }
    
    /**
     * Toggles between compact and extended view modes
     */
    private void toggleLanguage() {
        Calendar_i18n.toggleLanguage();
        setTitle(Calendar_i18n.getString("app.title"));
        todayButton.setToolTipText(Calendar_i18n.getString("button.today"));
        todayButton.getAccessibleContext().setAccessibleName(Calendar_i18n.getString("button.today"));
        populateControlsPanel();
        miniCalendarPanel.updateDisplay();
        updateAppointmentPanel();
        showEmptyAppointmentForm();
    }

    private void toggleAlwaysOnTopMode() {
        alwaysOnTopMode = !alwaysOnTopMode;
        setAlwaysOnTop(alwaysOnTopMode);
        populateControlsPanel();
    }

    private void toggleThemeMode() {
        darkMode = !darkMode;
        setupModernLookAndFeel(darkMode);
        configureTooltips();
        SwingUtilities.updateComponentTreeUI(this);
        populateControlsPanel();
        applyTheme();
        miniCalendarPanel.updateDisplay();
        updateAppointmentPanel();
        showEmptyAppointmentForm();
    }

    private void toggleViewMode() {
        boolean wasCompactMode = compactMode;
        if (!wasCompactMode) {
            rememberExtendedWindowSize();
        }

        compactMode = !compactMode;
        populateControlsPanel();
        updateLeftPanelSize();
        
        if (compactMode) {
            // Switch to compact mode - show only mini calendar
            miniCalendarPanel.setVisible(true);
            mainPanel.setVisible(false);
            // Resize the window to fit only the left panel
            setSize(getCompactWindowWidth(), 400);
        } else {
            // Switch back to extended mode
            miniCalendarPanel.setVisible(false);
            mainPanel.setVisible(true);
            setSize(extendedWindowSize);
        }
        
        // Update the UI
        applyTheme();
        miniCalendarPanel.updateDisplay();
        revalidate();
        repaint();
        if (!compactMode) {
            scrollTodayIntoViewIfCurrentMonth();
        }
    }

    private void scrollTodayIntoViewIfCurrentMonth() {
        if (compactMode || appointmentScrollPane == null || todayDayPanel == null || !isViewingCurrentMonth()) {
            return;
        }

        SwingUtilities.invokeLater(() -> {
            if (appointmentScrollPane == null || todayDayPanel == null || !todayDayPanel.isShowing()) {
                return;
            }

            Rectangle todayBounds = SwingUtilities.convertRectangle(
                todayDayPanel.getParent(),
                todayDayPanel.getBounds(),
                appointmentPanel
            );
            JViewport viewport = appointmentScrollPane.getViewport();
            int centeredX = todayBounds.x - Math.max(0, (viewport.getWidth() - todayBounds.width) / 2);
            int centeredY = todayBounds.y - Math.max(0, (viewport.getHeight() - todayBounds.height) / 2);
            JScrollBar horizontalBar = appointmentScrollPane.getHorizontalScrollBar();
            JScrollBar verticalBar = appointmentScrollPane.getVerticalScrollBar();
            int maxHorizontalValue = Math.max(
                horizontalBar.getMinimum(),
                horizontalBar.getMaximum() - horizontalBar.getVisibleAmount()
            );
            int maxVerticalValue = Math.max(
                verticalBar.getMinimum(),
                verticalBar.getMaximum() - verticalBar.getVisibleAmount()
            );
            horizontalBar.setValue(Math.max(horizontalBar.getMinimum(), Math.min(centeredX, maxHorizontalValue)));
            verticalBar.setValue(Math.max(verticalBar.getMinimum(), Math.min(centeredY, maxVerticalValue)));
        });
    }

    private boolean isViewingCurrentMonth() {
        Calendar today = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)
            && calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH);
    }

    private void rememberExtendedWindowSize() {
        Dimension currentSize = getSize();
        if (currentSize.width > getCompactWindowWidth()
                && currentSize.height > 0
                && !currentSize.equals(extendedWindowSize)) {
            extendedWindowSize = new Dimension(currentSize);
            saveUiSettingsToDisk();
        }
    }

    private int getCompactWindowWidth() {
        Insets insets = getInsets();
        return LEFT_COMPACT_SIZE.width + insets.left + insets.right;
    }
    
    /**
     * Restituisce lo stato della modalità compatta
     * @return true se il calendario è in modalità compatta, false altrimenti
     */
    public boolean isCompactMode() {
        return compactMode;
    }

    public boolean isDarkMode() {
        return darkMode;
    }
    
    /**
     * Mostra gli appuntamenti del giorno selezionato in una finestra di dialogo
     * quando il calendario è in modalità compatta.
     */
    public void showAppointmentsInDialog() {
        // Crea una nuova finestra di dialogo
        JDialog appointmentsDialog = new JDialog(this, dashboard.i18n.Calendar_i18n.getString("dialog.appointments_title"), false);
        appointmentsDialog.setLayout(new BorderLayout(10, 10));
        appointmentsDialog.setSize(400, 300);
        appointmentsDialog.setLocationRelativeTo(this);
        
        // Crea un pannello per il titolo
        JPanel titlePanel = new JPanel(new BorderLayout());
        String dateText = calendar.get(Calendar.DAY_OF_MONTH) + " " + 
                        Month.fromCalendarMonth(calendar.get(Calendar.MONTH)).getDisplayName() + " " + 
                        calendar.get(Calendar.YEAR);
        JLabel titleLabel = new JLabel(Calendar_i18n.getString("dialog.appointments_for") + " " + dateText);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        // Crea un pannello per gli appuntamenti
        JPanel appointmentsPanel = new JPanel();
        appointmentsPanel.setLayout(new BoxLayout(appointmentsPanel, BoxLayout.Y_AXIS));
        appointmentsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Aggiungi gli appuntamenti del giorno selezionato
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        boolean hasAppointments = false;
        List<CalendarAppointment> appointmentsForDay = new ArrayList<>();
        
        for (CalendarAppointment appointment : customAppointments) {
            if (appointment.year == calendar.get(Calendar.YEAR) &&
                    appointment.month == calendar.get(Calendar.MONTH) &&
                    appointment.day == day &&
                    isAppointmentTypeVisible(appointment.type)) {
                appointmentsForDay.add(appointment);
            }
        }
        appointmentsForDay.sort(Comparator.comparingInt(appointment -> parseTimeToMinutes(appointment.time)));
        for (CalendarAppointment appointment : appointmentsForDay) {
            addAppointmentToPanel(appointmentsPanel, appointment.time,
                appointment.title, getAppointmentColor(appointment.type));
            hasAppointments = true;
        }
        
        // Se non ci sono appuntamenti, mostra un messaggio
        if (!hasAppointments) {
            JLabel noAppointmentsLabel = new JLabel(Calendar_i18n.getString("dialog.no_appointments"));
            noAppointmentsLabel.setFont(new Font("Arial", Font.ITALIC, 12));
            appointmentsPanel.add(noAppointmentsLabel);
        }
        
        // Crea un pannello per i pulsanti
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton(dashboard.i18n.Calendar_i18n.getString("button.close"));
        closeButton.addActionListener(e -> appointmentsDialog.dispose());
        buttonPanel.add(closeButton);
        
        // Aggiungi i pannelli alla finestra di dialogo
        appointmentsDialog.add(titlePanel, BorderLayout.NORTH);
        appointmentsDialog.add(new JScrollPane(appointmentsPanel), BorderLayout.CENTER);
        appointmentsDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Mostra la finestra di dialogo
        appointmentsDialog.setVisible(true);
    }
    
    /**
     * Aggiunge un appuntamento al pannello degli appuntamenti nella finestra di dialogo.
     */
    private void addAppointmentToPanel(JPanel panel, String time, String description, Color color) {
        JPanel appointmentPanel = new JPanel(new BorderLayout(5, 0));
        appointmentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 3, 0, 0, color),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        appointmentPanel.add(timeLabel, BorderLayout.WEST);
        appointmentPanel.add(descriptionLabel, BorderLayout.CENTER);
        
        panel.add(appointmentPanel);
        panel.add(Box.createVerticalStrut(5)); // Spazio tra gli appuntamenti
    }

    private void createControlsPanel() {
        // Create controls panel with vertical box layout
        controlsPanel = new JPanel();
        markSidebarComponent(controlsPanel);
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
        controlsPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
        controlsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        populateControlsPanel();
    }

    private void populateControlsPanel() {
        boolean meetingsSelected = meetingsCheckbox == null || meetingsCheckbox.isSelected();
        boolean lunchSelected = lunchCheckbox == null || lunchCheckbox.isSelected();
        boolean conferenceSelected = conferenceCheckbox == null || conferenceCheckbox.isSelected();
        boolean courseSelected = courseCheckbox == null || courseCheckbox.isSelected();
        boolean otherSelected = otherCheckbox == null || otherCheckbox.isSelected();

        controlsPanel.removeAll();

        // Create filter section
        JPanel filterPanel = new JPanel(new BorderLayout(5, 0));
        markSidebarComponent(filterPanel);
        alignSidebarComponent(filterPanel);
        JLabel filterLabel = new JLabel(Calendar_i18n.getString("label.filters"));
        filterLabel.setFont(new Font("Arial", Font.BOLD, 12));
        filterLabel.setForeground(getTextColor());
        
        JPanel checkboxPanel = new JPanel();
        markSidebarComponent(checkboxPanel);
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        alignSidebarComponent(checkboxPanel);
        
        // Add checkboxes for different appointment types
        meetingsCheckbox = new JCheckBox(Calendar_i18n.getString("appointment.meeting"));
        lunchCheckbox = new JCheckBox(Calendar_i18n.getString("appointment.lunch"));
        conferenceCheckbox = new JCheckBox(Calendar_i18n.getString("appointment.conference"));
        courseCheckbox = new JCheckBox(Calendar_i18n.getString("appointment.course"));
        otherCheckbox = new JCheckBox(Calendar_i18n.getString("appointment.other"));
        markSidebarComponent(meetingsCheckbox);
        markSidebarComponent(lunchCheckbox);
        markSidebarComponent(conferenceCheckbox);
        markSidebarComponent(courseCheckbox);
        markSidebarComponent(otherCheckbox);
        styleFilterCheckbox(meetingsCheckbox, MEETING_COLOR_BG);
        styleFilterCheckbox(lunchCheckbox, LUNCH_COLOR_BG);
        styleFilterCheckbox(conferenceCheckbox, CONFERENCE_COLOR_BG);
        styleFilterCheckbox(courseCheckbox, COURSE_COLOR_BG);
        styleFilterCheckbox(otherCheckbox, OTHER_COLOR_BG);
        
        // Set all checkboxes selected by default
        meetingsCheckbox.setSelected(meetingsSelected);
        lunchCheckbox.setSelected(lunchSelected);
        conferenceCheckbox.setSelected(conferenceSelected);
        courseCheckbox.setSelected(courseSelected);
        otherCheckbox.setSelected(otherSelected);
        
        // Add action listeners to checkboxes
        ActionListener filterListener = e -> {
            updateAppointmentPanel();
            miniCalendarPanel.updateDisplay();
        };
        meetingsCheckbox.addActionListener(filterListener);
        lunchCheckbox.addActionListener(filterListener);
        conferenceCheckbox.addActionListener(filterListener);
        courseCheckbox.addActionListener(filterListener);
        otherCheckbox.addActionListener(filterListener);
        
        // Add checkboxes to panel
        checkboxPanel.add(meetingsCheckbox);
        checkboxPanel.add(lunchCheckbox);
        checkboxPanel.add(conferenceCheckbox);
        checkboxPanel.add(courseCheckbox);
        checkboxPanel.add(otherCheckbox);
        
        // Create buttons panel
        JPanel buttonsPanel = createToolbarButtonPanel(10);
        markSidebarComponent(buttonsPanel);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        alignSidebarComponent(buttonsPanel);

        JButton languageButton = createStyledButton("", getControlBackground());
        languageButton.setIcon(createLanguageIcon(!Calendar_i18n.isCurrentLanguageItalian()));
        languageButton.setToolTipText(Calendar_i18n.getString("button.language"));
        languageButton.getAccessibleContext().setAccessibleName(Calendar_i18n.getString("button.language"));
        languageButton.addActionListener(e -> toggleLanguage());
        
        // Add toggle view button
        String viewModeText = compactMode ? Calendar_i18n.getString("button.extended_mode") : Calendar_i18n.getString("button.compact_mode");
        JButton toggleViewButton = createStyledButton("", getControlBackground());
        toggleViewButton.setIcon(createViewModeIcon(!compactMode));
        toggleViewButton.setToolTipText(viewModeText);
        toggleViewButton.getAccessibleContext().setAccessibleName(viewModeText);
        toggleViewButton.addActionListener(e -> toggleViewMode());

        String alwaysOnTopText = alwaysOnTopMode
            ? Calendar_i18n.getString("button.always_on_top_on")
            : Calendar_i18n.getString("button.always_on_top_off");
        JButton alwaysOnTopButton = createStyledButton("", getControlBackground());
        alwaysOnTopButton.setIcon(createAlwaysOnTopIcon(!alwaysOnTopMode));
        alwaysOnTopButton.setToolTipText(alwaysOnTopText);
        alwaysOnTopButton.getAccessibleContext().setAccessibleName(alwaysOnTopText);
        alwaysOnTopButton.addActionListener(e -> toggleAlwaysOnTopMode());

        styleNavigationIconButton(languageButton, prevButton.getPreferredSize());
        styleNavigationIconButton(toggleViewButton, todayButton.getPreferredSize());
        styleNavigationIconButton(alwaysOnTopButton, nextButton.getPreferredSize());
        
        buttonsPanel.add(languageButton);
        buttonsPanel.add(toggleViewButton); // Add the toggle button
        buttonsPanel.add(alwaysOnTopButton);

        JPanel importPanel = createToolbarButtonPanel(10, 4);
        markSidebarComponent(importPanel);
        importPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        alignSidebarComponent(importPanel);
        JButton importButton = createStyledButton("", getControlBackground());
        importButton.setIcon(createImportIcon());
        importButton.setToolTipText(Calendar_i18n.getString("button.import_tooltip"));
        importButton.getAccessibleContext().setAccessibleName(Calendar_i18n.getString("button.import"));
        importButton.addActionListener(e -> importAppointmentsFromIcs());
        JButton exportButton = createStyledButton("", getControlBackground());
        exportButton.setIcon(createExportIcon());
        exportButton.setToolTipText(Calendar_i18n.getString("button.export_tooltip"));
        exportButton.getAccessibleContext().setAccessibleName(Calendar_i18n.getString("button.export"));
        exportButton.addActionListener(e -> exportAppointmentsToIcs());
        JButton googleCalendarButton = createStyledButton("", getControlBackground());
        googleCalendarButton.setIcon(createExternalCalendarIcon());
        googleCalendarButton.setToolTipText(Calendar_i18n.getString("button.google_calendar_tooltip"));
        googleCalendarButton.getAccessibleContext().setAccessibleName(Calendar_i18n.getString("button.google_calendar"));
        googleCalendarButton.addActionListener(e -> openExternalLink(GOOGLE_CALENDAR_URL));
        String themeButtonText = darkMode
            ? Calendar_i18n.getString("button.light_theme")
            : Calendar_i18n.getString("button.dark_theme");
        JButton themeButton = createStyledButton("", getControlBackground());
        themeButton.setIcon(createThemeIcon(!darkMode));
        themeButton.setToolTipText(Calendar_i18n.getString("button.theme_tooltip"));
        themeButton.getAccessibleContext().setAccessibleName(themeButtonText);
        themeButton.addActionListener(e -> toggleThemeMode());

        styleNavigationIconButton(importButton, prevButton.getPreferredSize());
        styleNavigationIconButton(exportButton, todayButton.getPreferredSize());
        styleNavigationIconButton(googleCalendarButton, nextButton.getPreferredSize());
        styleNavigationIconButton(themeButton, nextButton.getPreferredSize());

        importPanel.add(importButton);
        importPanel.add(exportButton);
        importPanel.add(googleCalendarButton);
        importPanel.add(themeButton);

        filterPanel.add(filterLabel, BorderLayout.NORTH);
        filterPanel.add(checkboxPanel, BorderLayout.CENTER);
        filterPanel.setVisible(!compactMode);

        updateHelpPanel();
        
        // Add components to controls panel
        alignSidebarComponent(navigationPanel);
        controlsPanel.add(navigationPanel);
        controlsPanel.add(buttonsPanel);
        controlsPanel.add(importPanel);
        controlsPanel.add(filterPanel);
        controlsPanel.revalidate();
        controlsPanel.repaint();
    }

    private void updateHelpPanel() {
        if (helpPanel != null && helpPanel.getParent() != null) {
            helpPanel.getParent().remove(helpPanel);
        }

        helpPanel = createHelpPanel();
        helpPanel.setVisible(!compactMode);

        if (leftPanel != null) {
            leftPanel.add(helpPanel, BorderLayout.SOUTH);
            leftPanel.revalidate();
            leftPanel.repaint();
        }
    }

    private JPanel createHelpPanel() {
        JPanel helpPanel = new JPanel();
        markSidebarComponent(helpPanel);
        helpPanel.setLayout(new BoxLayout(helpPanel, BoxLayout.Y_AXIS));
        helpPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        alignSidebarComponent(helpPanel);

        JLabel helpLabel = new JLabel(Calendar_i18n.getString("label.help"));
        helpLabel.setFont(new Font("Arial", Font.BOLD, 12));
        helpLabel.setForeground(getTextColor());
        helpLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        helpPanel.add(helpLabel);
        helpPanel.add(Box.createVerticalStrut(4));

        addHelpLine(helpPanel, Calendar_i18n.getString("help.new_event"));
        addHelpLine(helpPanel, Calendar_i18n.getString("help.select_event"));
        addHelpLine(helpPanel, Calendar_i18n.getString("help.move_event"));
        addHelpLine(helpPanel, Calendar_i18n.getString("help.copy_event"));
        helpPanel.add(Box.createVerticalStrut(4));
        helpPanel.add(createVersionLink());

        return helpPanel;
    }

    private JLabel createVersionLink() {
        JLabel versionLink = new JLabel("<html><u>CalenDaros v1.0.7</u></html>");
        versionLink.putClientProperty("versionLink", Boolean.TRUE);
        versionLink.setFont(new Font("Arial", Font.BOLD, 11));
        versionLink.setForeground(getLinkColor());
        versionLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        versionLink.setToolTipText(VERSION_PAGE_URL);
        versionLink.setAlignmentX(Component.LEFT_ALIGNMENT);
        versionLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openVersionPage();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                versionLink.setForeground(getLinkColor().brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                versionLink.setForeground(getLinkColor());
            }
        });
        return versionLink;
    }

    private void openVersionPage() {
        openExternalLink(VERSION_PAGE_URL);
    }

    private void openExternalLink(String url) {
        try {
            if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                throw new UnsupportedOperationException("Desktop browse is not supported");
            }
            Desktop.getDesktop().browse(URI.create(url));
        } catch (IOException | RuntimeException e) {
            JOptionPane.showMessageDialog(this,
                MessageFormat.format(Calendar_i18n.getString("error.open_link"), e.getMessage()),
                Calendar_i18n.getString("info.title"),
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void addHelpLine(JPanel helpPanel, String text) {
        JLabel line = new JLabel(formatHelpText(text));
        line.setFont(new Font("Arial", Font.PLAIN, 11));
        line.setForeground(getTextColor());
        line.setOpaque(false);
        line.setBorder(BorderFactory.createEmptyBorder());
        line.setAlignmentX(Component.LEFT_ALIGNMENT);
        line.setMaximumSize(new Dimension(LEFT_SIDEBAR_SIZE.width - 20, Integer.MAX_VALUE));
        helpPanel.add(line);
        helpPanel.add(Box.createVerticalStrut(8));
    }

    private String formatHelpText(String text) {
        String textColor = toHtmlColor(getTextColor());
        int separatorIndex = text.indexOf(':');
        if (separatorIndex < 0) {
            return "<html><body style='width:145px; color:" + textColor + "'><b>" + escapeHtml(text) + "</b></body></html>";
        }

        String command = escapeHtml(text.substring(0, separatorIndex));
        String description = escapeHtml(text.substring(separatorIndex + 1).trim());
        return "<html><body style='width:145px; color:" + textColor + "'><b>" + command + "</b><br>" + description + "</body></html>";
    }

    private String toHtmlColor(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    private String escapeHtml(String text) {
        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;");
    }

    private void styleNavigationIconButton(JButton button) {
        styleNavigationIconButton(button, NAVIGATION_ICON_BUTTON_SIZE);
    }

    private JPanel createToolbarButtonPanel() {
        return createToolbarButtonPanel(0);
    }

    private JPanel createToolbarButtonPanel(int bottomGap) {
        return createToolbarButtonPanel(bottomGap, 3);
    }

    private JPanel createToolbarButtonPanel(int bottomGap, int buttonCount) {
        JPanel panel = new JPanel(new GridLayout(1, buttonCount, 6, 0));
        int width = (NAVIGATION_ICON_BUTTON_SIZE.width * buttonCount) + (6 * Math.max(0, buttonCount - 1));
        Dimension size = new Dimension(width, NAVIGATION_ICON_BUTTON_SIZE.height + bottomGap);
        panel.setPreferredSize(size);
        panel.setMinimumSize(size);
        panel.setMaximumSize(size);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    private void setMonthArrowIcon(JButton button, String iconName, String fallbackText) {
        button.setText("");
        button.setIcon(createMaterialIcon(iconName, MONTH_ARROW_ICON_SIZE, getMonthArrowIconColor(), null));
        if (button.getIcon() == null) {
            button.setText(fallbackText);
        }
    }

    private Color getMonthArrowIconColor() {
        return darkMode ? getControlForeground() : new Color(17, 24, 39);
    }

    private void styleNavigationIconButton(JButton button, Dimension size) {
        Dimension buttonSize = new Dimension(size);
        button.setPreferredSize(buttonSize);
        button.setMinimumSize(buttonSize);
        button.setMaximumSize(buttonSize);
        button.setFocusPainted(false);
    }

    private void alignSidebarComponent(JComponent component) {
        component.setAlignmentX(Component.LEFT_ALIGNMENT);
        component.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    private void markSidebarComponent(JComponent component) {
        component.putClientProperty("sidebarComponent", Boolean.TRUE);
        component.setBackground(getPanelBackground());
        component.setOpaque(true);
    }

    private void styleFilterCheckbox(JCheckBox checkbox) {
        Color filterColor = checkbox instanceof JComponent
            ? (Color) ((JComponent) checkbox).getClientProperty("filterColor")
            : null;
        Color background = filterColor == null ? getPanelBackground() : filterColor;
        checkbox.setBackground(background);
        checkbox.setForeground(filterColor == null ? getTextColor() : getReadableTextColor(background));
        checkbox.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        checkbox.setOpaque(true);
    }

    private void styleFilterCheckbox(JCheckBox checkbox, Color filterColor) {
        checkbox.putClientProperty("filterColor", filterColor);
        styleFilterCheckbox(checkbox);
    }

    private Color getReadableTextColor(Color background) {
        double luminance = (0.299 * background.getRed())
            + (0.587 * background.getGreen())
            + (0.114 * background.getBlue());
        return luminance > 150 ? new Color(31, 41, 55) : Color.WHITE;
    }

    private void applyTheme() {
        getContentPane().setBackground(getAppBackground());
        if (mainPanel != null) {
            mainPanel.setBackground(getAppBackground());
        }
        if (miniCalendarPanel != null) {
            miniCalendarPanel.setBackground(getAppBackground());
        }
        if (monthLabel != null) {
            monthLabel.setForeground(getTextColor());
        }

        applyThemeToComponent(getContentPane());
        updateNavigationButtonTheme(prevButton);
        updateNavigationButtonTheme(todayButton);
        updateNavigationButtonTheme(nextButton);
        if (prevButton != null) {
            setMonthArrowIcon(prevButton, "chevron_left", "<<");
        }
        if (todayButton != null) {
            todayButton.setIcon(createTodayIcon());
        }
        if (nextButton != null) {
            setMonthArrowIcon(nextButton, "chevron_right", ">>");
        }
        if (leftPanel != null) {
            leftPanel.setBackground(getPanelBackground());
            leftPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, getBorderColor()),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        }
        if (controlsPanel != null) {
            controlsPanel.setBackground(getPanelBackground());
        }
        if (miniCalendarPanel != null) {
            miniCalendarPanel.setBackground(getAppBackground());
        }
        configureTooltips();
        revalidate();
        repaint();
    }

    private void updateNavigationButtonTheme(JButton button) {
        if (button == null) {
            return;
        }

        button.setBackground(getControlBackground());
        button.setForeground(getControlForeground());
        updateButtonFocusBorder(button);
    }

    private void applyThemeToComponent(Component component) {
        boolean appointmentComponent = component instanceof JComponent
            && Boolean.TRUE.equals(((JComponent) component).getClientProperty("appointmentComponent"));

        if (component instanceof JPanel || component instanceof JScrollPane || component instanceof JViewport) {
            if (!appointmentComponent) {
                component.setBackground(component instanceof JComponent
                    && Boolean.TRUE.equals(((JComponent) component).getClientProperty("sidebarComponent"))
                        ? getPanelBackground()
                        : getAppBackground());
            }
        }
        if (component instanceof JLabel && !appointmentComponent) {
            component.setForeground(Boolean.TRUE.equals(((JLabel) component).getClientProperty("versionLink"))
                ? getLinkColor()
                : getTextColor());
        }
        if (component instanceof JCheckBox) {
            if (component instanceof JComponent
                    && Boolean.TRUE.equals(((JComponent) component).getClientProperty("sidebarComponent"))) {
                styleFilterCheckbox((JCheckBox) component);
            } else {
                component.setBackground(getAppBackground());
                component.setForeground(getTextColor());
            }
        }
        if (component instanceof JTextField || component instanceof JTextArea || component instanceof JComboBox) {
            component.setBackground(darkMode ? new Color(39, 43, 53) : Color.WHITE);
            component.setForeground(getTextColor());
        }
        if (component instanceof JButton) {
            component.setBackground(getControlBackground());
            component.setForeground(getControlForeground());
            updateButtonFocusBorder((JButton) component);
        }

        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                applyThemeToComponent(child);
            }
        }
    }

    private void createAppointmentPanel() {
        appointmentPanel = new JPanel(new BorderLayout(0, 1));
        appointmentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        appointmentPanel.setOpaque(false);

        dayOfWeekHeaderPanel = new JPanel(new GridLayout(1, 7, 1, 0));
        dayOfWeekHeaderPanel.setOpaque(false);
        dayOfWeekHeaderPanel.setPreferredSize(new Dimension(0, 26));
        dayOfWeekHeaderPanel.setMinimumSize(new Dimension(0, 26));
        updateDayOfWeekHeaderPanel();

        appointmentGridPanel = new JPanel(new GridLayout(6, 7, 1, 1));
        appointmentGridPanel.setOpaque(false);
        appointmentPanel.add(dayOfWeekHeaderPanel, BorderLayout.NORTH);
        appointmentPanel.add(appointmentGridPanel, BorderLayout.CENTER);
        
        // Create a scroll pane for the appointment panel
        appointmentScrollPane = new JScrollPane(appointmentPanel);
        appointmentScrollPane.setBorder(BorderFactory.createEmptyBorder());
        appointmentScrollPane.setOpaque(false);
        appointmentScrollPane.getViewport().setOpaque(false);
        appointmentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        appointmentScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        appointmentScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Add the scroll pane to the main panel
        mainPanel.add(appointmentScrollPane, BorderLayout.CENTER);
        
        // Initial update of the appointment panel
        updateAppointmentPanel();
    }

    private void updateDayOfWeekHeaderPanel() {
        if (dayOfWeekHeaderPanel == null) {
            return;
        }

        dayOfWeekHeaderPanel.removeAll();
        for (DayOfWeek day : DayOfWeek.values()) {
            JLabel label = new JLabel(day.getDisplayName(), SwingConstants.CENTER);
            label.setBorder(createDayOfWeekHeaderBorder());
            label.setFont(new Font("Arial", Font.BOLD, 12));
            label.setForeground(day.isWeekend() ? getWeekendTextColor(true) : getTextColor());
            label.setBackground(getHeaderBackground());
            label.setOpaque(true);
            dayOfWeekHeaderPanel.add(label);
        }
        dayOfWeekHeaderPanel.revalidate();
        dayOfWeekHeaderPanel.repaint();
    }

    private void createDetailsPanel() {
        // Create details panel for showing appointment information
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(10, 0, 0, 0)
        ));
        
        // Create text area for appointment details
        appointmentDetails = new JTextArea(5, 20);
        appointmentDetails.setEditable(false);
        appointmentDetails.setFont(new Font("Arial", Font.PLAIN, 12));
        appointmentDetails.setLineWrap(true);
        appointmentDetails.setWrapStyleWord(true);
        appointmentDetails.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        appointmentDetails.setText(Calendar_i18n.getString("label.select_appointment"));
        
        detailsContentPanel = new JPanel(new BorderLayout());
        detailsContentPanel.setPreferredSize(DETAILS_PANEL_SIZE);
        detailsContentPanel.setMinimumSize(DETAILS_PANEL_SIZE);
        detailsContentPanel.add(createAppointmentForm(
            calendar.get(Calendar.DAY_OF_MONTH),
            "",
            "",
            "",
            Calendar_i18n.getString("appointment.other"),
            false
        ), BorderLayout.CENTER);

        // Add components to details panel
        detailsPanel.add(detailsContentPanel, BorderLayout.CENTER);
        
        // Add details panel to main panel
        mainPanel.add(detailsPanel, BorderLayout.SOUTH);
    }

    private static void configureTooltips() {
        ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
        toolTipManager.setInitialDelay(150);
        toolTipManager.setReshowDelay(50);
        toolTipManager.setDismissDelay(10000);

        Color tooltipBackground = new Color(255, 255, 248);
        Color tooltipForeground = new Color(31, 41, 55);
        Color tooltipBorder = NAVIGATION_BUTTON_COLOR_BD;

        UIManager.put("ToolTip.background", tooltipBackground);
        UIManager.put("ToolTip.foreground", tooltipForeground);
        UIManager.put("ToolTip.font", new Font("Arial", Font.PLAIN, 12));
        UIManager.put("ToolTip.border", BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(tooltipBorder, 1, true),
            BorderFactory.createEmptyBorder(7, 9, 7, 9)
        ));
        UIManager.put("ToolTip[Enabled].backgroundPainter", (Painter<JComponent>) (g, c, w, h) -> {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(tooltipBackground);
            g2.fillRoundRect(0, 0, w - 1, h - 1, 10, 10);
            g2.setColor(tooltipBorder);
            g2.drawRoundRect(0, 0, w - 1, h - 1, 10, 10);
            g2.dispose();
        });
    }

    private static void setupModernLookAndFeel(boolean darkMode) {
        configureFlatLafDefaults();

        try {
            String lafClassName = darkMode
                ? "com.formdev.flatlaf.FlatDarkLaf"
                : "com.formdev.flatlaf.FlatLightLaf";
            Class<?> lafClass = Class.forName(lafClassName);
            Method setupMethod = lafClass.getMethod("setup");
            setupMethod.invoke(null);
            return;
        } catch (ReflectiveOperationException | LinkageError e) {
            System.err.println("FlatLaf non disponibile, uso il Look & Feel di sistema: " + e.getMessage());
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Impossibile impostare il Look & Feel di sistema: " + e.getMessage());
        }
    }

    private static void configureFlatLafDefaults() {
        System.setProperty("flatlaf.useWindowDecorations", "true");
        UIManager.put("Component.arc", 10);
        UIManager.put("Button.arc", 10);
        UIManager.put("CheckBox.arc", 6);
        UIManager.put("TextComponent.arc", 8);
        UIManager.put("ScrollBar.showButtons", false);
        UIManager.put("ScrollBar.width", 12);
        UIManager.put("TabbedPane.showTabSeparators", true);
    }

    /**
     * Metodo principale che avvia l'applicazione Calendario.
     * Imposta il look and feel FlatLaf e crea l'istanza del calendario.
     * 
     * @param args argomenti da linea di comando
     */
    public static void main(String[] args) {
        boolean startAlwaysOnTop = shouldStartAlwaysOnTop(args);
        StartupPosition startupPosition = getStartupPosition(args);
        boolean startDarkMode = shouldStartDarkMode(args);
        boolean startCompactMode = shouldStartCompactMode(args);

        setupModernLookAndFeel(startDarkMode);
        configureTooltips();

        SwingUtilities.invokeLater(() -> {
            Calendario calendar = new Calendario(startAlwaysOnTop, startupPosition, startDarkMode, startCompactMode);
            calendar.setVisible(true);
        });
    }

    private static boolean shouldStartAlwaysOnTop(String[] args) {
        for (String arg : args) {
            if ("--always-on-top".equalsIgnoreCase(arg)
                    || "--sempre-in-primo-piano".equalsIgnoreCase(arg)
                    || "-top".equalsIgnoreCase(arg)) {
                return true;
            }
        }
        return false;
    }

    private static boolean shouldStartDarkMode(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if ("--dark".equalsIgnoreCase(arg) || "--tema-scuro".equalsIgnoreCase(arg)) {
                return true;
            }
            if ("--light".equalsIgnoreCase(arg) || "--tema-chiaro".equalsIgnoreCase(arg)) {
                return false;
            }

            String value = getOptionValue(args, i, "--tema", "--theme");
            Boolean darkMode = parseDarkMode(value);
            if (darkMode != null) {
                if (!arg.contains("=")) {
                    i++;
                }
                return darkMode;
            }
        }
        return false;
    }

    private static boolean shouldStartCompactMode(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if ("--compact".equalsIgnoreCase(arg) || "--compatta".equalsIgnoreCase(arg)) {
                return true;
            }
            if ("--extended".equalsIgnoreCase(arg) || "--estesa".equalsIgnoreCase(arg)) {
                return false;
            }

            String value = getOptionValue(args, i, "--modalita", "--mode");
            Boolean compactMode = parseCompactMode(value);
            if (compactMode != null) {
                if (!arg.contains("=")) {
                    i++;
                }
                return compactMode;
            }
        }
        return true;
    }

    private static StartupPosition getStartupPosition(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            StartupPosition shortcutPosition = parseStartupPositionShortcut(arg);
            if (shortcutPosition != null) {
                return shortcutPosition;
            }

            String value = getOptionValue(args, i, "--posizione", "--position");
            StartupPosition position = parseStartupPosition(value);
            if (position != null) {
                if (!arg.contains("=")) {
                    i++;
                }
                return position;
            }
        }
        return StartupPosition.TOP_LEFT;
    }

    private static StartupPosition parseStartupPositionShortcut(String arg) {
        if (!arg.startsWith("--")) {
            return null;
        }
        return parseStartupPosition(arg.substring(2));
    }

    private static String getOptionValue(String[] args, int index, String... optionNames) {
        String arg = args[index];
        for (String optionName : optionNames) {
            if (arg.regionMatches(true, 0, optionName + "=", 0, optionName.length() + 1)) {
                return arg.substring(optionName.length() + 1);
            }
            if (optionName.equalsIgnoreCase(arg) && index + 1 < args.length) {
                return args[index + 1];
            }
        }
        return null;
    }

    private static Boolean parseDarkMode(String value) {
        if (value == null) {
            return null;
        }

        switch (normalizeArgumentValue(value)) {
            case "scuro":
            case "dark":
                return true;
            case "chiaro":
            case "light":
                return false;
            default:
                return null;
        }
    }

    private static Boolean parseCompactMode(String value) {
        if (value == null) {
            return null;
        }

        switch (normalizeArgumentValue(value)) {
            case "compatta":
            case "compatto":
            case "compact":
                return true;
            case "estesa":
            case "esteso":
            case "extended":
                return false;
            default:
                return null;
        }
    }

    private static StartupPosition parseStartupPosition(String value) {
        if (value == null) {
            return null;
        }

        String normalized = normalizeArgumentValue(value);

        switch (normalized) {
            case "alto-sinistra":
            case "in-alto-a-sinistra":
            case "top-left":
                return StartupPosition.TOP_LEFT;
            case "alto-destra":
            case "in-alto-a-destra":
            case "top-right":
                return StartupPosition.TOP_RIGHT;
            case "basso-sinistra":
            case "in-basso-a-sinistra":
            case "bottom-left":
                return StartupPosition.BOTTOM_LEFT;
            case "basso-destra":
            case "in-basso-a-destra":
            case "bottom-right":
                return StartupPosition.BOTTOM_RIGHT;
            case "centro":
            case "center":
                return StartupPosition.CENTER;
            default:
                return null;
        }
    }

    private static String normalizeArgumentValue(String value) {
        return Normalizer.normalize(value.trim().toLowerCase(), Normalizer.Form.NFD)
            .replaceAll("\\p{M}", "")
            .replace('_', '-')
            .replace(' ', '-');
    }
}
