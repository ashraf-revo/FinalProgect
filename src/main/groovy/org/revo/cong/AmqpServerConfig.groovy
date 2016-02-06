package org.revo.cong

import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created by revo on 21/10/15.
 */
@Configuration
@EnableRabbit
class AmqpServerConfig {

    @Bean
    public RabbitAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory)
        rabbitAdmin.declareQueue(new Queue("toServer"))
        rabbitAdmin
    }

    @Bean
    public FanoutExchange fanoutExchange(RabbitAdmin rabbitAdmin) {
        FanoutExchange fanoutExchange = new FanoutExchange("fanoutExchange")
        rabbitAdmin.declareExchange(fanoutExchange)
        fanoutExchange
    }

    @Bean
    public DirectExchange directExchange(RabbitAdmin rabbitAdmin) {
        DirectExchange directExchange = new DirectExchange("directExchange")
        rabbitAdmin.declareExchange(directExchange)
        directExchange
    }


    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        new RabbitTemplate(connectionFactory)
    }
}
