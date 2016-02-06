package org.revo.model

import groovy.transform.Canonical
import org.revo.controller.Main

/**
 * Created by revo on 04/11/15.
 */
@Canonical
class SearchCriteria {
    String searchTxt
    String className
    int firstResult = 0
    int maxResults = 10
    Map<String, List<String>> tempFildes = ["child" : ["name", "moreInfo", "age", "addresses.locationName", "currentPalce.locationName"],
                                            "person": ["name", "email", "moreInfo", "phone", "addresses.locationName"]]

    String[] getTempFildes() {
        tempFildes[className].toArray(String[])
    }
}
