package org.revo.Service

import org.revo.Repository.PersonRepository
import org.revo.domain.Location
import org.revo.domain.Person
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by revo on 21/10/15.
 */
@Component
class ServerRevoListener {
    @Autowired
    PersonRepository personRepository
// in this format "id=1,x=1,y=2"
    //id=1,x=30.5325805,y=31.0990651
    @RabbitListener(queues = "toServer")
    void Receive(String body) {
        Map map = convertData(body)
        println map
        if (map.size() == 3 && map['id']) {
            Person person = personRepository.findOne(map['id'].toString().toLong())
            if (person && map['x'] && map['y']) {
                person.location = new Location(x: map['x'].toString().toDouble(), y: map['y'].toString().toDouble())
                personRepository.save(person)
            }
        }
    }

    public static Map convertData(String body) {
        String[] data = body.split(",")
        byte[] Payload = new byte[data.length]
        data.eachWithIndex { String entry, int i ->
            Payload[i] = entry.toInteger()
        }
        String message = new String(Payload)
        def map = [:]
        message.split(",").each {
            def split = it.split("=")
            try {
                map[split[0]] = split[1]
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
        return map
    }
}
