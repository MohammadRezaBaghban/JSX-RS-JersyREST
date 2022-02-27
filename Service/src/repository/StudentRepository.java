package repository;

import model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentRepository {

    int count();

    Collection<Student> getAll();

    Student getById(int id);

    List<Student> filterStudentsByName(String name);

    Student getStudentByIndex(int index);

    void deleteById(int studentId);
}
