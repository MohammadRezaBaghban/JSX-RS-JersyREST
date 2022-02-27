package repository;

import model.Student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FakeStudentRepository implements StudentRepository {

    private final List<Student> students = new ArrayList<>();

    public FakeStudentRepository(){
        students.add(new Student(1,"Joe smith"));
        students.add(new Student(2,"Ann Johnsson"));
        students.add(new Student(3,"Jnn Pereson"));
        students.add(new Student(4,"Mohammad Baghban"));
        students.add(new Student(5,"Miranda Winslet"));
    }

    @Override
    public int count() { return students.size(); }

    @Override
    public Collection<Student> getAll() { return students; }

    @Override
    public Student get(int nr) {
        return null;
    }

    @Override
    public List<Student> filter(String name) {
        return students;
    }
}
