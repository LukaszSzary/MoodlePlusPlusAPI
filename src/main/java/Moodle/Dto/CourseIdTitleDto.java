package Moodle.Dto;

import Moodle.Model.Courses;
import lombok.Data;

@Data
public class CourseIdTitleDto {
   private Integer id;
   private String title;
   public CourseIdTitleDto(Courses course){
       this.id = course.getId();
       this.title =course.getTitle();
   }

}
