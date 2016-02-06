package org.revo.Service

import org.revo.domain.Child
import org.revo.model.uids

/**
 * Created by ashraf on 2/5/2016.
 */
interface ChildService {
    Child save(Child child)

    Child addUid(long l, String s)

    Child findOne(long l)

    List<Child> findAllByUid(List<String> uuid)

    List<Child> findAll()

    Child updateSuggestedChild(long l, List<uids> uidses)
}