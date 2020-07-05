package ro.org.m2.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ro.org.common.DTOs.TransactionDTO;

@Service
public class RabbitMQConsumer {

    private final TransactionService transactionService;

    public RabbitMQConsumer(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @RabbitListener(queues = "${m2.rabbitmq.queue}")
    public void recievedMessage(TransactionDTO transactionDTO) {
        transactionService.persistTransaction(transactionDTO);
    }

}
