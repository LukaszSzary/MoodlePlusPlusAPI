package Moodle.Repositories;

import Moodle.Model.Files;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface FilesRepository extends JpaRepository<Files,Long> {
}
