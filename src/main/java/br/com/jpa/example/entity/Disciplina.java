package br.com.jpa.example.entity;


import javax.persistence.*;

@Entity
public class Disciplina {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String nome;


    public Disciplina(String nome) {
        this.nome = nome;
    }

    @Deprecated
    public Disciplina() {
    }

    public Long getId() {
        return id;
    }


    @Override
    public String toString() {
        return "Disciplina{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}
