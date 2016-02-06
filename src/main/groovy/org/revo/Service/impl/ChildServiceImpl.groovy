package org.revo.Service.impl

import org.revo.Repository.ChildRepository
import org.revo.Repository.PersonRepository
import org.revo.Repository.SuggestedChildRepository
import org.revo.Service.ChildService
import org.revo.Service.PersonService
import org.revo.domain.Child
import org.revo.domain.SuggestedChild
import org.revo.model.uids
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by ashraf on 2/5/2016.
 */
@Service
class ChildServiceImpl implements ChildService {
    @Autowired
    ChildRepository childRepository
    @Autowired
    PersonService personService

    @Transactional
    @Override
    Child save(Child child) {
        child.person = personService.findCurrent()
        childRepository.save(child)
    }

    @Transactional
    @Override
    Child addUid(long l, String s) {
        def one = childRepository.findOne(l)
        one.uid = s
        childRepository.save(one)
    }

    @Override
    Child findOne(long l) {
        childRepository.findOne(l)
    }

    @Override
    List<Child> findAllByUid(List<String> uuid) {
        childRepository.findByUidIn(uuid)
    }

    @Override
    List<Child> findAll() {
        childRepository.findAll()
    }

    @Transactional
    @Override
    Child updateSuggestedChild(long l, List<uids> uidses) {
        List<Child> children = findAllByUid(uidses*.uid)
        Child child = findOne(l)
        children.findAll {
            !Contains(child.suggestedChild*.suggested.toSet(), it)
        }.each {
            child.suggestedChild << suggestedChildRepository.save(new SuggestedChild(confidence: uidses.find { u -> u.uid == it.uid }.confidence, suggested: it, child: child))
        }
        childRepository.save(child)
    }

    private boolean Contains(Set<Child> data, Child one) {
        data.any {
            it.id == one.id
        }
    }
    @Autowired
    SuggestedChildRepository suggestedChildRepository
}
