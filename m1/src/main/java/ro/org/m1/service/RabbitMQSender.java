package ro.org.m1.service;

import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import ro.org.common.DTOs.TransactionDTO;

@Service
public class RabbitMQSender {

    private final AsyncRabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingkey;

    public RabbitMQSender(AsyncRabbitTemplate rabbitTemplate, @Value("${m2.rabbitmq.exchange}")String exchange, @Value("${m2.rabbitmq.routingkey}")String routingkey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingkey = routingkey;
    }

    public ListenableFuture<Boolean> send(TransactionDTO transactionDTO) {
        return rabbitTemplate.convertSendAndReceive(exchange, routingkey, transactionDTO).getConfirm();
    }

}
