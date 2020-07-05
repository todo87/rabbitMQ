package ro.org.m2.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ro.org.common.enums.TransactionTypeEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "transactions")
@Data
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper=true, includeFieldNames=true, doNotUseGetters = true)
public class Transaction extends GenericModelAudited {

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionTypeEnum transactionType;



    @Column(name = "payer_cnp")
    private String payerCNP;

    @Column(name = "payer_iban")
    private String payerIBAN;

    @Column(name = "payer_name")
    private String payerName;


    @Column(name = "payee_cnp")
    private String payeeCNP;

    @Column(name = "payee_iban")
    private String payeeIBAN;

    @Column(name = "payee_name")
    private String payeeName;


    @Column(name = "amount")
    private BigDecimal amount;



    @OneToOne(mappedBy = "transaction", cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY, optional = false)
    private TransactionDetail transactionDetail;

}
