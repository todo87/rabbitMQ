package ro.org.m2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.org.common.DTOs.ReportDownloadDTO;
import ro.org.common.DTOs.TransactionDTO;
import ro.org.m2.service.TransactionService;
import ro.org.m2.service.business.TransactionBusinessService;

@RestController("transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionBusinessService transactionBusinessService;

    public TransactionController(TransactionService transactionService, TransactionBusinessService transactionBusinessService) {
        this.transactionService = transactionService;
        this.transactionBusinessService = transactionBusinessService;
    }

    @PostMapping("persist")
    public void persistTransaction(TransactionDTO transactionDTO) {
        transactionService.persistTransaction(transactionDTO);
    }

    @GetMapping("get-report/{payeeName}")
    public ReportDownloadDTO downloadReport(@PathVariable String payeeName) {
        return transactionBusinessService.downloadReport(payeeName);
    }

}
