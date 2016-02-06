package org.revo.domain

import groovy.transform.Canonical
import javax.persistence.Embeddable

/**
 * Created by revo on 19/10/15.
 */
@Embeddable
@Canonical
class Location {
    Double x
    Double y
    String locationName
}
