package com.mantzavelas.eodyrapidtestpoiretriever.messaging;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;

@Configuration
public class RabbitMQConfiguration {

    public static final String POI_GEODATA_QUEUE = "amqp.queue.poigeodata";
    public static final String POI_GEODATA_ROUTING_KEY = "POI-GEODATA";
    public static final String POI_DB_SAVE_QUEUE = "amqp.queue.poidbsave";
    public static final String POI_DB_SAVE_ROUTING_KEY = "POI-DBSAVE";
    private final Map<String,String> queues = Map.of(
        POI_GEODATA_QUEUE, POI_GEODATA_ROUTING_KEY,
        POI_DB_SAVE_QUEUE, POI_DB_SAVE_ROUTING_KEY
    );

    private final AmqpAdmin amqpAdmin;

    @Autowired
    public RabbitMQConfiguration(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }

    @PostConstruct
    public void setUpGeoDataQueues() {
        queues.forEach((queue, routingKey) -> {
            Exchange ex = ExchangeBuilder.directExchange(queue)
                .durable(true)
                .build();
            amqpAdmin.declareExchange(ex);

            Queue q = QueueBuilder.durable(routingKey)
                .build();
            amqpAdmin.declareQueue(q);

            Binding b = BindingBuilder.bind(q)
                .to(ex)
                .with(routingKey)
                .noargs();
            amqpAdmin.declareBinding(b);
        });
    }

}
