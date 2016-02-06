package org.revo.controller

import org.revo.Repository.PersonRepository
import org.revo.Service.Util
import org.revo.Service.impl.SkybiometryImpl
import org.revo.domain.Person
import org.revo.model.variables
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartFile

/**
 * Created by revo on 02/11/15.
 */
@Controller
@RequestMapping("home")
class Home {
    @Autowired
    Environment env
    @Autowired
    PersonRepository personRepository
    @Autowired
    SkybiometryImpl skybiometry
    @Autowired
    Util util

    @RequestMapping
    @ResponseBody
    def index() {
        "home"
    }

    @ResponseBody
    @RequestMapping(value = "currentlogin")
    def getCurrentLogin() {
        personRepository.findByEmail(util.currentLogin).orElseGet { new Person() }
    }

    @ResponseBody
    @RequestMapping(value = "variables")
    def getVariables() {
        Person person = personRepository.findByEmail(util.currentLogin).get()
        new variables(person.queueName, env)
    }

    @ResponseBody
    @RequestMapping(value = "detect")
    def dd(@RequestParam String url) {
        skybiometry.detect url, "all"
    }

    @ResponseBody
    @RequestMapping(value = "uplode")
    def Uplode(MultipartFile file) {

    }
}
