package org.revo

import org.revo.Repository.PersonRepository
import org.revo.Service.SomeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.web.SpringBootServletInitializer

@SpringBootApplication
class FinalProjectApplication extends SpringBootServletInitializer implements CommandLineRunner {
    static void main(String[] args) {
        SpringApplication.run FinalProjectApplication, args
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        builder.sources FinalProjectApplication.class
    }

    @Override
    void run(String... strings) throws Exception {
        if (personRepository.count() == 0) someService.init()
    }
    @Autowired
    SomeService someService
    @Autowired
    PersonRepository personRepository
}
