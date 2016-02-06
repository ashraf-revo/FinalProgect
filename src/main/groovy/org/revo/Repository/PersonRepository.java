package org.revo.Repository;

import org.revo.domain.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

/**
 * Created by revo on 20/10/15.
 */
@RepositoryRestResource
public interface PersonRepository extends CrudRepository<Person, Long> {
    Optional<Person> findByEmail(String email);
}
