import br.com.jpa.example.entity.Aluno;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.*;

public class StateCicleJPAEntityTest {
    private EntityManager manager;

    @BeforeEach
    void setUp() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("escola");
        this.manager = entityManagerFactory.createEntityManager();
        manager.getTransaction().begin();

        manager.createQuery("delete from Aluno")
                .executeUpdate();//garatindo que os testes sejam independentes e isolados uns dos outros

        manager.getTransaction().commit();
    }

    @AfterEach
    void tearDown() {
        this.manager.close();//apos o termino de cada test o contexto de persistencia é encerrado
    }


    @Test
    void deveTransitarDeTransientParaManaged() {

        Aluno aluno = new Aluno("Jordi H.");
        EntityTransaction transaction = this.manager.getTransaction();
        transaction.begin();

        manager.persist(aluno);

        transaction.commit();

        assertTrue(
                manager.contains(aluno),
                "Esta entidade deveria estar no Contexto de Persistencia"

        );

    }


    @Test
    void deveTransitarDeManagedParaRemoved() {
        Aluno aluno = new Aluno("Jordi H.");
        EntityTransaction transaction = this.manager.getTransaction();
        transaction.begin();

        manager.persist(aluno);

        manager.remove(aluno);

        transaction.commit();


        assertFalse(
                manager.contains(aluno),
                "esta entidade não deveria pertencer ao Contexto de Persistencia"
        );
        assertNull(
                manager.find(Aluno.class,aluno.getId()),
                "Não deveria existir registro para este id"
        );
    }

    @Test
    void deveTransitarDeDetachedParaManaged() {
        Aluno aluno = new Aluno("Jordi H.");
        EntityTransaction transaction = this.manager.getTransaction();
        transaction.begin();

        manager.persist(aluno);
        transaction.commit();

        this.manager.detach(aluno);
        assertFalse(
                manager.contains(aluno),
                "esta entidade não deve participar do Contexto de Persistencia"
        );


        aluno.setNome("Yuri Matheus");

        transaction.begin();
        Aluno alunoAposMerge = manager.merge(aluno);
        transaction.commit();

        assertTrue(
                manager.contains(alunoAposMerge),
                "esta entidade deve pertencer ao estado Contexto de Persistencia"
        );

    }

    @Test
    void deveTransitarDeManagedParaDetached() {
        Aluno aluno = new Aluno("Jordi H.");
        EntityTransaction transaction = this.manager.getTransaction();
        transaction.begin();

        manager.persist(aluno);
        transaction.commit();

        this.manager.detach(aluno);
        assertFalse(
                manager.contains(aluno),
                "esta entidade não deve participar do contexto de persistencia"
        );
    }
}
