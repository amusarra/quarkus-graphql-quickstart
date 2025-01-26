package it.dontesta.labs.quarkus.graphql.orm.panache.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity(name = "editor")
public class Editor extends PanacheEntity {
    public String name;

    @OneToMany(mappedBy = "editor")
    @JsonBackReference
    public List<Book> books;
}