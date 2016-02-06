package org.revo.domain

import com.fasterxml.jackson.annotation.JsonView
import org.revo.model.View

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToOne

/**
 * Created by ashraf on 2/6/2016.
 */
@Entity
class SuggestedChild {
    @Id
    @GeneratedValue
    @JsonView(View.onlyMe)
    Long id
    @JsonView(View.onlyMe)
    int confidence
    @OneToOne
    @JoinColumn
    @JsonView(View.onlyMe)
    Child suggested
    @ManyToOne
    @JoinColumn
    @JsonView(View.onlyMe)
    Child child
}
