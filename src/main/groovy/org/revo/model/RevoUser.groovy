package org.revo.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.revo.domain.Person
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.social.security.SocialUser

/**
 * Created by ashraf on 2/4/2016.
 */
@JsonIgnoreProperties(["id", "password", "authorities", "accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled"])
class RevoUser extends SocialUser {
    Long id
    String email

    RevoUser(Person person) {
        super(person.email, person.password, AuthorityUtils.createAuthorityList("ROLE_USER"))
        this.id = person.id
        this.email = person.email
    }
}
