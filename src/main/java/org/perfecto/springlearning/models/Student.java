package org.perfecto.springlearning.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Entity // tells jpa to treat it like a database column we are explicitly saying this
@Table(name = "students") // here give the table column a name
@Data //generates getters setters automatically
@Builder // with the use of bellow two things we can use builder.constructor name for autometically created constructor
@NoArgsConstructor // generates the default constructor automatically
@AllArgsConstructor // generates the parametrized constructor for all the fields including all permutation and combinations

public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private int roll;

    @Column(nullable = false)
    private String name;
    private int marks;

}
