package org.revo.domain

import com.fasterxml.jackson.annotation.JsonView
import org.hibernate.annotations.Type
import org.hibernate.validator.constraints.NotEmpty
import org.hibernate.validator.constraints.URL
import org.revo.model.View
import org.springframework.web.multipart.MultipartFile

import javax.persistence.*

/**
 * Created by revo on 19/10/15.
 */
@Entity
class Child {
    @Id
    @GeneratedValue
    @JsonView(View.onlyMe)
    Long id
    @Column(length = 40)
    @NotEmpty
    @JsonView(View.onlyMe)
    String name
    @URL
    @Column(length = 110)
    @JsonView(View.onlyMe)
    String image
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @JsonView(View.onlyMe)
    String moreInfo
    @Embedded
    @AttributeOverrides([
            @AttributeOverride(name = "x", column = @Column(name = "addresses_x")),
            @AttributeOverride(name = "y", column = @Column(name = "addresses_y")),
            @AttributeOverride(name = "locationName", column = @Column(name = "addresses_locationName", length = 50))
    ])
    @JsonView(View.onlyMe)
    Location addresses
    @Embedded
    @AttributeOverrides([
            @AttributeOverride(name = "x", column = @Column(name = "currentPalce_x")),
            @AttributeOverride(name = "y", column = @Column(name = "currentPalce_y")),
            @AttributeOverride(name = "locationName", column = @Column(name = "currentPalce_locationName", length = 50))
    ])
    @JsonView(View.onlyMe)
    Location currentPalce
    @ManyToOne
    @JoinColumn
    Person person
    @JsonView(View.onlyMe)
    boolean state
    @JsonView(View.onlyMe)
    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "child")
    Set<SuggestedChild> suggestedChild = new HashSet<>()
    @JsonView(View.onlyMe)
    boolean gender
    @JsonView(View.onlyMe)
    int age
    @Column(length = 50)
    @JsonView(View.onlyMe)
    String uid
    @JsonView(View.onlyMe)
    boolean founded
    @JsonView(View.onlyMe)
    Date date = new Date()
    @Transient
    MultipartFile file
}
