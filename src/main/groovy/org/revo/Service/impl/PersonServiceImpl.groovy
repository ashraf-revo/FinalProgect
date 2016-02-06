package org.revo.Service.impl

import org.revo.Repository.PersonRepository
import org.revo.Service.PersonService
import org.revo.Service.SecurityService
import org.revo.domain.Person
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by ashraf on 2/5/2016.
 */
@Service
class PersonServiceImpl implements PersonService {
    @Autowired
    PersonRepository personRepository
    @Autowired
    SecurityService securityService

    @Override
    Person findCurrent() {
        return personRepository.findOne(securityService.GetRevoUser().id);
    }
}
