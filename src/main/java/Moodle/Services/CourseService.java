package Moodle.Services;

import Moodle.Repositories.CoursesRepository;
import org.springframework.stereotype.Service;

@Service
public class CourseService {
    private final CoursesRepository repository;


    public CourseService(CoursesRepository repository) {
        this.repository = repository;
    }
}
