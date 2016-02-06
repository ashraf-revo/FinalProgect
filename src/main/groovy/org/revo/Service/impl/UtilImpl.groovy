package org.revo.Service.impl

import org.revo.Repository.PersonRepository
import org.revo.Service.Util
import org.revo.domain.Child
import org.revo.domain.Person
import org.revo.model.Email
import org.revo.model.detect
import org.revo.model.status
import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.RememberMeServices
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.social.connect.Connection
import org.springframework.social.connect.UserProfile
import org.springframework.social.connect.web.ProviderSignInUtils
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.request.WebRequest
import rx.Observable
import rx.schedulers.Schedulers

import javax.mail.internet.MimeMessage
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by revo on 05/11/15.
 */
@Repository
@Transactional
class UtilImpl implements Util {
    @Autowired
    SkybiometryImpl skybiometry
    @Autowired
    AmqpAdmin amqpAdmin
    @Autowired
    FanoutExchange fanoutExchange
    @Autowired
    DirectExchange directExchange
    @Autowired
    RabbitTemplate rabbitTemplate
    @Autowired
    private JavaMailSender javaMailSender
    @Autowired
    PersonRepository personRepository
    @Autowired
    PasswordEncoder encoder
    @Autowired
    ProviderSignInUtils signInUtils
    @Autowired
    RememberMeServices services

    @Override
    void email(Email email) {
        MimeMessage message = javaMailSender.createMimeMessage()
        MimeMessageHelper helper = new MimeMessageHelper(message, true)
        helper.setTo(email.to)
        helper.setSubject(email.Subject)
        helper.setText(email.Message, true)
        javaMailSender.send(message)
    }

    @Override
    public void CreateQueueFor(String name) {
        Queue queue = new Queue(name)
        amqpAdmin.declareQueue(queue)
        amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(fanoutExchange))
        amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(directExchange).withQueueName())
    }

    @Override
    void send() {
        rabbitTemplate.convertAndSend("fanoutExchange", "", "welcome")
    }

    @Override
    void afteruplode(Child child) {
        Observable.<detect> create {
            it.onNext(skybiometry.detect(child.getImg(), "all"))
            it.onCompleted()
        }.filter { it.status == status.success }.map { it.photos }
                .subscribeOn(Schedulers.io()).subscribe {
            it.each { t ->
//                Set<String> collect = (0..t.tags.size()).collect {
//                    UUID.randomUUID().toString().replace("-", "") + skybiometry.login.namespace
//                }.toSet()
//                t.tags.eachWithIndex { tag, index ->
//                    if (skybiometry.save(collect[index], tag.tid).getStatus() == status.success)
//                        skybiometry.train(collect[index])
//                }
//                child.uid = collect
//                println child
            }
        }
    }

    @Override
    String getCurrentLogin() {
        Authentication authentication = SecurityContextHolder.context.authentication
        UserDetails springSecurityUser = null
        String userName = null
        if (authentication != null) {
            if (authentication.principal instanceof UserDetails) {
                springSecurityUser = authentication.principal
                userName = springSecurityUser.username
            } else {
                userName = authentication.principal
            }
        }
        userName
    }

    @Override
    void handelSocialSignUp(WebRequest request, HttpServletRequest req, HttpServletResponse res) {
        Connection<?> connectionFromSession = signInUtils.getConnectionFromSession(request)
        if (connectionFromSession != null) {
            UserProfile userProfile = connectionFromSession.fetchUserProfile()
            Person u = personRepository.findByEmail(userProfile.getEmail())
                    .map { it }
                    .orElseGet {
                String queueName = UUID.randomUUID().toString().replace("-", "").subSequence(0, 12).toString()
                CreateQueueFor(queueName);
                personRepository.save(new Person(email: userProfile.email,
                        password: encoder.encode(UUID.randomUUID().toString()),
                        image: connectionFromSession.imageUrl, role: "ROLE_USER",
                        name: userProfile.firstName + " " + userProfile.lastName,
                        queueName: queueName))
            }
            Authentication authentication = new UsernamePasswordAuthenticationToken(u.email, u.password, [new SimpleGrantedAuthority(u.role)])
            Authentication auth = SecurityContextHolder.context.authentication
            if (auth != null) {
                new SecurityContextLogoutHandler().logout(req, res, auth)
                SecurityContextHolder.context.authentication = authentication
            }
            signInUtils.doPostSignUp(userProfile.getEmail(), request)
            services.loginSuccess(req, res, authentication)
        }

    }

}
