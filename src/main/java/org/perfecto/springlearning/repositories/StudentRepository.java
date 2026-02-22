package org.perfecto.springlearning.repositories;

import org.perfecto.springlearning.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


// this is the interface that will be used to interact with the database and perform CRUD operations on the student entity via extending to jparepository which is a spring data interface that provides methods for performing CRUD operations on the database and it takes two parameters the first one is the entity class and the second one is the type of the primary key of the entity class
// from here the flow starts and goes to the service layer where we will have the business logic and then to the controller layer where we will have the endpoints for the API
@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
}
