package org.revo

import org.junit.Ignore
import org.junit.Test
import org.revo.domain.Child
import org.revo.domain.Person
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.client.RestTemplate

//@RunWith(SpringJUnit4ClassRunner)
//@SpringApplicationConfiguration(classes = FinalProjectApplication)
class FinalProjectApplicationTests {

    @Test()
    @Ignore
    void testBasicHttp() {
        String plainCreds = "ashraf.abdelrasool@yahoo.com:revo";
        String base64Creds = new String(Base64.getEncoder().encode(plainCreds.getBytes()));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<Person> response = new RestTemplate().exchange("http://localhost:8080/admin/CurrentLogin", HttpMethod.GET, request, Person.class);
        println response.getBody()
    }

    @Test
    @Ignore
    void testMessageFormats() {
        String message = "id=1,x=1,y=2"
        def map = [:]
        message.split(",").each {
            def split = it.split("=")
            try {
                map[split[0]] = split[1]
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
        println map['id']
    }

    @Test
    @Ignore
    void testbyte() {
        String body = "105,100,61,49,44,120,61,51,49,44,121,61,52,54"
        String[] data = body.split(",")
        byte[] message = new byte[data.length]
        data.eachWithIndex { String entry, int i ->
            message[i] = entry.toInteger()
        }
        new String(message)
    }

    @Test
    @Ignore
    void SearchFildes() {
        Class TClass = Child.class
        println Arrays.
                asList(TClass.getDeclaredFields())
                .findAll {
            it.isAnnotationPresent(Field.class) || it.isAnnotationPresent(IndexedEmbedded.class)
        }.collect { it.name }.toArray(String[])
    }

    @Test
    void password() {
        println new BCryptPasswordEncoder().encode("revo")
    }

    @Test
    @Ignore
    void ss() {

        def toLong = "30.5325798".toDouble()
        println toLong
    }
}
