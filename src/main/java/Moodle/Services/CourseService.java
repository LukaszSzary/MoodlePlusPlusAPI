package Moodle.Services;

import Moodle.Dto.CourseDto;
import Moodle.Dto.CourseIdTitleDto;
import Moodle.Model.Courses;
import Moodle.Model.Role;
import Moodle.Model.Tasks;
import Moodle.Model.Users;
import Moodle.Repositories.CoursesRepository;
import Moodle.Repositories.UsersRepository;
import Moodle.Security.StorageProperties;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {
    private final CoursesRepository coursesRepository;
    private final UsersRepository usersRepository;
    private final StorageProperties storageProperties;

    public CourseService(CoursesRepository repository, UsersRepository usersRepository, StorageProperties storageProperties) {
        this.coursesRepository = repository;
        this.usersRepository = usersRepository;
        this.storageProperties = storageProperties;
    }
    public Courses addCourse(CourseDto courseDto, Users user) throws Exception{

        Courses course =new Courses();
        course.setTitle(courseDto.getTitle());
        course.getCourse_owners().add(user);
        Files.createDirectory(Paths.get(storageProperties.getRootLocation()+File.separator+courseDto.getTitle()));
        coursesRepository.save(course);
        return course;
    }

    public Courses updateCourse(Courses course, Users authenticatedUser) throws Exception {
        Courses courseUpdated = coursesRepository.findById(course.getId()).orElseThrow(()->new Exception("There is no such course"));
        if(courseUpdated==null){
            throw new NullPointerException("There is no such course, bad id");
        }
//        System.out.println(authenticatedUser);
        if(!courseUpdated.getCourse_owners().contains(authenticatedUser)){
            throw new Exception("This user can not change this course");
       }
        Files.move(Paths.get(storageProperties.getRootLocation()+File.separator+courseUpdated.getTitle()),Paths.get(storageProperties.getRootLocation()+File.separator+course.getTitle()));
        courseUpdated.setTitle(course.getTitle());

        coursesRepository.save(courseUpdated);
        return courseUpdated;
    }

    public boolean deleteCourse(int id, Users authenticatedUser) throws Exception {
        Courses course = coursesRepository.findById(id).orElseThrow(()->new Exception("Task does not exists"));
        if(!course.getCourse_owners().contains(authenticatedUser)){
            throw new Exception("You are not the owner of course that contains this course");
        }
        File courseDir = new File(storageProperties.getRootLocation()+File.separator+course.getTitle());

        deleteContents(courseDir);

        coursesRepository.delete(course);
        return courseDir.delete();
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
    public boolean removeTutorFromCourse(int courseId, int userId, Users authenticatedUser) throws Exception {
        Courses course = coursesRepository.findById(courseId).orElseThrow(()->new Exception("Course with provided id can't be found"));

        if(!(course.getCourse_owners().contains(authenticatedUser) || authenticatedUser.getRole()== Role.admin)){
            throw new Exception("Privileges not sufficient");
        }

        Users user = usersRepository.findById(userId).orElseThrow(()->new Exception("User does not exist"));
        if(course.getCourse_owners().contains(user)){
            course.getCourse_owners().remove(user);
            coursesRepository.save(course);
        }

        return true;
    }

    public boolean addStudentToCourse(int id, Users user, Users authenticatedUser) throws Exception {
        Courses course = coursesRepository.findById(id).orElseThrow(()->new Exception("Course with provided id can't be found"));

        if(!(course.getCourse_owners().contains(authenticatedUser) || authenticatedUser.getRole()== Role.admin)){
            throw new Exception("Privileges not sufficient");
        }

        if(usersRepository.existsByIdAndNameAndSurnameAndMail(user.getId(), user.getName(), user.getSurname(), user.getMail())){
            course.getCourse_students().add(user);
            coursesRepository.save(course);
            return true;
        }
        else {
            throw new Exception("User does not exist");
        }
    }
    public boolean removeStudentFromCourse(int courseId, int userId, Users authenticatedUser) throws Exception {
        Courses course = coursesRepository.findById(courseId).orElseThrow(()->new Exception("Course with provided id can't be found"));

        if(!(course.getCourse_owners().contains(authenticatedUser) || authenticatedUser.getRole()== Role.admin)){
            throw new Exception("You have to be course owner or admin");
        }

        Users user = usersRepository.findById(userId).orElseThrow(()->new Exception("User does not exist"));
        if(course.getCourse_students().contains(user)){
            course.getCourse_students().remove(user);
            coursesRepository.save(course);
        }

        return true;
    }

    public List<Courses> getAllCourses() {
        return coursesRepository.findAll();
    }

    public Courses getCourse(int id) throws Exception{
        return coursesRepository.findById(id).orElseThrow(()->new Exception("There is no course with this id"));
    }

    public List<Courses> getAllUserCourses(Users user) {
        List<Courses> courses = coursesRepository.findAll().stream().filter(a->a.getCourse_students().contains(user)).toList();
//        List<CourseIdTitleDto> outputCourses = new ArrayList<>();
//        for (Courses c:courses) {
//            if(c.getCourse_students().contains(user) || c.getCourse_owners().contains(user)) {
//                outputCourses.add(new CourseIdTitleDto(c));
//            }
//        }
        return courses;
    }
    private void deleteContents(File courseDir){
        String[]entries = courseDir.list();
        for(String s: entries){
            File currentFile = new File(courseDir.getPath(),s);
            if(currentFile.isDirectory()){
                deleteContents(currentFile);
            }
            currentFile.delete();
        }
    }
}
