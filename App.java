import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Scanner;

public class App {

    static int id;
    static int choice;
    static Scanner input = new Scanner(System.in);

    // Function to display a prompt and wait for user input
    static void pressAnyKeyToReturn() {
        System.out.println("\nPress any key to return to the menu...");
        input.nextLine();  // Wait for user to press any key
    }

    public class TeacherInterface {
        static Scanner input = new Scanner(System.in);
        static int choice;

        public static void main(String[] args) {
            try {
                teacherInterface();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }

        static void teacherInterface() throws ClassNotFoundException, SQLException {
            boolean continueMenu = true; // Control variable for the loop to handle menu options

            while (continueMenu) {
                System.out.println("\nTeacher Menu:");
                System.out.println("1- Add Exam");
                System.out.println("2- Update Exam");
                System.out.println("3- Get Exam");
                System.out.println("4- Submit Degree");
                System.out.println("5- Get Notifications");
                System.out.println("6- Mark Attendance");
                System.out.println("7- Get Enrolled Students");
                System.out.println("8- Exit");

                System.out.print("\nEnter Your Choice: ");
                choice = input.nextInt();
                input.nextLine(); // Clear the buffer

                switch (choice) {
                    case 1: // Add Exam
// Example for Adding an Exam
                        System.out.print("Enter Course ID: ");
                        int courseID = input.nextInt();
                        input.nextLine(); // Clear the buffer
                        System.out.print("Enter Exam Name: ");
                        String examName = input.nextLine();
                        System.out.print("Enter Exam Date (YYYY-MM-DD): ");
                        String date = input.nextLine();

                        Exam.addExam(courseID, examName, date);                        break;
                    case 2: // Update Exam
                        System.out.print("Enter Exam ID: ");
                        int examId = input.nextInt();
                        input.nextLine(); // Clear the buffer
                        System.out.print("Enter New Exam Name: ");
                        String newExamName = input.nextLine();
                        System.out.print("Enter New Exam Date (YYYY-MM-DD): ");
                        String newDate = input.nextLine();

                        // Calling static method of Exam to update the exam
                        Exam.updateExam(examId, newExamName, newDate);                         break;
                    case 3: // Get Exam
                        System.out.print("Enter Exam ID to Retrieve Details: ");
                        int getExamId = input.nextInt();

                        // Calling the getExamDetails function from the Exam class
                        Exam.getExamDetails(getExamId);                         break;
                    case 4: // Submit Degree
                        System.out.print("Enter Student ID: ");
                        int studentId = input.nextInt();

                        System.out.print("Enter Exam ID: ");
                         examId = input.nextInt();

                        System.out.print("Enter Course ID: ");
                        int courseId = input.nextInt();

                        input.nextLine();  // Clear the buffer

                        System.out.print("Enter Grade (Degree): ");
                        String grade = input.nextLine();  // Can be letter grade (A, B) or numeric value (80, 90)

                        // Call the submitDegree method to submit the grade for the student
                        Exam.submitDegree(studentId, examId, courseId, grade);                        break;
                    case 5: // Get Notifications
                        System.out.print("Enter Teacher ID: ");

                        try {
                            // Fetch and display notifications for this teacher
                            Notification.getNotifications();
                        } catch (SQLException | ClassNotFoundException e) {
                            System.out.println("Error retrieving notifications: " + e.getMessage());
                        }                        break;
                    case 6: // Mark Attendance
                        // Prompting teacher to input the relevant attendance information
                        try {
                           Attendance.markStudentAttendance();
                        } catch (SQLException | ClassNotFoundException e) {
                            System.out.println("Error marking attendance: " + e.getMessage());
                        }
                        break;
                    case 7: // Get Enrolled Students
                        getEnrolledStudents();
                        break;
                    case 8: // Exit
                        continueMenu = false;
                        System.out.println("Exiting Teacher Menu.");
                        break;
                    default:
                        System.out.println("Feature under development or invalid choice.");
                }

                if (continueMenu) {
                    pressAnyKeyToReturn();  // Wait for user to press any key
                }
            }
        }




        // Get Enrolled Students Function
        static void getEnrolledStudents() throws SQLException, ClassNotFoundException {
            System.out.print("Enter Your Teacher ID: ");
            int teacherId = input.nextInt(); // Retrieve dynamically or from authentication

            System.out.print("Enter Course ID: ");
            int courseId = input.nextInt();

            Connection connection = DatabaseConnection.getDatabaseConnection();
            String sql = "SELECT u.name, u.email FROM users u JOIN student_enrolled se ON u.id = se.student_id WHERE se.course_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("Enrolled Students:");
            if (!rs.next()) {
                System.out.println("No students enrolled.");
            } else {
                do {
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    System.out.println("Name: " + name + ", Email: " + email);
                } while (rs.next());
            }

            connection.close();
        }

        // Utility method to wait for user to press any key
        static void pressAnyKeyToReturn() {
            System.out.println("\nPress Enter to return to the menu...");
            try {
                System.in.read();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static void loginInterface() throws ClassNotFoundException, SQLException {
        System.out.print("\nEnter Your Email: ");
        String email = input.next();

        System.out.print("Enter Your Password: ");
        String password = input.next();

        User user = User.authenticate(email, password);

        if (user == null ) {
            System.out.println("This user was not found.");
        } else {
            int roleId = (int) user.getRoleId();

            if (roleId == 1) {
                studentInterface();
            } else if (roleId == 2) {
                TeacherInterface.teacherInterface();
            } else if (roleId == 3) { // Assuming 3 is for Admin
                adminInterface();
            } else {
                System.out.println("Invalid role or under development.");
            }
        }
    }

    static void adminInterface() throws ClassNotFoundException, SQLException {
        System.out.println("\nAdmin Menu:");
        System.out.println("1- Add Course");
        System.out.println("2- Update Course");
        System.out.println("3- Get All Courses");
        System.out.println("4- Enroll Student");
        System.out.println("5- Get Exam Details");
        System.out.println("6- Send Notification");
        System.out.println("7- View Attendance");
        System.out.println("8- Update Degree");
        System.out.println("9- Delete Student");

        System.out.print("\nEnter Your Choice: ");
        choice = input.nextInt();
        input.nextLine(); // Clear buffer

        Admin admin = new Admin();

        switch (choice) {
            case 1:
                System.out.print("Enter Course Name: ");
                String courseName = input.nextLine();
                System.out.print("Enter Course Description: ");
                String courseDescription = input.nextLine();
                System.out.print("Enter Teacher ID: ");
                int teacherId = input.nextInt();
                admin.addCourse(courseName, courseDescription, teacherId);
                break;
            case 2:
                System.out.print("Enter Course ID: ");
                int courseId = input.nextInt();
                input.nextLine(); // Clear buffer
                System.out.print("Enter New Course Name: ");
                String newCourseName = input.nextLine();
                System.out.print("Enter New Course Description: ");
                String newCourseDescription = input.nextLine();
                admin.updateCourse(courseId, newCourseName, newCourseDescription);
                break;
            case 3:
                admin.getCourses().forEach(System.out::println);
                break;
            case 4:
                System.out.print("Enter Course ID: ");
                int enrollCourseId = input.nextInt();
                System.out.print("Enter Student ID: ");
                int enrollStudentId = input.nextInt();
                admin.enrollStudent(enrollCourseId, enrollStudentId);
                break;
            case 5:
                System.out.print("Enter Exam ID: ");
                int examId = input.nextInt();
                admin.getExamDetails(examId);
                break;
            case 6:
                System.out.print("Enter User ID: ");
                int userId = input.nextInt();
                input.nextLine(); // Clear buffer
                System.out.print("Enter Notification Title: ");
                String title = input.nextLine();
                System.out.print("Enter Notification Message: ");
                String message = input.nextLine();
                admin.sendNotification(userId, title, message);
                break;
            case 7:
                System.out.print("Enter Course ID: ");
                int attendanceCourseId = input.nextInt();
                System.out.print("Enter Student ID: ");
                int attendanceStudentId = input.nextInt();
                admin.viewAttendance(attendanceCourseId, attendanceStudentId);
                break;
            case 8:
                System.out.print("Enter Exam ID: ");
                int updateExamId = input.nextInt();
                System.out.print("Enter Student ID: ");
                int updateStudentId = input.nextInt();
                System.out.print("Enter New Grade: ");
                int newGrade = input.nextInt();
                admin.updateGrade(updateExamId, updateStudentId, newGrade);
                break;
            case 9:
                System.out.print("Enter Student ID: ");
                int deleteStudentId = input.nextInt();
                admin.deleteStudent(deleteStudentId);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }

        pressAnyKeyToReturn();  // Wait for user to press any key
        adminInterface();        // Return to admin menu
    }


    static void signUpInterface() throws ClassNotFoundException, SQLException {
        System.out.println("\nSign-Up Menu:");
        System.out.println("1- Student");
        System.out.println("2- Teacher");
        System.out.println("3-  Admin");
        System.out.print("\nEnter Your Role Choice: ");
        choice = input.nextInt();

        input.nextLine(); // Clear the buffer

        System.out.print("Enter Your Name: ");
        String name = input.nextLine();

        System.out.print("Enter Your Phone: ");
        String phone = input.nextLine();

        System.out.print("Enter Your Email (This will be your Username): ");
        String email = input.nextLine();

        System.out.print("Enter Your Password: ");
        String password = input.nextLine();

        User user = new User(name, email, phone, password, choice, true);
        user.register();

        if (choice == 1) {
            studentInterface();
        } else if (choice == 2) {
            TeacherInterface.teacherInterface();
        } else if (choice == 3) { // Assuming 3 is for Admin
            adminInterface();
        } else {
            System.out.println("Invalid role or under development.");
        }
    }

    static void studentInterface() throws ClassNotFoundException, SQLException {
        System.out.println("\nStudent Menu:");
        System.out.println("1- Get Report Card");
        System.out.println("2- Get Notifications");
        System.out.println("3- Get Courses");

        System.out.print("\nEnter Your Choice: ");
        choice = input.nextInt();

        switch (choice) {
            case 1: // Get Report Card
                System.out.print("Enter Your Student ID: ");
                int studentId = input.nextInt();
                Degrees.getReportCard(studentId);
                break;
            default:
                System.out.println("Feature under development or invalid choice.");
        }

        pressAnyKeyToReturn();  // Wait for user to press any key
        studentInterface();      // Return to student menu
    }

    public static void main(String[] args) {
        boolean exit = false;

        while (!exit) {
            System.out.println("\nWelcome to the System!");
            System.out.println("1- Login");
            System.out.println("2- Sign Up");
            System.out.println("3- Exit");

            System.out.print("\nEnter Your Choice: ");
            if (input.hasNextInt()) {
                choice = input.nextInt();

                switch (choice) {
                    case 1:
                        try {
                            loginInterface();
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                    case 2:
                        try {
                            signUpInterface();
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                    case 3:
                        exit = true;
                        System.out.println("Exiting the system. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                input.next(); // Clear invalid input
            }
        }

        input.close(); // Close the Scanner
    }
}
