package org.revo.Repository;

import org.revo.domain.Child;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by revo on 20/10/15.
 */
@RepositoryRestResource
public interface ChildRepository extends CrudRepository<Child, Long> {
    List<Child> findAll();

    List<Child> findByStateFalse(Pageable pageable);

    List<Child> findByUidIn(List<String> uids);

    Long countByStateTrue();

    Long countByStateFalse();
}