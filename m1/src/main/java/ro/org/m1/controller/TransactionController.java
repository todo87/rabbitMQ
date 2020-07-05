package ro.org.m1.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.org.common.DTOs.TransactionDTO;
import ro.org.m1.service.business.TransactionValidationService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionValidationService transactionValidationService;

    public TransactionController(TransactionValidationService transactionValidationService) {
        this.transactionValidationService = transactionValidationService;
    }

    @PostMapping(value = "/validate-and-make-transaction",  consumes = "application/json")
    public void validateAndMakeTransaction(@RequestBody TransactionDTO transactionDTO) {
        transactionValidationService.validateAndMakeTransaction(transactionDTO);
    }
}
