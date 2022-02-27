package repository;

import model.Student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FakeStudentRepository implements StudentRepository {

    private final List<Student> students = new ArrayList<>();

    public FakeStudentRepository() {
        students.add(new Student(1, "Joe smith"));
        students.add(new Student(2, "Ann Johnsson"));
        students.add(new Student(3, "Jnn Pereson"));
        students.add(new Student(4, "Mohammad Baghban"));
        students.add(new Student(5, "Miranda Winslet"));
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
            if (student.getName() == name)
                studentsResult.add(student);
        }
        return studentsResult;
    }


}
