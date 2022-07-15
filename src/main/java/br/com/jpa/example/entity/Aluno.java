package br.com.jpa.example.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;

@Entity
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String nome;

    private LocalDate dataNascimento;

    public Aluno(String nome) {

        this.nome = nome;
    }

    @Deprecated
    public Aluno() {
    }

    public Long getId() {
        return id;
    }


    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public boolean isMaiorDeIdade() {
        return Period.between(dataNascimento, LocalDate.now()).getYears() >= 18;
    }
}