package org.revo.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import groovy.transform.Canonical
import org.hibernate.annotations.Type
import org.hibernate.validator.constraints.Email
import org.hibernate.validator.constraints.NotEmpty
import org.hibernate.validator.constraints.URL

import javax.persistence.*

/**
 * Created by revo on 19/10/15.
 */

@Entity
class Person {
    @Id
    @GeneratedValue
    Long id
    @Column(length = 40)
    @NotEmpty
    String name
    @Column(length = 40)
    @Email
    @NotEmpty
    String email
    @Column(length = 60)
    @NotEmpty
    @JsonIgnore
    String password
    @Column(length = 12)
    String phone
    @URL
    @Column(length = 70)
    String image
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    String moreInfo
    @Embedded
    @AttributeOverrides([
            @AttributeOverride(name = "x", column = @Column(name = "addresses_x")),
            @AttributeOverride(name = "y", column = @Column(name = "addresses_y")),
            @AttributeOverride(name = "locationName", column = @Column(name = "addresses_locationName", length = 50))
    ])
    Location addresses
    @Embedded
    @AttributeOverrides([
            @AttributeOverride(name = "x", column = @Column(name = "location_x")),
            @AttributeOverride(name = "y", column = @Column(name = "location_y")),
            @AttributeOverride(name = "locationName", column = @Column(name = "location_locationName", length = 50))
    ])
    Location location
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "person")
    Set<Child> children
    @Column(length = 12)
    @NotEmpty
    String queueName
}
