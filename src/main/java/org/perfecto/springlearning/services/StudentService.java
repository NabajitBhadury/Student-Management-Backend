// here in this service layer we will be writing all the business logic for student like the exception handling and the validation and all the operations that we want to perform on the student entity and then we will be calling these methods from the controller layer where we will have the endpoints for the API and we will be returning the response to the client from the controller layer and in the service layer we will be returning the data to the controller layer after performing all the operations on it

package org.perfecto.springlearning.services;

import org.perfecto.springlearning.exceptions.genericExceptions.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.perfecto.springlearning.models.Student;
import org.perfecto.springlearning.repositories.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service // this annotation is used to mark the class as a service class and it is a specialization of the @Component annotation which is used to mark the class as a bean
@RequiredArgsConstructor // this annotation is used to generate a constructor with required arguments which are the final or marked as not null in the class and it is used to inject the dependencies into the class
public class StudentService {
    // make instance of student repo
    private final StudentRepository studentRepository;

    // JPA already has the inbuilt methods for performing CRUD operations on the database here we are using those only
    // here we are using the @Transactional annotation to mark the method as a transactional method which means that if any exception occurs in the method then the transaction will be rolled back and if the method executes successfully then the transaction will be committed and we are using readOnly = true for the methods that are only used for reading data from the database and for the methods that are used for writing data to the database we are not using readOnly = true because we want to commit the transaction after writing data to the database
    @Transactional(readOnly = true)
    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Student getStudentById(UUID studentId) {
        return studentRepository.findById(studentId).orElseThrow(() ->new ResourceNotFoundException("Student with id: " + studentId + " not found"));
    }

    @Transactional
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    @Transactional
    public void deleteStudentById(UUID studentId) {
        if (studentRepository.existsById(studentId)) {
            studentRepository.deleteById(studentId);
        } else{
            throw new ResourceNotFoundException("Student with id: " + studentId + " not found");
        }
    }

    @Transactional
    public Student updateStudentById(UUID studentId, Student student) {
            return studentRepository.findById(studentId).map(existingStudent -> {
                existingStudent.setName(student.getName());
                existingStudent.setRoll(student.getRoll());
                existingStudent.setMarks(student.getMarks());
                return studentRepository.save(existingStudent);
            }).orElseThrow(() ->new ResourceNotFoundException("Student with id: " + studentId + " not found"));

    }

}
