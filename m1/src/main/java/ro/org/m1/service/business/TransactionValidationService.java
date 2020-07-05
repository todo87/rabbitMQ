package ro.org.m1.service.business;

import org.springframework.stereotype.Service;
import ro.org.common.DTOs.TransactionDTO;
import ro.org.common.DTOs.TransactionPartDTO;
import ro.org.common.exceptions.AppValidationException;
import ro.org.m1.service.RabbitMQSender;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.executable.ValidateOnExecution;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@ValidateOnExecution
public class TransactionValidationService {

    private final RabbitMQSender rabbitMQSender;

    private final Validator validator;

    public TransactionValidationService(RabbitMQSender rabbitMQSender, Validator validator) {
        this.rabbitMQSender = rabbitMQSender;
        this.validator = validator;
    }

    public void validateAndMakeTransaction(TransactionDTO transactionDTO) {
        if (transactionDTO == null ) {
            throw new AppValidationException("Transaction invalid");
        }

        Set<ConstraintViolation<TransactionDTO>> violations = validator.validate(transactionDTO);
        if (!violations.isEmpty()) {
            throw new AppValidationException(violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(";")));
        }

        validateTransactionDetailsDTO(transactionDTO.getPayer());
        validateTransactionDetailsDTO(transactionDTO.getPayee());

        // TODO further complex validations for iban if it's necessary

        rabbitMQSender.send(transactionDTO);

    }

    public void validateTransactionDetailsDTO(TransactionPartDTO transactionDTO) {
        Set<ConstraintViolation<TransactionPartDTO>> violations = validator.validate(transactionDTO);
        if (!violations.isEmpty()) {
            throw new AppValidationException(violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(";")));
        }
    }

}
