package org.revo.controller

import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.fasterxml.jackson.annotation.JsonView
import org.revo.Service.ChildService
import org.revo.Service.LoggerService
import org.revo.Service.Util
import org.revo.Service.impl.SkybiometryImpl
import org.revo.domain.Child
import org.revo.domain.SuggestedChild
import org.revo.model.ErrorMessage
import org.revo.model.SearchCriteria
import org.revo.model.View
import org.revo.model.status
import org.revo.model.uids
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.context.request.WebRequest

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by revo on 21/10/15.
 */
@Controller
class Main {
    @Autowired
    Util util
    @Autowired
    Cloudinary cloudinary
    @Autowired
    SkybiometryImpl skybiometry
    private final static Logger logger = LoggerFactory.getLogger(Main.class)

    @RequestMapping("/")
    def index() {
        "redirect:/index.html"
    }

    @RequestMapping(value = "/uplode", method = RequestMethod.POST)
    @ResponseBody
    @JsonView(View.onlyMe)
    def uplode(@ModelAttribute Child child) {
        try {
            if (child.file && !child.file.isEmpty()) {
                Map map = cloudinary.uploader().uploadLarge(child.file.inputStream, ObjectUtils.emptyMap());
                child.image = map.get("url") as String
                Child saveChild = childService.save(child)
                def de = skybiometry.detect(child.image, "all")
                if (de.status == status.success) {
                    if (de.photos.size() == 1) {
                        def photos = de.photos.get(0);
                        if (photos.tags.size() == 1) {
                            def tags = photos.tags.get(0)
                            def uid = String.valueOf(saveChild.id + "@revo")
                            def save = skybiometry.save(uid, tags.tid)
                            if (save.status == status.success) {
                                skybiometry.train(uid)
                                new ResponseEntity(childService.addUid(saveChild.id, uid), HttpStatus.OK)
                            } else throw new Exception("some error happen caused me to unable to save it to the skybiometry server")
                        } else throw new Exception("i need only one tag in this photo to continue")
                    } else throw new Exception("i need only one image to continue")
                } else throw new Exception("cant detect image")
            } else throw new Exception("some error in this file please uplode another one ")
        } catch (Exception ignored) {
            loggerService.Error(logger, new ErrorMessage(1, ignored.message))
        }
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    @JsonView(View.onlyMe)
    def SearchImage(@RequestParam(value = "id") Long id) {
        try {
            Child child = childService.findOne(id)
            String AllUid = childService.findAll().findAll {
                it.uid != null
            }.collect {
                it.uid
            }.join(",")
            def reco = skybiometry.recognize(AllUid, child.image)
            List<uids> uuids = []
            reco.photos.each {
                it.tags.each {
                    uuids.addAll(it.uids)
                }
            }

            Child result = childService.updateSuggestedChild(id, uuids)
            List<SuggestedChild> e = []
            result.suggestedChild.each {
                it.suggested = new Child(id: it.suggested.id, name: it.suggested.name, image: it.suggested.image, addresses: it.suggested.addresses, age: it.suggested.age, date: it.suggested.date, gender: it.suggested.gender, founded: it.suggested.founded, moreInfo: it.suggested.moreInfo, currentPalce: it.suggested.currentPalce,uid: it.suggested.uid,state: it.suggested.state)
                e.add(new SuggestedChild(id: it.id, confidence: it.confidence, suggested: it.suggested))
            }

            result.suggestedChild = e
            new ResponseEntity(result, HttpStatus.OK)
        } catch (Exception ignored) {
            loggerService.Error(logger, new ErrorMessage(1, ignored.message))
        }
    }

    @Autowired
    LoggerService loggerService
    @Autowired
    ChildService childService

    @RequestMapping(value = "/signup")
    String signup(WebRequest request, HttpServletRequest req, HttpServletResponse res) {
        util.handelSocialSignUp(request, req, res)
        return "redirect:/home"
    }

    @RequestMapping("/csrf")
    @ResponseBody
    def csrf(CsrfToken token) {
        token
    }

    @RequestMapping(value = "/searchd", method = RequestMethod.POST)
    @ResponseBody
    def Search(@ModelAttribute SearchCriteria criteria) {
//        SearchCriteria criteria = new SearchCriteria(searchTxt: "ashraf",className: "person");
//        searchTemplate.Search(criteria.searchTxt, criteria.className == "child" ? Child.class : Person.class, criteria.firstResult, criteria.maxResults, criteria.getTempFildes())
        true
    }

}
