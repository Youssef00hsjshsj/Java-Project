import java.util.List;

public class Courses {
    private int id;
    private String name;
    private String description;
    private int teacherId;
    private List<Integer> students; // List of student IDs

    public Courses(int id, String name, String description, int teacherId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.teacherId = teacherId;
    }

    // Methods
    public void addCourse(String courseDetails) { /* Add a new course */ }
    public void updateCourse(String courseDetails) { /* Update course details */ }
    public List<String> getCourses(int courseId) { /* Fetch courses */ return null; }
    public List<Integer> getCoursesForStudent(int studentId) { /* Courses for a student */ return null; }
}
