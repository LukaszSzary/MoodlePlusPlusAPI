package Moodle.Services;

import Moodle.Dto.CourseDto;
import Moodle.Model.Courses;
import Moodle.Model.Role;
import Moodle.Model.Users;
import Moodle.Repositories.CoursesRepository;
import Moodle.Repositories.UsersRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public Courses addCourse(CourseDto courseDto, Users user){

        Courses course =new Courses();
        course.setTitle(courseDto.getTitle());
        course.getCourse_owners().add(user);
        coursesRepository.save(course);
        return course;
    }

    public Courses updateCourse(Courses course, Users authenticatedUser) throws Exception {
        Courses courseUpdated = coursesRepository.findById(course.getId()).get();
        if(courseUpdated==null){
            throw new NullPointerException("There is no such course, bad id");
        }
//        System.out.println(authenticatedUser);
        if(!courseUpdated.getCourse_owners().contains(authenticatedUser)){
            throw new Exception("This user can not change this course");
       }
        courseUpdated.setTitle(course.getTitle());
        coursesRepository.save(courseUpdated);
        return courseUpdated;
    }

    public boolean deleteCourse(int id, Users authenticatedUser) {
        if(coursesRepository.findById(id).get().getCourse_owners().contains(authenticatedUser)){
            coursesRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean addTutorToCourse(int id, Users user, Users authenticatedUser) throws Exception {
        Courses course = coursesRepository.findById(id).orElseThrow(()->new Exception("Course with provided id can't be found"));

        if(!(course.getCourse_owners().contains(authenticatedUser) || authenticatedUser.getRole()== Role.admin)){
            throw new Exception("Privileges not sufficient");
        }

        if(usersRepository.existsByIdAndNameAndSurnameAndMail(user.getId(), user.getName(), user.getSurname(), user.getMail()) && (user.getRole()==Role.tutor || user.getRole()==Role.admin)){
            course.getCourse_owners().add(user);
            coursesRepository.save(course);
            return true;
        }
        else {
            throw new Exception("User does not exist or is nighter admin nor tutor");
        }
    }
}
