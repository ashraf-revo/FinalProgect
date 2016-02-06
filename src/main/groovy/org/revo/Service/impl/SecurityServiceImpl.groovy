package org.revo.Service.impl

import org.revo.Service.SecurityService
import org.revo.model.RevoUser
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by revo on 05/12/15.
 */
@Service
@Transactional
class SecurityServiceImpl implements SecurityService {


    RevoUser GetRevoUser() throws Exception {
        Authentication authentication = SecurityContextHolder.context.authentication
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof RevoUser) {
                return authentication.principal as RevoUser
            } else {
                throw new Exception("not login user")
            }
        } else throw new Exception("not login user")
    }

}