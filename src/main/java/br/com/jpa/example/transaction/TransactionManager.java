package br.com.jpa.example.transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.function.Consumer;

public class TransactionManager {
    private EntityManagerFactory managerFactory;

    public TransactionManager(EntityManagerFactory managerFactory) {
        this.managerFactory = managerFactory;
    }

    public void executa(Consumer<EntityManager> acao) {

        EntityManager manager = null;
        EntityTransaction transaction = null;

        try {
            manager = managerFactory.createEntityManager();
            transaction = manager.getTransaction();
            transaction.begin();

            acao.accept(manager);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException(e);
        } finally {
            if (manager != null) {
                manager.close();
            }
        }
    }
}
