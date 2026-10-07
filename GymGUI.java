//importing all the necessary libraries

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.ButtonGroup;
import javax.swing.BorderFactory;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.time.Year;
import java.time.LocalDate;

//main code begins from here
public class GymGUI extends JFrame {
    // Modern color palette
    private static final Color PRIMARY_COLOR = Color.BLACK; // Pure black background
    private static final Color NAV_COLOR = Color.BLACK; // Top nav bar now pure black
    private static final Color ACCENT_COLOR = new Color(46, 204, 113);
    private static final Color BUTTON_COLOR = new Color(41, 128, 185);
    private static final Color BUTTON_HOVER = new Color(52, 152, 219);
    private static final Color FIELD_BORDER = new Color(200, 200, 220);
    private static final Color FIELD_BG = new Color(255, 255, 255);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    // Add new color constants for button types and table styling
    private static final Color POSITIVE_COLOR = new Color(39, 174, 96); // Green
    private static final Color NEGATIVE_COLOR = new Color(231, 76, 60); // Red
    private static final Color INFO_COLOR = new Color(41, 128, 185); // Blue
    private static final Color NEUTRAL_COLOR = new Color(149, 165, 166); // Gray
    private static final Color TABLE_HEADER_BG = Color.BLACK;
    private static final Color TABLE_HEADER_FG = Color.WHITE;
    private static final Color TABLE_ROW_ALT = new Color(245, 250, 255);
    private static final Color TABLE_GRID = new Color(220, 220, 230);

    private ArrayList<GymMember> members = new ArrayList<>();
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JTextField[] textFields = new JTextField[12];
    private JComboBox<String> planComboBox;
    private JRadioButton maleRadio, femaleRadio;
    private ButtonGroup genderGroup;
    private JCheckBox premiumCheckBox;
    private JTextArea displayArea;

    public GymGUI() {
        initializeFrame();
        initializeComponents();
        setupMainPanel();
    }

    private void initializeComponents() {
        // Initialize text fields
        for (int i = 0; i < textFields.length; i++) {
            textFields[i] = createStyledTextField(20);
        }

        // Initialize gender radio buttons
        maleRadio = new JRadioButton("Male");
        femaleRadio = new JRadioButton("Female");
        genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);

        // Initialize premium checkbox
        premiumCheckBox = new JCheckBox("Premium Member");

        // Initialize plan combo box
        String[] plans = { "Basic", "Standard", "Deluxe" };
        planComboBox = new JComboBox<>(plans);

        // Initialize display area
        displayArea = new JTextArea();
        displayArea.setEditable(false);
    }

    private void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    private static JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(LABEL_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER, 2, true), // Rounded border
                new EmptyBorder(8, 14, 8, 14)
        ));
        field.setOpaque(true);
        field.setBackground(FIELD_BG);
        field.setForeground(new Color(50, 50, 50));
        field.setCaretColor(new Color(80, 80, 80));
        field.setPreferredSize(new Dimension(200, 36));
        return field;
    }

    private JComboBox<String> createStyledComboBox(String[] items, int width) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(LABEL_FONT);
        comboBox.setPreferredSize(new Dimension(width, 36));
        comboBox.setBackground(FIELD_BG);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER, 2, true),
                new EmptyBorder(0, 8, 0, 8)));
        return comboBox;
    }

    private void initializeFrame() {
        setTitle("Gym Management System");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(PRIMARY_COLOR);
    }

    private void setupMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(PRIMARY_COLOR);
        mainPanel.add(createTopNavBar(), BorderLayout.NORTH);
        mainPanel.add(createContentPanel(), BorderLayout.CENTER);
        add(mainPanel);
    }

    // Replaces createSidebar()
    private JPanel createTopNavBar() {
        JPanel navBar = new JPanel();
        navBar.setPreferredSize(new Dimension(1280, 70));
        navBar.setBackground(NAV_COLOR);
        navBar.setLayout(new BoxLayout(navBar, BoxLayout.X_AXIS));
        navBar.setBorder(new EmptyBorder(10, 30, 10, 30));

        String[] menuItems = { "Register Member", "Mark Attendance", "Manage Membership",
                "Upgrade Plan", "Payment", "Display", "Save/Load" };

        for (String item : menuItems) {
            JButton btn = createMenuButton(item);
            btn.addActionListener(new MenuButtonListener());
            btn.setMaximumSize(new Dimension(180, 45));
            btn.setAlignmentY(0.5f);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
            applyNavHoverEffect(btn, BUTTON_HOVER, NAV_COLOR);
            navBar.add(btn);
            navBar.add(Box.createHorizontalStrut(20));
        }
        navBar.add(Box.createHorizontalGlue());
        return navBar;
    }

    private void applyNavHoverEffect(JButton button, Color hoverColor, Color defaultColor) {
        button.setOpaque(true);
        button.setBackground(defaultColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(defaultColor);
            }
        });
    }

    private JPanel createContentPanel() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(createRegistrationPanel(), "register");
        contentPanel.add(createAttendancePanel(), "attendance");
        contentPanel.add(createMembershipPanel(), "membership");
        contentPanel.add(createUpgradePlanPanel(), "upgradePlan");
        contentPanel.add(createPaymentPanel(), "payment");

        contentPanel.add(createDisplayPanel(), "display");
        contentPanel.add(createSaveLoadPanel(), "saveload");

        return contentPanel;
    }

    private JPanel createRegistrationPanel() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                setOpaque(false);
            }
        };
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20),
                BorderFactory.createLineBorder(FIELD_BORDER, 2, true)
        ));
        panel.setBackground(FIELD_BG);

        // Create a tabbed pane for Regular and Premium Members
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Regular Member", new JScrollPane(createRegularMemberForm(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        tabbedPane.addTab("Premium Member", new JScrollPane(createPremiumMemberForm(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        JScrollPane tabScroll = new JScrollPane(tabbedPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        tabScroll.setBorder(null);
        panel.add(tabScroll, BorderLayout.CENTER);
        JLabel header = new JLabel("Resgister a New Member", SwingConstants.CENTER);
        header.setFont(TITLE_FONT);
        panel.add(header, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createRegularMemberForm() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(FIELD_BG);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Left Column - Personal Information
        JPanel leftColumn = new JPanel(new GridBagLayout());
        leftColumn.setBackground(FIELD_BG);
        leftColumn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 2), "Personal Information", 0, 0, LABEL_FONT, ACCENT_COLOR),
            new EmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Personal Info Fields
        JTextField idField = createStyledTextField(15);
        JTextField nameField = createStyledTextField(15);
        JTextField locationField = createStyledTextField(15);
        JTextField phoneField = createStyledTextField(15);
        JTextField emailField = createStyledTextField(15);

        // Gender radio buttons
        JRadioButton maleRadio = new JRadioButton("Male");
        JRadioButton femaleRadio = new JRadioButton("Female");
        maleRadio.setFont(LABEL_FONT);
        femaleRadio.setFont(LABEL_FONT);
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        genderPanel.setBackground(FIELD_BG);
        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);

        // DOB ComboBoxes
        JPanel dobPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        dobPanel.setBackground(FIELD_BG);

        String[] days = new String[31];
        String[] months = { "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December" };
        String[] years = new String[100];

        for (int i = 0; i < 31; i++) {
            days[i] = String.format("%02d", i + 1);
        }

        int currentYear = java.time.Year.now().getValue();
        for (int i = 0; i < 100; i++) {
            years[i] = String.valueOf(currentYear - i);
        }

        JComboBox<String> dayCombo = createStyledComboBox(days, 60);
        JComboBox<String> monthCombo = createStyledComboBox(months, 100);
        JComboBox<String> yearCombo = createStyledComboBox(years, 70);

        dobPanel.add(new JLabel("Day:"));
        dobPanel.add(dayCombo);
        dobPanel.add(new JLabel("Month:"));
        dobPanel.add(monthCombo);
        dobPanel.add(new JLabel("Year:"));
        dobPanel.add(yearCombo);

        // Add fields to left column
        gbc.gridx = 0; gbc.gridy = 0;
        leftColumn.add(new JLabel("Member ID:"), gbc);
        gbc.gridx = 1;
        leftColumn.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        leftColumn.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        leftColumn.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        leftColumn.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        leftColumn.add(locationField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        leftColumn.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        leftColumn.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        leftColumn.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        leftColumn.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        leftColumn.add(new JLabel("Date of Birth:"), gbc);
        gbc.gridx = 1;
        leftColumn.add(dobPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        leftColumn.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        leftColumn.add(genderPanel, gbc);

        // Right Column - Membership Information
        JPanel rightColumn = new JPanel(new GridBagLayout());
        rightColumn.setBackground(FIELD_BG);
        rightColumn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 2), "Membership Details", 0, 0, LABEL_FONT, ACCENT_COLOR),
            new EmptyBorder(20, 20, 20, 20)
        ));

        // Membership Start Date ComboBoxes
        JPanel startDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        startDatePanel.setBackground(FIELD_BG);

        JComboBox<String> startDayCombo = createStyledComboBox(days, 60);
        JComboBox<String> startMonthCombo = createStyledComboBox(months, 100);
        JComboBox<String> startYearCombo = createStyledComboBox(years, 70);

        startDatePanel.add(new JLabel("Day:"));
        startDatePanel.add(startDayCombo);
        startDatePanel.add(new JLabel("Month:"));
        startDatePanel.add(startMonthCombo);
        startDatePanel.add(new JLabel("Year:"));
        startDatePanel.add(startYearCombo);

        JTextField referralField = createStyledTextField(15);
        JTextField priceField = createStyledTextField(15);
        priceField.setText("6500");
        priceField.setEditable(false);

        // Add fields to right column
        gbc.gridx = 0; gbc.gridy = 0;
        rightColumn.add(new JLabel("Membership Start:"), gbc);
        gbc.gridx = 1;
        rightColumn.add(startDatePanel, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        rightColumn.add(new JLabel("Referral Source:"), gbc);
        gbc.gridx = 1;
        rightColumn.add(referralField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        rightColumn.add(new JLabel("Plan Price (Rs):"), gbc);
        gbc.gridx = 1;
        rightColumn.add(priceField, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(FIELD_BG);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton submitBtn = createStyledButton("Register Regular Member", POSITIVE_COLOR);
        submitBtn.setPreferredSize(new Dimension(200, 45));
        JButton clearBtn = createStyledButton("Clear Form", NEUTRAL_COLOR);
        clearBtn.setPreferredSize(new Dimension(200, 45));

        buttonPanel.add(submitBtn);
        buttonPanel.add(clearBtn);

        // Add action listeners
        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                idField.setText("");
                nameField.setText("");
                locationField.setText("");
                phoneField.setText("");
                emailField.setText("");
                dayCombo.setSelectedIndex(0);
                monthCombo.setSelectedIndex(0);
                yearCombo.setSelectedIndex(0);
                startDayCombo.setSelectedIndex(0);
                startMonthCombo.setSelectedIndex(0);
                startYearCombo.setSelectedIndex(0);
                referralField.setText("");
                genderGroup.clearSelection();
            }
        });

        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(idField.getText());
                    if (isDuplicateId(id)) {
                        showError("Member ID already exists!");
                        return;
                    }

                    String gender = maleRadio.isSelected() ? "Male" : femaleRadio.isSelected() ? "Female" : "";
                    if (gender.isEmpty()) {
                        showError("Please select a gender!");
                        return;
                    }

                    if (!priceField.getText().equals("6500")) {
                        showError("Invalid plan price configuration!");
                        return;
                    }

                    // Format DOB from combo boxes
                    String month = String.format("%02d", monthCombo.getSelectedIndex() + 1);
                    String dob = String.format("%s-%s-%s",
                            yearCombo.getSelectedItem(),
                            month,
                            dayCombo.getSelectedItem());

                    // Format Start Date from combo boxes
                    String startMonth = String.format("%02d", startMonthCombo.getSelectedIndex() + 1);
                    String startDate = String.format("%s-%s-%s",
                            startYearCombo.getSelectedItem(),
                            startMonth,
                            startDayCombo.getSelectedItem());

                    RegularMember member = new RegularMember(
                            id,
                            nameField.getText(),
                            locationField.getText(),
                            phoneField.getText(),
                            emailField.getText(),
                            gender,
                            dob,
                            startDate,
                            referralField.getText(),
                            "basic", // Default initial plan
                            6500,    // Default initial price
                            0,       // Initial attendance
                            0.0,     // Initial loyalty points
                            true     // Initial active status (assuming new members are active)
                    );

                    members.add(member);
                    showSuccess("Regular member added successfully!");
                    clearFields(idField, nameField, locationField, phoneField, emailField,
                            referralField);
                    dayCombo.setSelectedIndex(0);
                    monthCombo.setSelectedIndex(0);
                    yearCombo.setSelectedIndex(0);
                    startDayCombo.setSelectedIndex(0);
                    startMonthCombo.setSelectedIndex(0);
                    startYearCombo.setSelectedIndex(0);
                } catch (NumberFormatException ex) {
                    showError("Invalid Member ID!");
                }
            }
        });

        // Layout the main panel
        JPanel columnsPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        columnsPanel.setBackground(FIELD_BG);
        columnsPanel.add(leftColumn);
        columnsPanel.add(rightColumn);

        mainPanel.add(columnsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel createPremiumMemberForm() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(FIELD_BG);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Left Column - Personal Information
        JPanel leftColumn = new JPanel(new GridBagLayout());
        leftColumn.setBackground(FIELD_BG);
        leftColumn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 2), "Personal Information", 0, 0, LABEL_FONT, ACCENT_COLOR),
            new EmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Personal Info Fields
        JTextField idField = createStyledTextField(15);
        JTextField nameField = createStyledTextField(15);
        JTextField locationField = createStyledTextField(15);
        JTextField phoneField = createStyledTextField(15);
        JTextField emailField = createStyledTextField(15);

        // Gender radio buttons
        JRadioButton maleRadio = new JRadioButton("Male");
        JRadioButton femaleRadio = new JRadioButton("Female");
        maleRadio.setFont(LABEL_FONT);
        femaleRadio.setFont(LABEL_FONT);
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        genderPanel.setBackground(FIELD_BG);
        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);

        // DOB ComboBoxes
        JPanel dobPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        dobPanel.setBackground(FIELD_BG);

        String[] days = new String[31];
        String[] months = { "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December" };
        String[] years = new String[100];

        for (int i = 0; i < 31; i++) {
            days[i] = String.format("%02d", i + 1);
        }

        int currentYear = java.time.Year.now().getValue();
        for (int i = 0; i < 100; i++) {
            years[i] = String.valueOf(currentYear - i);
        }

        JComboBox<String> dayCombo = createStyledComboBox(days, 60);
        JComboBox<String> monthCombo = createStyledComboBox(months, 100);
        JComboBox<String> yearCombo = createStyledComboBox(years, 70);

        dobPanel.add(new JLabel("Day:"));
        dobPanel.add(dayCombo);
        dobPanel.add(new JLabel("Month:"));
        dobPanel.add(monthCombo);
        dobPanel.add(new JLabel("Year:"));
        dobPanel.add(yearCombo);

        // Add fields to left column
        gbc.gridx = 0; gbc.gridy = 0;
        leftColumn.add(new JLabel("Member ID:"), gbc);
        gbc.gridx = 1;
        leftColumn.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        leftColumn.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        leftColumn.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        leftColumn.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        leftColumn.add(locationField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        leftColumn.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        leftColumn.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        leftColumn.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        leftColumn.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        leftColumn.add(new JLabel("Date of Birth:"), gbc);
        gbc.gridx = 1;
        leftColumn.add(dobPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        leftColumn.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        leftColumn.add(genderPanel, gbc);

        // Right Column - Premium Membership Information
        JPanel rightColumn = new JPanel(new GridBagLayout());
        rightColumn.setBackground(FIELD_BG);
        rightColumn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 2), "Premium Membership Details", 0, 0, LABEL_FONT, ACCENT_COLOR),
            new EmptyBorder(20, 20, 20, 20)
        ));

        // Membership Start Date ComboBoxes
        JPanel startDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        startDatePanel.setBackground(FIELD_BG);

        JComboBox<String> startDayCombo = createStyledComboBox(days, 60);
        JComboBox<String> startMonthCombo = createStyledComboBox(months, 100);
        JComboBox<String> startYearCombo = createStyledComboBox(years, 70);

        startDatePanel.add(new JLabel("Day:"));
        startDatePanel.add(startDayCombo);
        startDatePanel.add(new JLabel("Month:"));
        startDatePanel.add(startMonthCombo);
        startDatePanel.add(new JLabel("Year:"));
        startDatePanel.add(startYearCombo);

        JTextField trainerField = createStyledTextField(15);
        JTextField priceField = createStyledTextField(15);
        priceField.setText("50000");
        priceField.setEditable(false);

        // Add fields to right column
        gbc.gridx = 0; gbc.gridy = 0;
        rightColumn.add(new JLabel("Membership Start:"), gbc);
        gbc.gridx = 1;
        rightColumn.add(startDatePanel, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        rightColumn.add(new JLabel("Personal Trainer:"), gbc);
        gbc.gridx = 1;
        rightColumn.add(trainerField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        rightColumn.add(new JLabel("Premium Charge (Rs):"), gbc);
        gbc.gridx = 1;
        rightColumn.add(priceField, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(FIELD_BG);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton submitBtn = createStyledButton("Register Premium Member", POSITIVE_COLOR);
        submitBtn.setPreferredSize(new Dimension(200, 45));
        JButton clearBtn = createStyledButton("Clear Form", NEUTRAL_COLOR);
        clearBtn.setPreferredSize(new Dimension(200, 45));

        buttonPanel.add(submitBtn);
        buttonPanel.add(clearBtn);

        // Add action listeners
        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                idField.setText("");
                nameField.setText("");
                locationField.setText("");
                phoneField.setText("");
                emailField.setText("");
                dayCombo.setSelectedIndex(0);
                monthCombo.setSelectedIndex(0);
                yearCombo.setSelectedIndex(0);
                startDayCombo.setSelectedIndex(0);
                startMonthCombo.setSelectedIndex(0);
                startYearCombo.setSelectedIndex(0);
                trainerField.setText("");
                genderGroup.clearSelection();
            }
        });

        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(idField.getText());
                    if (isDuplicateId(id)) {
                        showError("Member ID already exists!");
                        return;
                    }

                    String gender = maleRadio.isSelected() ? "Male" : femaleRadio.isSelected() ? "Female" : "";
                    if (gender.isEmpty()) {
                        showError("Please select a gender!");
                        return;
                    }

                    if (!priceField.getText().equals("50000")) {
                        showError("Invalid premium charge configuration!");
                        return;
                    }

                    // Format DOB from combo boxes
                    String month = String.format("%02d", monthCombo.getSelectedIndex() + 1);
                    String dob = String.format("%s-%s-%s",
                            yearCombo.getSelectedItem(),
                            month,
                            dayCombo.getSelectedItem());

                    // Format Start Date from combo boxes
                    String startMonth = String.format("%02d", startMonthCombo.getSelectedIndex() + 1);
                    String startDate = String.format("%s-%s-%s",
                            startYearCombo.getSelectedItem(),
                            startMonth,
                            startDayCombo.getSelectedItem());

                    PremiumMember member = new PremiumMember(
                            id,
                            nameField.getText(),
                            locationField.getText(),
                            phoneField.getText(),
                            emailField.getText(),
                            dob,
                            gender,
                            startDate,
                            trainerField.getText(),
                            50000, // Initial premium charge
                            0.0,   // Initial paid amount
                            0.0,   // Initial discount amount
                            false, // Initial isFullPayment status
                            0,     // Initial attendance
                            0.0,   // Initial loyalty points
                            true   // Initial active status
                    );

                    members.add(member);
                    showSuccess("Premium member added successfully!");
                    clearFields(idField, nameField, locationField, phoneField, emailField,
                            trainerField);
                    dayCombo.setSelectedIndex(0);
                    monthCombo.setSelectedIndex(0);
                    yearCombo.setSelectedIndex(0);
                    startDayCombo.setSelectedIndex(0);
                    startMonthCombo.setSelectedIndex(0);
                    startYearCombo.setSelectedIndex(0);
                } catch (NumberFormatException ex) {
                    showError("Invalid Member ID!");
                }
            }
        });

        // Layout the main panel
        JPanel columnsPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        columnsPanel.setBackground(FIELD_BG);
        columnsPanel.add(leftColumn);
        columnsPanel.add(rightColumn);

        mainPanel.add(columnsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel createUpgradePlanPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header Label
        JLabel header = new JLabel("Upgrade Membership Plan", SwingConstants.CENTER);
        header.setFont(TITLE_FONT);
        panel.add(header, BorderLayout.NORTH);

        // Main content panel with GridBagLayout
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Search Section
        JPanel searchPanel = new JPanel(new GridBagLayout());
        GridBagConstraints searchGbc = new GridBagConstraints();
        searchGbc.insets = new Insets(5, 5, 5, 5);

        JLabel idLabel = new JLabel("Member ID:");
        JTextField idField = createStyledTextField(15);
        idField.setPreferredSize(new Dimension(150, 30));

        JButton searchButton = createStyledButton("Search Member", BUTTON_COLOR);
        searchButton.setPreferredSize(new Dimension(150, 30));

        // Add Refresh Button
        JButton refreshButton = createStyledButton("Refresh All", BUTTON_COLOR);
        refreshButton.setPreferredSize(new Dimension(150, 30));

        searchGbc.gridx = 0;
        searchGbc.gridy = 0;
        searchGbc.anchor = GridBagConstraints.EAST;
        searchPanel.add(idLabel, searchGbc);

        searchGbc.gridx = 1;
        searchGbc.anchor = GridBagConstraints.WEST;
        searchPanel.add(idField, searchGbc);

        searchGbc.gridx = 2;
        searchPanel.add(searchButton, searchGbc);

        searchGbc.gridx = 3;
        searchPanel.add(refreshButton, searchGbc);

        // Table Setup
        String[] columns = { "Member ID", "Name", "Current Plan", "Price", "Status", "Eligible for Upgrade" };
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable memberTable = new JTable(tableModel);
        styleTable(memberTable);
        memberTable.setRowHeight(35); // Increased row height for more spacious look
        memberTable.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Larger font
        JScrollPane scrollPane = new JScrollPane(memberTable);
        scrollPane.setPreferredSize(new Dimension(1000, 300)); // Increased size
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around table

        // Plan Selection Components
        JLabel planLabel = new JLabel("Select New Plan:");
        String[] plans = { "Basic", "Standard", "Deluxe" };
        JComboBox<String> planCombo = new JComboBox<>(plans);
        planCombo.setPreferredSize(new Dimension(150, 30));

        // Add table selection listener
        memberTable.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int row = memberTable.getSelectedRow();
                    if (row >= 0) {
                        // Get the member ID from the selected row
                        int selectedId = (int) tableModel.getValueAt(row, 0);
                        idField.setText(String.valueOf(selectedId));

                        // Get the member and update plan combo box state
                        GymMember member = findMember(selectedId);
                        if (member instanceof RegularMember) {
                            RegularMember rm = (RegularMember) member;
                            planCombo.setEnabled(rm.getAttendance() >= 30 && !rm.getPlan().equalsIgnoreCase("deluxe"));
                        } else {
                            planCombo.setEnabled(false);
                        }
                    }
                }
            }
        });

        JButton upgradeButton = createStyledButton("Upgrade Plan", POSITIVE_COLOR);
        upgradeButton.setPreferredSize(new Dimension(150, 40));

        // Add Revert Button
        JButton revertButton = createStyledButton("Revert Membership", NEGATIVE_COLOR);
        revertButton.setPreferredSize(new Dimension(180, 40));

        // Add components to main panel
        JPanel planPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        planPanel.setOpaque(false);
        planLabel.setFont(LABEL_FONT);
        planPanel.add(planLabel);
        planCombo.setPreferredSize(new Dimension(150, 36));
        planPanel.add(planCombo);
        planPanel.add(upgradeButton);
        planPanel.add(revertButton);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        contentPanel.add(searchPanel, gbc);

        gbc.gridy = 1;
        contentPanel.add(scrollPane, gbc);

        gbc.gridy = 2;
        contentPanel.add(planPanel, gbc);

        // Search Button Action
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    tableModel.setRowCount(0);
                    int id = Integer.parseInt(idField.getText());
                    GymMember member = findMember(id);

                    if (member != null) {
                        Object[] rowData = new Object[6];
                        rowData[0] = member.getId();
                        rowData[1] = member.getName();
                        rowData[4] = member.isActiveStatus() ? "Active" : "Inactive";

                        if (member instanceof RegularMember) {
                            RegularMember rm = (RegularMember) member;
                            rowData[2] = rm.getPlan();
                            rowData[3] = rm.getPrice();
                            rowData[5] = rm.getAttendance() >= 30 ? "Yes" : "No (Attendance: " + rm.getAttendance() + ")";
                            planCombo.setEnabled(rm.getAttendance() >= 30 && !rm.getPlan().equalsIgnoreCase("deluxe"));
                        } else {
                            rowData[2] = "Premium";
                            rowData[3] = ((PremiumMember) member).getPremiumCharge();
                            rowData[5] = "No (Premium Member)";
                            planCombo.setEnabled(false);
                        }

                        tableModel.addRow(rowData);
                    } else {
                        showError("Member not found!");
                    }
                } catch (NumberFormatException ex) {
                    showError("Invalid Member ID format!");
                }
            }
        });

        // Refresh Button Action
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.setRowCount(0);
                for (GymMember member : members) {
                    Object[] rowData = new Object[6];
                    rowData[0] = member.getId();
                    rowData[1] = member.getName();
                    rowData[4] = member.isActiveStatus() ? "Active" : "Inactive";

                    if (member instanceof RegularMember) {
                        RegularMember rm = (RegularMember) member;
                        rowData[2] = rm.getPlan();
                        rowData[3] = rm.getPrice();
                        rowData[5] = rm.getAttendance() >= 30 ? "Yes" : "No (Attendance: " + rm.getAttendance() + ")";
                    } else {
                        rowData[2] = "Premium";
                        rowData[3] = ((PremiumMember) member).getPremiumCharge();
                        rowData[5] = "No (Premium Member)";
                    }

                    tableModel.addRow(rowData);
                }
                planCombo.setEnabled(false);
            }
        });

        // Upgrade Button Action
        upgradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(idField.getText());
                    GymMember member = findMember(id);

                    if (member instanceof RegularMember) {
                        RegularMember rm = (RegularMember) member;
                        String newPlan = (String) planCombo.getSelectedItem();
                        String result = rm.upgradePlan(newPlan);

                        if (result.startsWith("Successfully")) {
                            showSuccess(result);
                            searchButton.doClick();
                        } else {
                            showError(result);
                        }
                    } else {
                        showError("Only Regular members can upgrade plans!");
                    }
                } catch (NumberFormatException ex) {
                    showError("Invalid Member ID!");
                }
            }
        });

        // Revert Button Action
        revertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(idField.getText());
                    GymMember member = findMember(id);

                    if (member == null) {
                        showError("Member not found!");
                        return;
                    }

                    // Create reason input dialog
                    JPanel inputPanel = new JPanel(new BorderLayout());
                    JLabel reasonLabel = new JLabel("Revert Reason:");
                    JTextArea reasonField = new JTextArea(3, 20);
                    reasonField.setLineWrap(true);
                    JScrollPane scroll = new JScrollPane(reasonField);

                    inputPanel.add(reasonLabel, BorderLayout.NORTH);
                    inputPanel.add(scroll, BorderLayout.CENTER);

                    int result = JOptionPane.showConfirmDialog(
                            panel,
                            inputPanel,
                            "Enter Revert Reason",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {
                        String reason = reasonField.getText().trim();
                        if (reason.isEmpty()) {
                            showError("Reason cannot be empty!");
                            return;
                        }

                        if (member instanceof RegularMember) {
                            RegularMember rm = (RegularMember) member;
                            if (rm.getPlan().equalsIgnoreCase("basic")) {
                                showError("Already at basic plan!");
                                return;
                            }
                            rm.revertRegularMember(reason);
                            showSuccess("Reverted to Basic plan. Reason: " + reason);
                        } else if (member instanceof PremiumMember) {
                            PremiumMember pm = (PremiumMember) member;
                            pm.revertPremiumMember();
                            showSuccess("Reverted Premium membership. Reason: " + reason);
                        }

                        searchButton.doClick();
                        planCombo.setEnabled(false);
                    }
                } catch (NumberFormatException ex) {
                    showError("Invalid Member ID format!");
                }
            }
        });

        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAttendancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header Label
        JLabel header = new JLabel("Member Attendance Management", SwingConstants.CENTER);
        header.setFont(TITLE_FONT);
        panel.add(header, BorderLayout.NORTH);

        // Table setup for all members
        String[] columns = { "Member ID", "Name", "Type", "Attendance Count" };
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable memberTable = new JTable(tableModel);
        styleTable(memberTable);
        JScrollPane scrollPane = new JScrollPane(memberTable);

        // Initial population
        refreshAttendanceTable(tableModel);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel idLabel = new JLabel("Member ID:");
        JTextField memberIdField = createStyledTextField(15);
        memberIdField.setEditable(false);

        JButton markButton = createStyledButton("Mark Attendance", ACCENT_COLOR);
        JButton refreshButton = createStyledButton("Refresh List", BUTTON_COLOR);

        // Add components to input panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(idLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(memberIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(markButton);
        buttonPanel.add(refreshButton);
        inputPanel.add(buttonPanel, gbc);

        // Table selection listener
        memberTable.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int row = memberTable.getSelectedRow();
                    if (row >= 0) {
                        memberIdField.setText(tableModel.getValueAt(row, 0).toString());
                    }
                }
            }
        });

        // Mark attendance button action
        markButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int memberId = Integer.parseInt(memberIdField.getText());
                    GymMember member = findMember(memberId);

                    if (member != null) {
                        if (member.isActiveStatus()) {
                            member.markAttendance(); // Calls abstract method implementation
                            showSuccess("Attendance marked for ID: " + memberId);
                            refreshAttendanceTable(tableModel);
                        } else {
                            showError("Cannot mark attendance - membership inactive!");
                        }
                    } else {
                        showError("Member not found!");
                    }
                } catch (NumberFormatException ex) {
                    showError("Invalid member ID format!");
                }
            }
        });

        // Refresh button action
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshAttendanceTable(tableModel);
            }
        });

        JScrollPane inputScroll = new JScrollPane(inputPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        inputScroll.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(inputScroll, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshAttendanceTable(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);

        for (GymMember member : members) {
            String type = member instanceof PremiumMember ? "Premium" : "Regular";

            tableModel.addRow(new Object[] {
                    member.getId(),
                    member.getName(),
                    type,
                    member.getAttendance(),

            });
        }
    }

    private JPanel createPaymentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header Label
        JLabel header = new JLabel("Premium Member Payments", SwingConstants.CENTER);
        header.setFont(TITLE_FONT);
        panel.add(header, BorderLayout.NORTH);

        // Table setup for premium members
        String[] columns = { "Member ID", "Name", "Membership Type", "Total Amount", "Paid Amount", "Remaining",
                "Discount" };
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable memberTable = new JTable(tableModel);
        styleTable(memberTable);
        JScrollPane scrollPane = new JScrollPane(memberTable);

        // Populate premium members table
        refreshPremiumMembersTable(tableModel);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel idLabel = new JLabel("Member ID:");
        JTextField memberIdField = createStyledTextField(15);
        memberIdField.setEditable(false);

        JLabel amountLabel = new JLabel("Payment Amount:");
        JTextField amountField = createStyledTextField(15);

        JButton payButton = createStyledButton("Process Payment", ACCENT_COLOR);
        JButton refreshButton = createStyledButton("Refresh List", BUTTON_COLOR);

        // Add components to input panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(idLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(memberIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(amountLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(amountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(payButton);
        buttonPanel.add(refreshButton);
        inputPanel.add(buttonPanel, gbc);

        // Table selection listener
        memberTable.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int row = memberTable.getSelectedRow();
                    if (row >= 0) {
                        String memberId = tableModel.getValueAt(row, 0).toString();
                        memberIdField.setText(memberId);
                    }
                }
            }
        });

        // Payment button action
        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int memberId = Integer.parseInt(memberIdField.getText());
                    double amount = Double.parseDouble(amountField.getText());
                    PremiumMember member = (PremiumMember) findMember(memberId);

                    if (member != null) {
                        String result = member.payDueAmount(amount);

                        // If payment was successful and it's a full payment, calculate discount
                        if (result.startsWith("Payment successful") && member.getIsFullPayment()) {
                            member.calculateDiscount();
                            result += "\nDiscount of 10% has been applied!";
                        }

                        showSuccess(result);
                        amountField.setText("");
                        refreshPremiumMembersTable(tableModel);
                    } else {
                        showError("Member not found!");
                    }
                } catch (NumberFormatException ex) {
                    showError("Invalid input format!");
                } catch (ClassCastException ex) {
                    showError("Selected member is not a Premium member!");
                }
            }
        });

        // Refresh button action
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshPremiumMembersTable(tableModel);
            }
        });

        // Add components to main panel
        JScrollPane inputScroll = new JScrollPane(inputPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        inputScroll.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(inputScroll, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshPremiumMembersTable(DefaultTableModel tableModel) {
        tableModel.setRowCount(0); // Clear existing data

        for (GymMember member : members) {
            if (member instanceof PremiumMember) {
                PremiumMember pm = (PremiumMember) member;
                double total = pm.getPremiumCharge();
                double paid = pm.getPaidAmount();
                double remaining = total - paid - pm.getDiscountAmount();

                tableModel.addRow(new Object[] {
                        pm.getId(),
                        pm.getName(),
                        "Premium",
                        String.format("Rs%.2f", total),
                        String.format("Rs%.2f", paid),
                        String.format("Rs%.2f", remaining),
                        String.format("Rs%.2f", pm.getDiscountAmount())
                });
            }
        }
    }

    private JPanel createDisplayPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Table setup
        String[] columns = { "ID", "Name", "Type", "Location", "Phone", "Email", "Status" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(1000, 400));

        // Button panel setup
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        JButton allButton = createStyledButton("All Members", BUTTON_COLOR);
        allButton.setPreferredSize(new Dimension(200, 40));
        JButton regularButton = createStyledButton("Regular Members", BUTTON_COLOR);
        regularButton.setPreferredSize(new Dimension(200, 40));
        JButton premiumButton = createStyledButton("Premium Members", BUTTON_COLOR);
        premiumButton.setPreferredSize(new Dimension(200, 40));

        // Common action listener
        ActionListener filterAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setRowCount(0); // Clear table
                JButton source = (JButton) e.getSource();

                for (GymMember member : members) {
                    boolean addRow = false;

                    if (source == allButton) {
                        addRow = true;
                    } else if (source == regularButton && member instanceof RegularMember) {
                        addRow = true;
                    } else if (source == premiumButton && member instanceof PremiumMember) {
                        addRow = true;
                    }

                    if (addRow) {
                        model.addRow(new Object[]{
                                member.getId(),
                                member.getName(),
                                member instanceof PremiumMember ? "Premium" : "Regular",
                                member.getLocation(),
                                member.getPhone(),
                                member.getEmail(),
                                member.isActiveStatus() ? "Active" : "Inactive"
                        });
                    }
                }
            }
        };

        allButton.addActionListener(filterAction);
        regularButton.addActionListener(filterAction);
        premiumButton.addActionListener(filterAction);

        buttonPanel.add(allButton);
        buttonPanel.add(regularButton);
        buttonPanel.add(premiumButton);

        JLabel header = new JLabel("Member Directory", SwingConstants.CENTER);
        header.setFont(TITLE_FONT);

        panel.add(header, BorderLayout.NORTH);
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setPreferredSize(new Dimension(1000, 400));
        panel.add(tableScroll, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMembershipPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header Label
        JLabel header = new JLabel("Membership Management", SwingConstants.CENTER);
        header.setFont(TITLE_FONT);
        panel.add(header, BorderLayout.NORTH);

        // Input Panel with GridBagLayout for proper alignment
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(new EmptyBorder(20, 50, 20, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Member ID Label
        JLabel idLabel = new JLabel("Member ID:");
        idLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.EAST;
        inputPanel.add(idLabel, gbc);

        // Member ID Text Field
        JTextField memberIdField = createStyledTextField(20);
        memberIdField.setPreferredSize(new Dimension(200, 40));
        gbc.gridx = 1;
        gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(memberIdField, gbc);

        // Button Panel for Search and Refresh
        JPanel buttonPanelTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JButton searchButton = createStyledButton("Search", ACCENT_COLOR);
        JButton refreshButton = createStyledButton("Refresh All", BUTTON_COLOR);
        searchButton.setPreferredSize(new Dimension(100, 40));
        refreshButton.setPreferredSize(new Dimension(120, 40));

        buttonPanelTop.add(searchButton);
        buttonPanelTop.add(refreshButton);

        gbc.gridx = 2;
        gbc.weightx = 0.1;
        inputPanel.add(buttonPanelTop, gbc);

        // Table setup
        String[] columnNames = { "Member ID", "Name", "Plan", "Price", "Status", "Attendance", "Loyalty Points" };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable memberTable = new JTable(tableModel);
        styleTable(memberTable);
        JScrollPane scrollPane = new JScrollPane(memberTable);

        // Add table to input panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        inputPanel.add(scrollPane, gbc);

        // Populate table with all members initially
        refreshMembershipTable(tableModel);

        // Table selection listener
        memberTable.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = memberTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        int id = (int) tableModel.getValueAt(selectedRow, 0);
                        memberIdField.setText(String.valueOf(id));
                    }
                }
            }
        });

        // Search button action
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(memberIdField.getText());
                    GymMember member = findMember(id);
                    tableModel.setRowCount(0); // Clear previous data

                    if (member != null) {
                        addMemberToTable(tableModel, member);
                    } else {
                        showError("Member not found!");
                    }
                } catch (NumberFormatException ex) {
                    showError("Invalid Member ID!");
                }
            }
        });

        // Refresh button action
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshMembershipTable(tableModel);
                memberIdField.setText("");
            }
        });

        // Buttons
        JButton activateButton = createStyledButton("Activate Membership", ACCENT_COLOR);
        JButton deactivateButton = createStyledButton("Deactivate Membership", ACCENT_COLOR);
        activateButton.setPreferredSize(new Dimension(200, 40));
        deactivateButton.setPreferredSize(new Dimension(200, 40));

        // Action listeners for activate/deactivate
        activateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleMembershipAction(tableModel, memberIdField, true);
            }
        });
        deactivateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleMembershipAction(tableModel, memberIdField, false);
            }
        });

        // Button Panel
        JPanel buttonPanelBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanelBottom.add(activateButton);
        buttonPanelBottom.add(deactivateButton);

        // Add components to main panel
        JScrollPane inputScroll = new JScrollPane(inputPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        inputScroll.setBorder(null);
        panel.add(inputScroll, BorderLayout.CENTER);
        panel.add(buttonPanelBottom, BorderLayout.SOUTH);

        return panel;
    }

    // Helper method to refresh the table with all members
    private void refreshMembershipTable(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        for (GymMember member : members) {
            addMemberToTable(tableModel, member);
        }
    }

    // Helper method to add a single member to the table
    private void addMemberToTable(DefaultTableModel tableModel, GymMember member) {
        Object[] rowData = new Object[7];
        rowData[0] = member.getId();
        rowData[1] = member.getName();

        if (member instanceof RegularMember) {
            RegularMember rm = (RegularMember) member;
            rowData[2] = rm.getPlan();
            rowData[3] = rm.getPrice();
        } else if (member instanceof PremiumMember) {
            PremiumMember pm = (PremiumMember) member;
            rowData[2] = "Premium";
            rowData[3] = pm.getPremiumCharge();
        }

        rowData[4] = member.isActiveStatus() ? "Active" : "Inactive";
        rowData[5] = member.getAttendance();
        rowData[6] = member.getLoyaltyPoints();
        tableModel.addRow(rowData);
    }

    // Unified handler for activate/deactivate actions
    private void handleMembershipAction(DefaultTableModel tableModel, JTextField memberIdField, boolean activate) {
        try {
            int id = Integer.parseInt(memberIdField.getText());
            GymMember member = findMember(id);
            if (member != null) {
                if (activate) {
                    member.activateMembership();
                    showSuccess("Membership activated for ID: " + id);
                } else {
                    member.deactivateMembership();
                    showSuccess("Membership deactivated for ID: " + id);
                }
                refreshMembershipTable(tableModel);
            } else {
                showError("Member not found!");
            }
        } catch (NumberFormatException ex) {
            showError("Invalid Member ID!");
        }
    }

    private JPanel createSaveLoadPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(40, 40, 40, 40));
        panel.setBackground(FIELD_BG);

        // Header with better styling
        JLabel header = new JLabel("Data Management", SwingConstants.CENTER);
        header.setFont(TITLE_FONT);
        header.setBorder(new EmptyBorder(0, 0, 30, 0));
        panel.add(header, BorderLayout.NORTH);

        // Main content panel with GridBagLayout for precise control
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(FIELD_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        // Save Section
        JPanel saveSection = new JPanel(new BorderLayout());
        saveSection.setBackground(FIELD_BG);
        saveSection.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(POSITIVE_COLOR, 2), "Save Data", 0, 0, LABEL_FONT, POSITIVE_COLOR),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel saveLabel = new JLabel("Save all member data to file");
        saveLabel.setFont(LABEL_FONT);
        saveLabel.setHorizontalAlignment(SwingConstants.CENTER);
        saveSection.add(saveLabel, BorderLayout.NORTH);

        JButton saveButton = createStyledButton("💾 Save to File", POSITIVE_COLOR);
        saveButton.setPreferredSize(new Dimension(250, 50));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveToFile();
            }
        });
        saveSection.add(saveButton, BorderLayout.CENTER);

        // Load Section
        JPanel loadSection = new JPanel(new BorderLayout());
        loadSection.setBackground(FIELD_BG);
        loadSection.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(INFO_COLOR, 2), "Load Data", 0, 0, LABEL_FONT, INFO_COLOR),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel loadLabel = new JLabel("Load member data from file");
        loadLabel.setFont(LABEL_FONT);
        loadLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loadSection.add(loadLabel, BorderLayout.NORTH);

        JButton loadButton = createStyledButton("📁 Load from File", INFO_COLOR);
        loadButton.setPreferredSize(new Dimension(250, 50));
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFromFile();
            }
        });
        loadSection.add(loadButton, BorderLayout.CENTER);

        // Add sections to content panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        contentPanel.add(saveSection, gbc);

        gbc.gridx = 1;
        contentPanel.add(loadSection, gbc);

        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(Color.BLACK);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(255,255,255,30), 1, true));
        return button;
    }

    private JButton createStyledButton(String text, Color ignoredColor) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(Color.BLACK);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 45));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(30, 30, 30));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.BLACK);
            }
        });
        return button;
    }

    private class MenuButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = ((JButton) e.getSource()).getText();
            switch (command) {
                case "Register Member":
                    cardLayout.show(contentPanel, "register");
                    break;
                case "Mark Attendance":
                    cardLayout.show(contentPanel, "attendance");
                    break;
                case "Manage Membership":
                    cardLayout.show(contentPanel, "membership");
                    break;
                case "Upgrade Plan":
                    cardLayout.show(contentPanel, "upgradePlan");
                    break;
                case "Payment":
                    cardLayout.show(contentPanel, "payment");
                    break;
                case "Display":
                    cardLayout.show(contentPanel, "display");
                    break;
                case "Save/Load":
                    cardLayout.show(contentPanel, "saveload");
                    break;
            }
        }
    }

    private void addMember() {
        try {
            int id = Integer.parseInt(textFields[0].getText());
            if (isDuplicateId(id)) {
                showError("Member ID already exists!");
                return;
            }

            if (!validateRequiredFields()) {
                showError("Please fill all required fields and select a gender!");
                return;
            }

            if (premiumCheckBox.isSelected()) {
                addPremiumMember(id);
            } else {
                addRegularMember(id);
            }
        } catch (NumberFormatException ex) {
            showError("Invalid ID format!");
        }
    }

    private String getSelectedGender() {
        if (maleRadio.isSelected()) {
            return "Male";
        } else if (femaleRadio.isSelected()) {
            return "Female";
        } else {
            return ""; // No gender selected
        }
    }

    private void addRegularMember(int id) {
        String gender = getSelectedGender();
        if (gender.isEmpty()) {
            showError("Please select a gender!");
            return;
        }

        RegularMember member = new RegularMember(
                id,
                textFields[1].getText(), // Name
                textFields[2].getText(), // Location
                textFields[3].getText(), // Phone
                textFields[4].getText(), // Email
                gender, // Gender
                textFields[5].getText(), // DOB
                textFields[6].getText(), // Membership Start Date
                textFields[7].getText(), // Referral Source
                "basic", // Default initial plan
                6500,    // Default initial price
                0,       // Initial attendance
                0.0,     // Initial loyalty points
                true     // Initial active status
        );
        members.add(member);
        showSuccess("Regular member added successfully!");
        clearFields();
    }

    private void addPremiumMember(int id) {
        String gender = getSelectedGender();
        if (gender.isEmpty()) {
            showError("Please select a gender!");
            return;
        }

        PremiumMember member = new PremiumMember(
                id,
                textFields[1].getText(), // Name
                textFields[2].getText(), // Location
                textFields[3].getText(), // Phone
                textFields[4].getText(), // Email
                textFields[5].getText(), // DOB
                gender, // Gender
                textFields[6].getText(), // Membership Start Date
                textFields[8].getText(), // Personal Trainer
                50000, // Initial premium charge
                0.0,   // Initial paid amount
                0.0,   // Initial discount amount
                false, // Initial isFullPayment status
                0,     // Initial attendance
                0.0,   // Initial loyalty points
                true   // Initial active status
        );
        members.add(member);
        showSuccess("Premium member added successfully!");
        clearFields();
    }

    private boolean validateRequiredFields() {
        for (int i = 0; i < 7; i++) {
            if (textFields[i].getText().trim().isEmpty())
                return false;
        }
        return genderGroup.getSelection() != null;
    }

    private GymMember findMember(int id) {
        for (GymMember member : members) {
            if (member.getId() == id)
                return member;
        }
        return null;
    }

    private void toggleMemberType() {
        boolean isPremium = premiumCheckBox.isSelected();
        textFields[8].setVisible(isPremium);
        textFields[9].setVisible(isPremium);
        textFields[10].setVisible(!isPremium);
        textFields[11].setVisible(isPremium);
        planComboBox.setVisible(!isPremium);
    }

    private void clearFields() {
        for (JTextField field : textFields)
            field.setText("");
        genderGroup.clearSelection();
        premiumCheckBox.setSelected(false);
    }

    private boolean isDuplicateId(int id) {
        for (GymMember member : members) {
            if (member.getId() == id)
                return true;
        }
        return false;
    }

    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("MemberDetails.txt"))) {
            // Corrected header - ensure it matches the fields being saved
            writer.println(
                    "Type,ID,Name,Location,Phone,Email,Gender,DOB,MembershipStart,Plan,Price,Attendance,LoyaltyPoints,Active,Referral,PersonalTrainer,PremiumCharge,PaidAmount,DiscountAmount,IsFullPayment");

            for (GymMember member : members) {
                if (member instanceof RegularMember) {
                    RegularMember rm = (RegularMember) member;
                    writer.println(String.format(
                            "Regular,%d,%s,%s,%s,%s,%s,%s,%s,%s,%.2f,%d,%.2f,%s,%s,,,,,", // Added empty placeholders for premium fields
                            rm.getId(),
                            sanitize(rm.getName()),
                            sanitize(rm.getLocation()),
                            sanitize(rm.getPhone()),
                            sanitize(rm.getEmail()),
                            sanitize(rm.getGender()),
                            sanitize(rm.getDOB()),
                            sanitize(rm.getMembershipStartDate()),
                            sanitize(rm.getPlan()),
                            rm.getPrice(),
                            rm.getAttendance(),
                            rm.getLoyaltyPoints(),
                            rm.isActiveStatus(),
                            sanitize(rm.getReferralSource())));
                } else if (member instanceof PremiumMember) {
                    PremiumMember pm = (PremiumMember) member;
                    writer.println(String.format(
                            "Premium,%d,%s,%s,%s,%s,%s,%s,%s,,%d,%.2f,%s,,%s,%.2f,%.2f,%.2f,%s", // Plan and Price are blank for premium, Referral is blank
                            pm.getId(),
                            sanitize(pm.getName()),
                            sanitize(pm.getLocation()),
                            sanitize(pm.getPhone()),
                            sanitize(pm.getEmail()),
                            sanitize(pm.getGender()),
                            sanitize(pm.getDOB()),
                            sanitize(pm.getMembershipStartDate()),
                            // No plan for premium (or use a placeholder like "PremiumPlan")
                            // No price for premium (using premiumCharge instead)
                            pm.getAttendance(),
                            pm.getLoyaltyPoints(),
                            pm.isActiveStatus(),
                            // No referral for premium (or use a placeholder)
                            sanitize(pm.getPersonalTrainer()),
                            pm.getPremiumCharge(),
                            pm.getPaidAmount(),
                            pm.getDiscountAmount(),
                            pm.getIsFullPayment()));
                }
            }
            showSuccess("Member details saved successfully!");
        } catch (IOException ex) {
            showError("Error saving file: " + ex.getMessage());
        }
    }

    // Helper method to handle null values and commas
    private String sanitize(String input) {
        return input == null ? "" : input.replace(",", ";");
    }

    private void loadFromFile() {
        boolean hadFormatError = false;
        try (BufferedReader reader = new BufferedReader(new FileReader("MemberDetails.txt"))) {
            members.clear();
            String headerLine = reader.readLine(); // Read header
            if (headerLine == null) {
                showError("File is empty or header is missing.");
                return;
            }
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length < 20) {
                    String[] newParts = new String[20];
                    System.arraycopy(parts, 0, newParts, 0, parts.length);
                    for (int i = parts.length; i < 20; i++) newParts[i] = "";
                    parts = newParts;
                } else if (parts.length > 20) {
                    String[] newParts = new String[20];
                    System.arraycopy(parts, 0, newParts, 0, 20);
                    parts = newParts;
                }
                try {
                    String type = parts[0];
                    int id = tryParseInt(parts[1], 0);
                    String name = desanitize(parts[2]);
                    String location = desanitize(parts[3]);
                    String phone = desanitize(parts[4]);
                    String email = desanitize(parts[5]);
                    String gender = desanitize(parts[6]);
                    String dob = desanitize(parts[7]);
                    String membershipStart = desanitize(parts[8]);
                    String plan = desanitize(parts[9]);
                    double price = tryParseDouble(parts[10], 0.0);
                    int attendance = tryParseInt(parts[11], 0);
                    double loyalty = tryParseDouble(parts[12], 0.0);
                    boolean active = tryParseBoolean(parts[13], true);
                    String referral = desanitize(parts[14]);
                    String trainer = desanitize(parts[15]);
                    double premiumCharge = tryParseDouble(parts[16], 0.0);
                    double paidAmount = tryParseDouble(parts[17], 0.0);
                    double discountAmount = tryParseDouble(parts[18], 0.0);
                    boolean isFullPayment = tryParseBoolean(parts[19], false);

                    if (type.equals("Regular")) {
                        RegularMember member = new RegularMember(
                                id, name, location, phone, email,
                                gender, dob, membershipStart, referral,
                                plan, price, attendance, loyalty, active);
                        members.add(member);
                    } else if (type.equals("Premium")) {
                        PremiumMember member = new PremiumMember(
                                id, name, location, phone, email,
                                dob, gender, membershipStart, trainer,
                                premiumCharge, paidAmount, discountAmount, isFullPayment,
                                attendance, loyalty, active);
                        members.add(member);
                    }
                } catch (Exception ex) {
                    hadFormatError = true;
                }
            }
            if (hadFormatError) {
                showSuccess("Members loaded with some format errors. Some values may be defaulted.");
            } else {
                showSuccess("Members loaded successfully!");
            }
        } catch (IOException ex) {
            showError("Error loading file: " + ex.getMessage());
        }
    }

    // Helper parse methods for robust loading
    private int tryParseInt(String s, int def) {
        try { return Integer.parseInt(s); } catch (Exception e) { return def; }
    }
    private double tryParseDouble(String s, double def) {
        try { return Double.parseDouble(s); } catch (Exception e) { return def; }
    }
    private boolean tryParseBoolean(String s, boolean def) {
        if (s == null) return def;
        if (s.equalsIgnoreCase("true")) return true;
        if (s.equalsIgnoreCase("false")) return false;
        return def;
    }

    // Helper method to restore original values
    private String desanitize(String input) {
        return input.replace(";", ",");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    // Table styling utility
    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setRowHeight(28);
        table.setShowGrid(true);
        table.setGridColor(TABLE_GRID);
        table.setSelectionBackground(new Color(174, 214, 241));
        table.setSelectionForeground(Color.BLACK);
        table.setFillsViewportHeight(true);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBorder(BorderFactory.createLineBorder(FIELD_BORDER, 1, true));
        // Alternate row color
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                java.awt.Component comp = super.getTableCellRendererComponent(t, v, s, f, r, c);
                comp.setBackground(r % 2 == 0 ? Color.WHITE : TABLE_ROW_ALT);
                return comp;
            }
        });
        // Header styling
        javax.swing.table.JTableHeader header = table.getTableHeader();
        header.setBackground(TABLE_HEADER_BG);
        header.setForeground(TABLE_HEADER_FG);
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT_COLOR));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GymGUI().setVisible(true);
            }
        });
    }
}
