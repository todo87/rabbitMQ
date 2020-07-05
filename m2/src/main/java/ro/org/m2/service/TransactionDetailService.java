package ro.org.m2.service;

import org.springframework.stereotype.Service;
import ro.org.m2.model.TransactionDetail;
import ro.org.m2.repo.TransactionDetailRepo;

@Service
public class TransactionDetailService extends GenericServiceImpl<TransactionDetail, Long> {

    private final TransactionDetailRepo transactionDetailRepo;

    public TransactionDetailService(TransactionDetailRepo transactionDetailRepo) {
        this.transactionDetailRepo = transactionDetailRepo;
    }
}
