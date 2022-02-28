package repository;

import model.Student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FakeStudentRepository implements StudentRepository {

    private static FakeStudentRepository instance = new FakeStudentRepository();
    private final List<Student> students = new ArrayList<>();
    private int id = 0;
    private FakeStudentRepository() {
        students.add(new Student(id++, "Joe smith"));
        students.add(new Student(id++, "Ann Johnsson"));
        students.add(new Student(id++, "Jnn Pereson"));
        students.add(new Student(id++, "Mohammad Baghban"));
        students.add(new Student(id++, "Miranda Winslet"));
    }

    public static FakeStudentRepository getInstance() {
        return instance;
    }

    public Student getStudentByIndex(int index) {
        if (students.size() < 1 || index < 0) return null;
        else return students.get(0);
    }

    @Override
    public void deleteById(int studentId) {
        for (Student student : students) {
            if (student.getId() == studentId) {
                students.remove(student);
                break;
            }
        }
    }

    @Override
    public Student add(String name) {
        var student = new Student(id++,name);
        students.add(student);
        return student;
    }

    @Override
    public int count() {
        return students.size();
    }

    @Override
    public Collection<Student> getAll() {
        return students;
    }

    @Override
    public Student getById(int nr) {
        for (Student student : students) {
            if (student.getId() == nr) return student;
        }
        return null;
    }

    @Override
    public List<Student> filterStudentsByName(String name) {
        List<Student> studentsResult = new ArrayList<>();
        for (Student student : students) {
            if (student.getName() == name) studentsResult.add(student);
        }
        return studentsResult;
    }


}
