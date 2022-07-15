import br.com.jpa.example.entity.Aluno;
import br.com.jpa.example.entity.Disciplina;
import br.com.jpa.example.entity.Turma;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import br.com.jpa.example.transaction.TransactionManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionManagerTest {
    private EntityManagerFactory factory;
    private TransactionManager txManager;

    @BeforeEach
    void setUp() {
        this.factory = Persistence.createEntityManagerFactory("escola");
        this.txManager = new TransactionManager(factory);
        txManager.executa(manager -> {
            manager.createQuery("delete from Aluno")
                    .executeUpdate();
        });
    }

    @Test
    void devePersistirUmAluno() {

        txManager.executa(manager -> {
            manager.persist(new Aluno("Yuri Matheus"));
        });

        EntityManager entityManager = factory.createEntityManager();

        Aluno aluno = entityManager.createQuery(
                        "select a from Aluno a where nome=:nome",
                        Aluno.class
                )
                .setParameter("nome", "Yuri Matheus")
                .getSingleResult();

        assertNotNull(aluno, "deveria existir um registro de aluno para este nome");

    }

    @Test
    void naoDevePersistiOAlunoCasoATransacaoSejaRevertida() {
        Aluno jordi = new Aluno("Jordi H.");
        Disciplina disciplina = new Disciplina("Entendendo Controle Transacional Com JPA/Hibernate");
        Turma turma1 = new Turma("Turma 1", disciplina);

        assertThrows(RuntimeException.class, () -> {
            txManager.executa(manager -> {
                manager.persist(jordi);
                manager.persist(disciplina);
                manager.persist(turma1);

                jordi.setDataNascimento(LocalDate.now());

                turma1.matricular(jordi);
            });

        });

        EntityManager entityManager = factory.createEntityManager();

        assertNull(entityManager.find(Aluno.class,jordi.getId()));
        assertNull(entityManager.find(Disciplina.class,disciplina.getId()));
        assertNull(entityManager.find(Turma.class,turma1.getId()));
    }
}
