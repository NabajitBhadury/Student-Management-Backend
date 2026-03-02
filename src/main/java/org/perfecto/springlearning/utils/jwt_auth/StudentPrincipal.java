package org.perfecto.springlearning.utils.jwt_auth;

import lombok.Getter;
import org.perfecto.springlearning.models.Student;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
public class StudentPrincipal {
    private final UUID studentId;
    private final String name;

    public StudentPrincipal(Student student) {
        this.studentId = student.getId();
        this.name = student.getName();
    }

    public Collection<GrantedAuthority> getAuthorities() {
        // Here you can return the authorities/roles associated with the student if needed
        return List.of(new SimpleGrantedAuthority("STUDENT")); // Return an empty list for now, or populate it based on your requirements
    }
}
