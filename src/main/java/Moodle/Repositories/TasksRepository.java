package Moodle.Repositories;

import Moodle.Model.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TasksRepository extends JpaRepository<Tasks,Long> {
}
