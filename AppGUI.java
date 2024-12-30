import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Map;


public class AppGUI {

    private static JFrame frame;
    private static JPanel panel;
    private static int choice;

    public static void main(String[] args) {
        // Start the application
        SwingUtilities.invokeLater(AppGUI::createMainMenu);
    }

    // Method to create the main menu
    public static void createMainMenu() {
        frame = new JFrame("Welcome to the System");
        panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));

        JButton loginButton = new JButton("Login");
        
        JButton exitButton = new JButton("Exit");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createLoginForm();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);  // Exit the application
            }
        });

        panel.add(loginButton);
        panel.add(exitButton);

        frame.add(panel);
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // Method to create the login form
    public static void createLoginForm() {
        JFrame loginFrame = new JFrame("Login");
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(4, 2));

        JLabel emailLabel = new JLabel("Email: ");
        JTextField emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password: ");
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton forgotPasswordButton = new JButton("Forgot Password");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                try {
                    User user = User.authenticate(email, password);
                    if (user == null ) {
                        JOptionPane.showMessageDialog(loginFrame, "This user was not found.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                    } else {
                        int roleId = (int) user.getRoleId();
                        if (roleId == 1) {
                            createStudentMenu(user);
                        } else if (roleId == 2) {
                            createTeacherMenu(user);
                        } else if (roleId == 3) {
                            createAdminMenu(user);
                        } else {
                            JOptionPane.showMessageDialog(loginFrame, "Invalid role or under development.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (ClassNotFoundException | SQLException ex) {
                    JOptionPane.showMessageDialog(loginFrame, "Error: " + ex.getMessage(), "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        forgotPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createForgotPasswordForm();
            }
        });

        loginPanel.add(emailLabel);
        loginPanel.add(emailField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(forgotPasswordButton);

        loginFrame.add(loginPanel);
        loginFrame.setSize(300, 250);
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loginFrame.setVisible(true);
    }

    public static void createForgotPasswordForm() {
        JFrame forgotPasswordFrame = new JFrame("Forgot Password");
        JPanel forgotPasswordPanel = new JPanel();
        forgotPasswordPanel.setLayout(new GridLayout(4, 2));  // Increased grid size for the new password field
    
        JLabel emailLabel = new JLabel("Enter your Email: ");
        JTextField emailField = new JTextField();
        JLabel newPasswordLabel = new JLabel("Enter New Password: ");
        JPasswordField newPasswordField = new JPasswordField();
        JButton submitButton = new JButton("Submit");
    
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String newPassword = new String(newPasswordField.getPassword());
    
                // Basic validation for email
                if (email.isEmpty()) {
                    JOptionPane.showMessageDialog(forgotPasswordFrame, "Email cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!email.contains("@") || !email.contains(".")) {
                    JOptionPane.showMessageDialog(forgotPasswordFrame, "Invalid email format", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
    
                // Basic validation for new password
                if (newPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(forgotPasswordFrame, "New Password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
    
                // Handle password reset logic here
                try {
                    User.forgotPassword(email, newPassword);
                    JOptionPane.showMessageDialog(forgotPasswordFrame, "Password successfully reset for: " + email);
                    forgotPasswordFrame.dispose(); // Close the forgot password form
                } catch (ClassNotFoundException | SQLException e1) {
                    
                    JOptionPane.showMessageDialog(forgotPasswordFrame, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            }
        });
    
        forgotPasswordPanel.add(emailLabel);
        forgotPasswordPanel.add(emailField);
        forgotPasswordPanel.add(newPasswordLabel);
        forgotPasswordPanel.add(newPasswordField);
        forgotPasswordPanel.add(submitButton);
    
        forgotPasswordFrame.add(forgotPasswordPanel);
        forgotPasswordFrame.setSize(300, 200);  // Adjusted size to accommodate the new fields
        forgotPasswordFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        forgotPasswordFrame.setVisible(true);
    }
    
    // Method to create the sign-up form
    public static void createSignUpForm() {
        JFrame signUpFrame = new JFrame("Sign Up");
        JPanel signUpPanel = new JPanel();
        signUpPanel.setLayout(new GridLayout(6, 2));
    
        JLabel nameLabel = new JLabel("Name: ");
        JTextField nameField = new JTextField();
        JLabel phoneLabel = new JLabel("Phone: ");
        JTextField phoneField = new JTextField();
        JLabel emailLabel = new JLabel("Email: ");
        JTextField emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password: ");
        JPasswordField passwordField = new JPasswordField();
        JLabel roleLabel = new JLabel("Role: ");
    
        // Create a dropdown menu (JComboBox) for roles
        String[] roles = {"Select Role", "Student", "Teacher", "Admin"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
    
        JButton signUpButton = new JButton("Sign Up");
    
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String phone = phoneField.getText().trim();
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                String selectedRole = (String) roleComboBox.getSelectedItem();
        
                // Check if any field is empty
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(signUpFrame, "Name cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (phone.isEmpty()) {
                    JOptionPane.showMessageDialog(signUpFrame, "Phone cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (email.isEmpty()) {
                    JOptionPane.showMessageDialog(signUpFrame, "Email cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(signUpFrame, "Password cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (selectedRole == null || selectedRole.equals("Select Role")) {
                    JOptionPane.showMessageDialog(signUpFrame, "Please select a valid role.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
        
                // Map the selected role to corresponding integer
                int role = 0;
                switch (selectedRole) {
                    case "Student":
                        role = 1;

                        break;
                    case "Teacher":
                        role = 2;
                        break;
                    case "Admin":
                        role = 3;
                        break;
                    default:
                        JOptionPane.showMessageDialog(signUpFrame, "Please select a valid role.", "Role Error", JOptionPane.ERROR_MESSAGE);
                        return;
                }
        
                // Create User and handle registration
                User user = new User(name, email, phone, password, role, true);
                try {
                    user.register();
                    if (role == 1) {
                        JOptionPane.showMessageDialog(signUpFrame, " Student Acount added successfully ! " , " Success ", JOptionPane.INFORMATION_MESSAGE);
                        signUpFrame.dispose();
                    } else if (role == 2) {
                        JOptionPane.showMessageDialog(signUpFrame, " Teacher Acount added successfully ! ", " Success ", JOptionPane.INFORMATION_MESSAGE);
                        signUpFrame.dispose();
                    } else if (role == 3) {
                        JOptionPane.showMessageDialog(signUpFrame, " Admin Acount added successfully " , " Success ", JOptionPane.INFORMATION_MESSAGE);
                        signUpFrame.dispose();
                    }
                } catch (ClassNotFoundException | SQLException ex) {
                    JOptionPane.showMessageDialog(signUpFrame, "Error: " + ex.getMessage(), "Sign Up Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        signUpPanel.add(nameLabel);
        signUpPanel.add(nameField);
        signUpPanel.add(phoneLabel);
        signUpPanel.add(phoneField);
        signUpPanel.add(emailLabel);
        signUpPanel.add(emailField);
        signUpPanel.add(passwordLabel);
        signUpPanel.add(passwordField);
        signUpPanel.add(roleLabel);
        signUpPanel.add(roleComboBox);
        signUpPanel.add(new JLabel());  // Empty cell
        signUpPanel.add(signUpButton);
    
        signUpFrame.add(signUpPanel);
        signUpFrame.setSize(300, 300);
        signUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        signUpFrame.setVisible(true);
    }
    public static void createStudentMenu(User loggedInUser) {
        JFrame studentFrame = new JFrame("Student Menu");
        JPanel studentPanel = new JPanel();
        studentPanel.setLayout(new GridLayout(5, 1)); // Increased rows for additional buttons

        JButton reportCardButton = new JButton("Get Report Card");
        JButton notificationsButton = new JButton("Get Notifications");
        JButton coursesButton = new JButton("Get Courses");
        JButton profileButton = new JButton("View Profile");
        JButton logoutButton = new JButton("Log Out");

        // View/Update Profile Button Action
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                                        
                String currentProfile = 
                " Name : " + loggedInUser.getName() + "\n" +
                                        " Email : " + loggedInUser.getEmail() + "\n" +
                                        " Phone : " + loggedInUser.getPhone();

                JTextArea profileArea = new JTextArea(currentProfile);
                profileArea.setEditable(false);
                JPanel profilePanel = new JPanel(new BorderLayout());
                profilePanel.add(new JScrollPane(profileArea), BorderLayout.CENTER);

                int option = JOptionPane.showConfirmDialog(studentFrame, profilePanel, "View Profile", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    // Allow user to update profile
                    JTextField nameField = new JTextField(loggedInUser.getName());
                    JTextField emailField = new JTextField(loggedInUser.getEmail());
                    JTextField phoneField = new JTextField(loggedInUser.getPhone());

                    JPanel updatePanel = new JPanel(new GridLayout(3, 2));
                    updatePanel.add(new JLabel("Name:"));
                    updatePanel.add(nameField);
                    updatePanel.add(new JLabel("Email:"));
                    updatePanel.add(emailField);
                    updatePanel.add(new JLabel("Phone:"));
                    updatePanel.add(phoneField);

                    int updateOption = JOptionPane.showConfirmDialog(studentFrame, updatePanel, "Update Profile", JOptionPane.OK_CANCEL_OPTION);
                    if (updateOption == JOptionPane.OK_OPTION) {
                        try {
                            // Update the user profile (save to the database)
                            loggedInUser.updateProfile(nameField.getText(), phoneField.getText(), emailField.getText());
                            JOptionPane.showMessageDialog(studentFrame, "Profile updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(studentFrame, "Error updating profile: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        // Log Out Button Action
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    loggedInUser.logout();
                    JOptionPane.showMessageDialog(studentFrame, "Logged out successfully.", "Log Out", JOptionPane.INFORMATION_MESSAGE);
                    studentFrame.dispose();  // Close the student menu window after logout
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(studentFrame, "Error logging out: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Report Card Button Action
        reportCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField studentIdField = new JTextField();
                JPanel panel = new JPanel(new GridLayout(1, 2));
                panel.add(new JLabel("Enter Student ID:"));
                panel.add(studentIdField);

                int option = JOptionPane.showConfirmDialog(studentFrame, panel, "Enter Student ID", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    try {
                        int studentId = Integer.parseInt(studentIdField.getText());

                        // Fetch report card from the database (Example: using a method getReportCard)
                        String reportCard = Students.getReportCard(studentId);

                        // Show report card details
                        JOptionPane.showMessageDialog(studentFrame, reportCard, "Report Card", JOptionPane.INFORMATION_MESSAGE);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(studentFrame, "Invalid Student ID. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(studentFrame, "Error fetching report card: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Notifications Button Action
        notificationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Fetch notifications (Example: using a method getNotifications)
                    Map<String, String> notifications = Students.getNotifications();

                    if (notifications.isEmpty()) {
                        JOptionPane.showMessageDialog(studentFrame, "No notifications available.", "Notifications", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        StringBuilder notificationList = new StringBuilder();
                        for (Map.Entry<String, String> entry : notifications.entrySet()) {
                            notificationList.append("Title: ").append(entry.getKey()).append("\n");
                            notificationList.append("Message: ").append(entry.getValue()).append("\n\n");
                        }

                        // Display notifications
                        JOptionPane.showMessageDialog(studentFrame, notificationList.toString(), "Notifications", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(studentFrame, "Error fetching notifications: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Courses Button Action
        coursesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField studentIdField = new JTextField();
                JPanel panel = new JPanel(new GridLayout(1, 2));
                panel.add(new JLabel("Enter Student ID:"));
                panel.add(studentIdField);

                int option = JOptionPane.showConfirmDialog(studentFrame, panel, "Enter Student ID", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    try {
                        int studentId = Integer.parseInt(studentIdField.getText());

                        // Fetch courses from the database (Example: using a method getCourses)
                        String courses = Students.getCourses(studentId);

                        // Show course list
                        JOptionPane.showMessageDialog(studentFrame, courses, "Courses", JOptionPane.INFORMATION_MESSAGE);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(studentFrame, "Invalid Student ID. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(studentFrame, "Error fetching courses: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        studentPanel.add(reportCardButton);
        studentPanel.add(notificationsButton);
        studentPanel.add(coursesButton);
        studentPanel.add(profileButton);  // Add profile button to the menu
        studentPanel.add(logoutButton);  // Add logout button to the menu

        studentFrame.add(studentPanel);
        studentFrame.setSize(300, 250);  // Adjust size to fit the new buttons
        studentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        studentFrame.setVisible(true);
    }
    
    public static void createTeacherMenu(User loggedInUser) {

        JFrame teacherFrame = new JFrame("Teacher Menu");
        JPanel teacherPanel = new JPanel();
        teacherPanel.setLayout(new GridLayout(9, 1)); // Adjusted grid for 9 options (8 existing + 1 new)

        // Adding buttons for each option
        JButton addExamButton = new JButton("Add Exam");
        JButton updateExamButton = new JButton("Update Exam");
        JButton getExamButton = new JButton("Get Exam");
        JButton submitDegreeButton = new JButton("Submit Degree");
        JButton getNotificationsButton = new JButton("Get Notifications");
        JButton markAttendanceButton = new JButton("Mark Attendance");
        JButton getEnrolledStudentsButton = new JButton("Get Enrolled Students");
        JButton profileButton = new JButton("View Profile"); // View Profile Button
        JButton logoutButton = new JButton("Logout"); // Logout Button

        addExamButton.addActionListener(e -> {
            JPanel inputPanel = new JPanel(new GridLayout(3, 2));
            JTextField courseIdField = new JTextField();
            JTextField examNameField = new JTextField();
            JTextField examDateField = new JTextField();
            inputPanel.add(new JLabel("Enter Course ID:"));
            inputPanel.add(courseIdField);
            inputPanel.add(new JLabel("Enter Exam Name:"));
            inputPanel.add(examNameField);
            inputPanel.add(new JLabel("Enter Exam Date (YYYY-MM-DD):"));
            inputPanel.add(examDateField);
        
            int option = JOptionPane.showConfirmDialog(teacherFrame, inputPanel, "Add Exam", JOptionPane.OK_CANCEL_OPTION);
        
            if (option == JOptionPane.OK_OPTION) {
                try {
                    if (courseIdField.getText().isEmpty() || examNameField.getText().isEmpty() || examDateField.getText().isEmpty()) {
                        throw new IllegalArgumentException("All fields must be filled!");
                    }
                    int courseId = Integer.parseInt(courseIdField.getText());
                    String examName = examNameField.getText();
                    String examDate = examDateField.getText();
                    int examId = Teacher.addExam(courseId, examName, examDate);
                    JOptionPane.showMessageDialog(teacherFrame, "Exam added successfully! Exam ID: " + examId, "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(teacherFrame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        updateExamButton.addActionListener(e -> {
            JPanel inputPanel = new JPanel(new GridLayout(3, 2));
            JTextField examIdField = new JTextField();
            JTextField newExamNameField = new JTextField();
            JTextField newExamDateField = new JTextField();
            inputPanel.add(new JLabel("Enter Exam ID:"));
            inputPanel.add(examIdField);
            inputPanel.add(new JLabel("Enter New Exam Name:"));
            inputPanel.add(newExamNameField);
            inputPanel.add(new JLabel("Enter New Exam Date (YYYY-MM-DD):"));
            inputPanel.add(newExamDateField);
        
            int option = JOptionPane.showConfirmDialog(teacherFrame, inputPanel, "Update Exam", JOptionPane.OK_CANCEL_OPTION);
        
            if (option == JOptionPane.OK_OPTION) {
                try {
                    if (examIdField.getText().isEmpty() || newExamNameField.getText().isEmpty() || newExamDateField.getText().isEmpty()) {
                        throw new IllegalArgumentException("All fields must be filled!");
                    }
                    int examId = Integer.parseInt(examIdField.getText());
                    String newExamName = newExamNameField.getText();
                    String newExamDate = newExamDateField.getText();
                    Teacher.updateExam(examId, newExamName, newExamDate);
                    JOptionPane.showMessageDialog(teacherFrame, "Exam updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(teacherFrame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
                
                profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String currentProfile =
                        " Name : " + loggedInUser.getName() + "\n" +
                                " Email : " + loggedInUser.getEmail() + "\n" +
                                " Phone : " + loggedInUser.getPhone();

                JTextArea profileArea = new JTextArea(currentProfile);
                profileArea.setEditable(false);
                JPanel profilePanel = new JPanel(new BorderLayout());
                profilePanel.add(new JScrollPane(profileArea), BorderLayout.CENTER);

                int option = JOptionPane.showConfirmDialog(teacherFrame, profilePanel, "View Profile", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    // Allow user to update profile
                    JTextField nameField = new JTextField(loggedInUser.getName());
                    JTextField emailField = new JTextField(loggedInUser.getEmail());
                    JTextField phoneField = new JTextField(loggedInUser.getPhone());

                    JPanel updatePanel = new JPanel(new GridLayout(3, 2));
                    updatePanel.add(new JLabel("Name:"));
                    updatePanel.add(nameField);
                    updatePanel.add(new JLabel("Email:"));
                    updatePanel.add(emailField);
                    updatePanel.add(new JLabel("Phone:"));
                    updatePanel.add(phoneField);

                    int updateOption = JOptionPane.showConfirmDialog(teacherFrame, updatePanel, "Update Profile", JOptionPane.OK_CANCEL_OPTION);
                    if (updateOption == JOptionPane.OK_OPTION) {
                        if (nameField.getText().isEmpty() || emailField.getText().isEmpty() || phoneField.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(teacherFrame, "All fields must be filled out!", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        try {
                            // Update the user profile (save to the database)
                            loggedInUser.updateProfile(nameField.getText(), phoneField.getText(), emailField.getText());
                            JOptionPane.showMessageDialog(teacherFrame, "Profile updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(teacherFrame, "Error updating profile: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        // Logout Button Action
        logoutButton.addActionListener(e -> {
            try {
                loggedInUser.logout(); // Assuming the logout method will set currentState to false in the database
                JOptionPane.showMessageDialog(teacherFrame, "Logged out successfully!");
                teacherFrame.dispose(); // Close the teacher menu after logging out
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(teacherFrame, "Error: " + ex.getMessage());
            }
        });

        // Get Exam Button Action
        getExamButton.addActionListener(e -> {
            try {
                String courseIdInput = JOptionPane.showInputDialog(teacherFrame, "Enter Course ID:");
                if (courseIdInput == null || courseIdInput.isEmpty()) {
                    JOptionPane.showMessageDialog(teacherFrame, "Course ID cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int courseId = Integer.parseInt(courseIdInput);
                String examDetails = Teacher.getExamDetails(courseId);
                JDialog examDialog = new JDialog(teacherFrame, "Exam Details", true);
                examDialog.setSize(400, 300);
                examDialog.setLocationRelativeTo(teacherFrame);
                examDialog.setLayout(new BorderLayout());
                JTextArea textArea = new JTextArea(examDetails);
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                JButton closeButton = new JButton("Close");
                closeButton.addActionListener(e1 -> examDialog.dispose());
                examDialog.add(scrollPane, BorderLayout.CENTER);
                examDialog.add(closeButton, BorderLayout.SOUTH);
                examDialog.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(teacherFrame, "Error: " + ex.getMessage());
            }
        });

        submitDegreeButton.addActionListener(e -> {
            JPanel inputPanel = new JPanel(new GridLayout(3, 2));
            JTextField examIdField = new JTextField();
            JTextField studentIdField = new JTextField();
            JTextField degreeField = new JTextField();
            inputPanel.add(new JLabel("Enter Exam ID:"));
            inputPanel.add(examIdField);
            inputPanel.add(new JLabel("Enter Student ID:"));
            inputPanel.add(studentIdField);
            inputPanel.add(new JLabel("Enter Degree:"));
            inputPanel.add(degreeField);
        
            int option = JOptionPane.showConfirmDialog(teacherFrame, inputPanel, "Submit Degree", JOptionPane.OK_CANCEL_OPTION);
        
            if (option == JOptionPane.OK_OPTION) {
                try {
                    if (examIdField.getText().isEmpty() || studentIdField.getText().isEmpty() || degreeField.getText().isEmpty()) {
                        throw new IllegalArgumentException("All fields must be filled!");
                    }
                    int examId = Integer.parseInt(examIdField.getText());
                    int studentId = Integer.parseInt(studentIdField.getText());
                    double degree = Double.parseDouble(degreeField.getText());
                    String result = Teacher.submitDegree(examId, studentId, degree);
                    JOptionPane.showMessageDialog(teacherFrame, result, "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(teacherFrame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
         

          // Notifications Button Action
          getNotificationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Fetch notifications (Example: using a method getNotifications)
                    Map<String, String> notifications = Students.getNotifications();

                    if (notifications.isEmpty()) {
                        JOptionPane.showMessageDialog(teacherFrame, "No notifications available.", "Notifications", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        StringBuilder notificationList = new StringBuilder();
                        for (Map.Entry<String, String> entry : notifications.entrySet()) {
                            notificationList.append("Title: ").append(entry.getKey()).append("\n");
                            notificationList.append("Message: ").append(entry.getValue()).append("\n\n");
                        }

                        // Display notifications
                        JOptionPane.showMessageDialog(teacherFrame, notificationList.toString(), "Notifications", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(teacherFrame, "Error fetching notifications: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        markAttendanceButton.addActionListener(e -> {
            JPanel inputPanel = new JPanel(new GridLayout(4, 2));
            JTextField courseIdField = new JTextField();
            JTextField studentIdField = new JTextField();
            JTextField dateField = new JTextField();
            JTextField statusField = new JTextField();
            inputPanel.add(new JLabel("Enter Course ID:"));
            inputPanel.add(courseIdField);
            inputPanel.add(new JLabel("Enter Student ID:"));
            inputPanel.add(studentIdField);
            inputPanel.add(new JLabel("Enter Date (YYYY-MM-DD):"));
            inputPanel.add(dateField);
            inputPanel.add(new JLabel("Enter Attendance Status (Present/Absent):"));
            inputPanel.add(statusField);
        
            int option = JOptionPane.showConfirmDialog(teacherFrame, inputPanel, "Mark Attendance", JOptionPane.OK_CANCEL_OPTION);
        
            if (option == JOptionPane.OK_OPTION) {
                try {
                    if (courseIdField.getText().isEmpty() || studentIdField.getText().isEmpty() || dateField.getText().isEmpty() || statusField.getText().isEmpty()) {
                        throw new IllegalArgumentException("All fields must be filled!");
                    }
                    int courseId = Integer.parseInt(courseIdField.getText());
                    int studentId = Integer.parseInt(studentIdField.getText());
                    String date = dateField.getText();
                    String status = statusField.getText();
                    String result = Teacher.markAttendance(courseId, studentId, date, status);
                    JOptionPane.showMessageDialog(teacherFrame, result, "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(teacherFrame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Get Enrolled Students Button Action
        getEnrolledStudentsButton.addActionListener(e -> {
            try {
                String courseId = JOptionPane.showInputDialog(teacherFrame, "Enter Course ID:");
                if (courseId == null || courseId.isEmpty()) {
                    JOptionPane.showMessageDialog(teacherFrame, "Course ID cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String enrolledStudents = Teacher.getEnrolledStudents(Integer.parseInt(courseId));
                JTextArea studentsArea = new JTextArea(enrolledStudents);
                studentsArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(studentsArea);
                JOptionPane.showMessageDialog(teacherFrame, scrollPane, "Enrolled Students", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(teacherFrame, "Error: " + ex.getMessage());
            }
        });

        // Add buttons to panel
        teacherPanel.add(addExamButton);
        teacherPanel.add(updateExamButton);
        teacherPanel.add(getExamButton);
        teacherPanel.add(submitDegreeButton);
        teacherPanel.add(getNotificationsButton);
        teacherPanel.add(markAttendanceButton);
        teacherPanel.add(getEnrolledStudentsButton);
        teacherPanel.add(profileButton); // Added to the panel
        teacherPanel.add(logoutButton); // Added to the panel

        // Configure the frame
        teacherFrame.add(teacherPanel);
        teacherFrame.setSize(400, 600); // Adjusted size to accommodate all buttons
        teacherFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        teacherFrame.setVisible(true);
    }
    public static void createAdminMenu(User loggedInUser) {
        Admin admin = new Admin();

        JFrame adminFrame = new JFrame("Admin Menu");
        JPanel adminPanel = new JPanel();
        adminPanel.setLayout(new GridLayout(11, 1)); // Adjusting the grid for 9 options

        // Creating buttons for each option
        JButton addCourseButton = new JButton("Add Course");
        JButton updateCourseButton = new JButton("Update Course");
        JButton getCoursesButton = new JButton("Get All Courses");
        JButton enrollStudentButton = new JButton("Enroll Student");
        JButton getExamDetailsButton = new JButton("Get Exam Details");
        JButton sendNotificationButton = new JButton("Send Notification");
        JButton viewAttendanceButton = new JButton("View Attendance");
        JButton updateDegreeButton = new JButton("Update Degree");
        JButton addUserButton = new JButton("Add User");
        JButton deleteUserButton = new JButton("Delete User");
        JButton viewProfileButton = new JButton("View Profile");
        JButton logOutButton = new JButton("Log Out");

        // Add Course Button Action
        addCourseButton.addActionListener(e -> {
            JPanel inputPanel = createInputPanel(new String[]{"Course Name:", "Course Description:", "Teacher ID:"});
            int result = showDialog(adminFrame, inputPanel, "Add Course");

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String name = ((JTextField) inputPanel.getComponent(1)).getText().trim();
                    String description = ((JTextField) inputPanel.getComponent(3)).getText().trim();
                    int teacherId = Integer.parseInt(((JTextField) inputPanel.getComponent(5)).getText().trim());

                    if (name.isEmpty() || description.isEmpty()) {
                        throw new IllegalArgumentException("All fields must be filled.");
                    }

                    String response = admin.addCourse(name, description, teacherId);
                    JOptionPane.showMessageDialog(adminFrame, response, "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(adminFrame, "Teacher ID must be a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(adminFrame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Update Course Button Action
        updateCourseButton.addActionListener(e -> {
            JPanel inputPanel = createInputPanel(new String[]{"Course ID:", "New Course Name:", "New Course Description:"});
            int result = showDialog(adminFrame, inputPanel, "Update Course");

            if (result == JOptionPane.OK_OPTION) {
                try {
                    int courseId = Integer.parseInt(((JTextField) inputPanel.getComponent(1)).getText().trim());
                    String name = ((JTextField) inputPanel.getComponent(3)).getText().trim();
                    String description = ((JTextField) inputPanel.getComponent(5)).getText().trim();

                    if (name.isEmpty() || description.isEmpty()) {
                        throw new IllegalArgumentException("All fields must be filled.");
                    }

                    String response = admin.updateCourse(courseId, name, description);
                    JOptionPane.showMessageDialog(adminFrame, response, "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(adminFrame, "Course ID must be a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(adminFrame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Get All Courses Button Action
        getCoursesButton.addActionListener(e -> {
            try {
                StringBuilder coursesList = new StringBuilder();
                for (String course : admin.getCourses()) {
                    coursesList.append(course).append("\n");
                }
                JOptionPane.showMessageDialog(adminFrame, coursesList.toString(), "All Courses", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        // Enroll Student Button Action
        enrollStudentButton.addActionListener(e -> {
            JPanel panel = createInputPanel(new String[]{"Course ID:", "Student ID:"});
            int option = showDialog(adminFrame, panel, "Enroll Student");

            if (option == JOptionPane.OK_OPTION) {
                try {
                    int courseId = Integer.parseInt(((JTextField) panel.getComponent(1)).getText());
                    int studentId = Integer.parseInt(((JTextField) panel.getComponent(3)).getText());
                    String result = admin.enrollStudent(courseId, studentId);
                    JOptionPane.showMessageDialog(adminFrame, result);
                } catch (SQLException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(adminFrame, "Error: " + ex.getMessage());
                }
            }
        });

        addUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createSignUpForm();
            }
        });

        // Get Exam Details Button Action
        getExamDetailsButton.addActionListener(e -> {
            JPanel panel = createInputPanel(new String[]{"Exam ID:"});
            int option = showDialog(adminFrame, panel, "Get Exam Details");

            if (option == JOptionPane.OK_OPTION) {
                try {
                    int examId = Integer.parseInt(((JTextField) panel.getComponent(1)).getText());
                    String result = admin.getExamDetails(examId);
                    JOptionPane.showMessageDialog(adminFrame, result);
                } catch (SQLException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(adminFrame, "Error: " + ex.getMessage());
                }
            }
        });

        // Send Notification Button Action
        sendNotificationButton.addActionListener(e -> {
            JPanel panel = createInputPanel(new String[]{"User ID:", "Notification Title:", "Notification Message:"});
            int option = showDialog(adminFrame, panel, "Send Notification");

            if (option == JOptionPane.OK_OPTION) {
                try {
                    int userId = Integer.parseInt(((JTextField) panel.getComponent(1)).getText());
                    String title = ((JTextField) panel.getComponent(3)).getText();
                    String message = ((JTextField) panel.getComponent(5)).getText();
                    String result = admin.sendNotification(userId, title, message);
                    JOptionPane.showMessageDialog(adminFrame, result);
                } catch (SQLException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(adminFrame, "Error: " + ex.getMessage());
                }
            }
        });

        // View Attendance Button Action
        viewAttendanceButton.addActionListener(e -> {
            JPanel panel = createInputPanel(new String[]{"Course ID:", "Student ID:"});
            int option = showDialog(adminFrame, panel, "View Attendance");

            if (option == JOptionPane.OK_OPTION) {
                try {
                    int courseId = Integer.parseInt(((JTextField) panel.getComponent(1)).getText());
                    int studentId = Integer.parseInt(((JTextField) panel.getComponent(3)).getText());
                    String result = admin.viewAttendance(courseId, studentId);
                    JOptionPane.showMessageDialog(adminFrame, result);
                } catch (SQLException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(adminFrame, "Error: " + ex.getMessage());
                }
            }
        });

        // Update Degree Button Action
        updateDegreeButton.addActionListener(e -> {
            JPanel panel = createInputPanel(new String[]{"Exam ID:", "Student ID:", "New Grade:"});
            int option = showDialog(adminFrame, panel, "Update Degree");

            if (option == JOptionPane.OK_OPTION) {
                try {
                    int examId = Integer.parseInt(((JTextField) panel.getComponent(1)).getText());
                    int studentId = Integer.parseInt(((JTextField) panel.getComponent(3)).getText());
                    int grade = Integer.parseInt(((JTextField) panel.getComponent(5)).getText());
                    String result = admin.updateGrade(examId, studentId, grade);
                    JOptionPane.showMessageDialog(adminFrame, result);
                } catch (SQLException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(adminFrame, "Error: " + ex.getMessage());
                }
            }
        });

        // Delete Student Button Action
        deleteUserButton.addActionListener(e -> {
            JPanel panel = createInputPanel(new String[]{"User ID:"});
            int option = showDialog(adminFrame, panel, "Delete User");

            if (option == JOptionPane.OK_OPTION) {
                try {
                    int studentId = Integer.parseInt(((JTextField) panel.getComponent(1)).getText());
                    String result = admin.deleteStudent(studentId);
                    JOptionPane.showMessageDialog(adminFrame, result);
                } catch (SQLException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(adminFrame, "Error: " + ex.getMessage());
                }
            }
        });

        // View Profile Button Action
        viewProfileButton.addActionListener(e -> {
            String currentProfile = "Name: " + loggedInUser.getName() + "\n" +
                    "Email: " + loggedInUser.getEmail() + "\n" +
                    "Phone: " + loggedInUser.getPhone();

            JTextArea profileArea = new JTextArea(currentProfile);
            profileArea.setEditable(false);
            JPanel profilePanel = new JPanel(new BorderLayout());
            profilePanel.add(new JScrollPane(profileArea), BorderLayout.CENTER);

            int option = showDialog(adminFrame, profilePanel, "View Profile");

            if (option == JOptionPane.OK_OPTION) {
                JTextField nameField = new JTextField(loggedInUser.getName());
                JTextField emailField = new JTextField(loggedInUser.getEmail());
                JTextField phoneField = new JTextField(loggedInUser.getPhone());

                JPanel updatePanel = new JPanel(new GridLayout(3, 2));
                updatePanel.add(new JLabel("Name:"));
                updatePanel.add(nameField);
                updatePanel.add(new JLabel("Email:"));
                updatePanel.add(emailField);
                updatePanel.add(new JLabel("Phone:"));
                updatePanel.add(phoneField);

                int updateOption = showDialog(adminFrame, updatePanel, "Update Profile");

                if (updateOption == JOptionPane.OK_OPTION) {
                    try {
                        loggedInUser.updateProfile(nameField.getText(), phoneField.getText(), emailField.getText());
                        JOptionPane.showMessageDialog(adminFrame, "Profile updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(adminFrame, "Error updating profile: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Logout Button Action
        logOutButton.addActionListener(e -> {
            try {
                loggedInUser.logout();
                JOptionPane.showMessageDialog(adminFrame, "Logged out successfully!");
                adminFrame.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(adminFrame, "Error: " + ex.getMessage());
            }
        });

        // Adding buttons to the panel
        adminPanel.add(addCourseButton);
        adminPanel.add(updateCourseButton);
        adminPanel.add(getCoursesButton);
        adminPanel.add(enrollStudentButton);
        adminPanel.add(getExamDetailsButton);
        adminPanel.add(sendNotificationButton);
        adminPanel.add(viewAttendanceButton);
        adminPanel.add(updateDegreeButton);
        adminPanel.add(addUserButton);
        adminPanel.add(deleteUserButton);
        adminPanel.add(viewProfileButton);
        adminPanel.add(logOutButton);

        adminFrame.add(adminPanel);
        adminFrame.setSize(300, 500);
        adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        adminFrame.setVisible(true);
    }

    // Utility method to create input panel
    private static JPanel createInputPanel(String[] labels) {
        JPanel panel = new JPanel(new GridLayout(labels.length, 2));
        for (String label : labels) {
            panel.add(new JLabel(label));
            panel.add(new JTextField());
        }
        return panel;
    }

    // Utility method to show dialog
    private static int showDialog(JFrame parentFrame, JPanel panel, String title) {
        return JOptionPane.showConfirmDialog(parentFrame, panel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    }
}