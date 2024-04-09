package Moodle.Services;

import Moodle.Dto.CourseDto;
import Moodle.Model.Courses;
import Moodle.Model.Users;
import Moodle.Repositories.CoursesRepository;
import Moodle.Repositories.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseService {
    private final CoursesRepository coursesRepository;
    private final UsersRepository usersRepository;

    public CourseService(CoursesRepository repository, UsersRepository usersRepository) {
        this.coursesRepository = repository;
        this.usersRepository = usersRepository;
    }
        public Courses addCourse(CourseDto courseDto, Users use){

        Courses course =new Courses();
        course.setTitle(courseDto.getTitle());
        Users user =usersRepository.findByMail(use.getMail()).get();
            course = coursesRepository.save(course);

        user.addCourseToOwned(course);
        usersRepository.save(user);

        System.out.println(user);

        return course;
    }

    public Courses updateCourse(int id, CourseDto courseDto, Users authenticatedUser) throws Exception {
        Courses course = coursesRepository.getReferenceById(id);
        if(course==null){
            throw new NullPointerException("There is no such course, bad id");
        }
        System.out.println(authenticatedUser);
        if(!authenticatedUser.getCourses_owned().contains(course)){
            throw new Exception("This user can not change this course");
        }
        course.setTitle(courseDto.getTitle());
        coursesRepository.save(course);
        return course;
    }
}
