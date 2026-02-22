//here we will be writing all the api and it's related logics by using the student service i.e interacting the student repository using the service layer

package org.perfecto.springlearning.controllers;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.perfecto.springlearning.models.Student;
import org.perfecto.springlearning.services.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController // this annotation is used to mark the class as a controller class and it is a specialization of the @Component annotation which is used to mark the class as a bean and it is used to handle the incoming HTTP requests and send the response back to the client and it is a combination of @Controller and @ResponseBody annotations
@RequestMapping("/api/v1/students") // this annotation is used to map the incoming HTTP requests to the handler methods of the controller class and it is used to specify the base URL for all the endpoints in the controller class and here we are specifying the base URL as /api/v1/students which means that all the endpoints in this controller class will be prefixed with /api/v1/students
@RequiredArgsConstructor // this annotation is used to generate a constructor with required arguments which are the final or marked as not null in the class and it is used to inject the dependencies into the class and here we
public class StudentController {
    private final StudentService studentService; // make instance of student service

    // @getmapping means it is a get request and the return type of all of these will be response entity which is a generic class that is used to represent the HTTP response and it contains the status code, headers and body of the response
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents(); // ask the db for all students and the api will send the response
        log.info("Getting all students");
        return students.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<Student> getStudentById(@PathVariable UUID id) {
        Student student = studentService.getStudentById(id);
        log.info("Getting student by id {}", id);
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student createdStudent = studentService.createStudent(student);
        log.info("Created student {}", createdStudent);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@RequestBody Student student, @PathVariable UUID id) {
        Student updatedStudent = studentService.updateStudentById(id, student);
        log.info("Updated student {}", updatedStudent);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Student> deleteStudentById(@PathVariable UUID id) {
        log.info("Deleting student {}", id);
        studentService.deleteStudentById(id);
        log.info("Deleted student {}", id);
        return ResponseEntity.noContent().build();
    }
}
