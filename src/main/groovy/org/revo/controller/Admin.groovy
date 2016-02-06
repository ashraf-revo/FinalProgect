package org.revo.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

/**
 * Created by revo on 02/11/15.
 */
@Controller
@RequestMapping("admin")
class Admin {
    @RequestMapping
    @ResponseBody
    def index() {
        "admin"
    }
}
