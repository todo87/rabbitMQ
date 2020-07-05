package ro.org.m2.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.org.common.DTOs.TransactionDTO;
import ro.org.m2.model.Transaction;
import ro.org.m2.model.TransactionDetail;
import ro.org.m2.repo.TransactionRepo;

import java.util.List;

@Service
public class TransactionService extends GenericServiceImpl<Transaction, Long> {

    private final TransactionRepo transactionRepo;

    public TransactionService(TransactionRepo transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    public List<Transaction> findAllByPayeeNameOrderByTransactionTypeAsc(String payeeName) {
        return transactionRepo.findAllByPayeeNameOrderByTransactionTypeAsc(payeeName);
    }

    public List<Transaction> findAllByOrderByPayeeNameAscTransactionTypeAsc() {
        return transactionRepo.findAllByOrderByPayeeNameAscTransactionTypeAsc();
    }

    @Transactional
    public Transaction persistTransaction(TransactionDTO transactionDTO) {
        TransactionDetail transactionDetail = new TransactionDetail();
        transactionDetail.setDetails(transactionDTO.getDescription());

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setTransactionType(transactionDTO.getTransactionType());
        transaction.setTransactionDetail(transactionDetail);

        transaction.setPayerCNP(transactionDTO.getPayer().getCnp());
        transaction.setPayerIBAN(transactionDTO.getPayer().getIBAN());
        transaction.setPayerName(transactionDTO.getPayer().getName());

        transaction.setPayeeCNP(transactionDTO.getPayee().getCnp());
        transaction.setPayeeIBAN(transactionDTO.getPayee().getIBAN());
        transaction.setPayeeName(transactionDTO.getPayee().getName());

        return save(transaction);
    }


}
