package com.bpwizard.wcm.repo.jcr;
import javax.transaction.TransactionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.modeshape.jcr.api.txn.TransactionManagerLookup;

/**
 * {@link TransactionManagerLookup} implementation needed to make ModeShape use a custom transaction manager. We're using
 * Spring here to inject the actual Atomikos instance which we'll simply return to ModeShape.
 *
 */
public class ModeshapeTransactionManagerLookup implements TransactionManagerLookup {

    private static final Logger logger = LoggerFactory.getLogger(ModeshapeTransactionManagerLookup.class);

    private static TransactionManager transactionManager;

    public void setTransactionManager(TransactionManager transactionManager) {
    	ModeshapeTransactionManagerLookup.transactionManager = transactionManager;
    }

    @Override
    public TransactionManager getTransactionManager() {
    	if (ModeshapeTransactionManagerLookup.transactionManager != null) {
            logger.info("Using transaction manager...");
            return ModeshapeTransactionManagerLookup.transactionManager;
        }
        throw new IllegalStateException("Spring has not injected the Atomikos TM as expected. Check the configurations...");
    }
}
