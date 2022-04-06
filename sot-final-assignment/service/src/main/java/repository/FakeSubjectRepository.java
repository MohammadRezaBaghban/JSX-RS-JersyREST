package repository;

import model.Book;
import model.Subject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FakeSubjectRepository implements ISubjectRepository {

    private static FakeSubjectRepository instance = new FakeSubjectRepository();
    private final List<Subject> subjects = new ArrayList<>();

    private FakeSubjectRepository() {
        subjects.add(new Subject("Mathematics"));
        subjects.add(new Subject("Computer Science"));
        subjects.add(new Subject("Natural Science"));
        subjects.add(new Subject("Theology"));
        subjects.add(new Subject("Sport Science"));
        subjects.add(new Subject("Mathematics"));
        subjects.add(new Subject("Biology"));
        subjects.add(new Subject("Medical"));
    }

    public static FakeSubjectRepository getInstance() {
        return instance;
    }

    @Override
    public int count() {
        return subjects.size();
    }

    @Override
    public Collection<Subject> getAll() {
        return subjects;
    }

    @Override
    public Subject getById(int id) {
        for (Subject subject : subjects) {
            if (subject.getId() == id) return subject;
        }
        return null;
    }

    @Override
    public List<Subject> filterSubjectByName(String subjectName) {
        List<Subject> subjectsResult = new ArrayList<>();
        System.out.println(subjectName);
        for (Subject subject : subjects) {
            if (subject.getName().equals(subjectName))
                subjectsResult.add(subject);
        }
        return subjectsResult;
    }

    @Override
    public Subject getSubjectByIndex(int index) {
        if (subjects.size() < 1 || index < 0) return null;
        else return subjects.get(index);
    }

    @Override
    public void deleteById(int bookId) {
        for (Subject subject : subjects) {
            if (subject.getId() == bookId) {
                subjects.remove(subject);
                break;
            }
        }
    }

    @Override
    public Subject add(Subject subject) {
        var subjectObj = new Subject(subject.getName());
        subjects.add(subjectObj);
        return subjectObj;
    }

    @Override
    public void update(int subjectId, Subject subject) {
        var subjectObject = this.getById(subjectId);
        if (subjectObject != null){
            subjectObject.setName(subject.getName());
        }
    }
}
