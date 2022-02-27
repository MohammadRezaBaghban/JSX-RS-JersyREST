package repository;

import model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentRepository {

    int count();

    Collection<Student> getAll();

    Student get(int nr);

    List<Student> filter(String name);
}
