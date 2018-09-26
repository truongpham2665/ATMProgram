package com.homedirect.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTransactionHistory is a Querydsl query type for TransactionHistory
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTransactionHistory extends EntityPathBase<TransactionHistory> {

    private static final long serialVersionUID = -31145144L;

    public static final QTransactionHistory transactionHistory = new QTransactionHistory("transactionHistory");

    public final StringPath content = createString("content");

    public final StringPath fromAccount = createString("fromAccount");

    public final NumberPath<Integer> Id = createNumber("Id", Integer.class);

    public final StringPath status = createString("status");

    public final DateTimePath<java.util.Date> time = createDateTime("time", java.util.Date.class);

    public final StringPath toAccount = createString("toAccount");

    public final NumberPath<Double> transferAmount = createNumber("transferAmount", Double.class);

    public final NumberPath<Byte> type = createNumber("type", Byte.class);

    public QTransactionHistory(String variable) {
        super(TransactionHistory.class, forVariable(variable));
    }

    public QTransactionHistory(Path<? extends TransactionHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTransactionHistory(PathMetadata metadata) {
        super(TransactionHistory.class, metadata);
    }

}

