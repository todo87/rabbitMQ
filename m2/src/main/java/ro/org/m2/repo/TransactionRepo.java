package ro.org.m2.repo;

import ro.org.m2.model.Transaction;

import java.util.List;

public interface TransactionRepo extends GenericRepo<Transaction, Long> {

    List<Transaction> findAllByPayeeNameOrderByTransactionTypeAsc(String payeeName);
    List<Transaction> findAllByOrderByPayeeNameAscTransactionTypeAsc();

}
