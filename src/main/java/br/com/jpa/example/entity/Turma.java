package br.com.jpa.example.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Turma {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String nome;

    @ManyToOne
    private Disciplina disciplina;

    @ManyToMany
    private List<Aluno> alunos = new ArrayList<>();

    public Turma(String nome, Disciplina disciplina) {
        this.nome = nome;
        this.disciplina=disciplina;
    }

    public Turma() {
    }

    public Long getId() {
        return id;
    }

    public void matricular(Aluno aluno) {
        if (!aluno.isMaiorDeIdade()) {
            throw new RuntimeException("O aluno não tem idade necessária para esta disciplina");
        }
        this.alunos.add(aluno);
    }


}
