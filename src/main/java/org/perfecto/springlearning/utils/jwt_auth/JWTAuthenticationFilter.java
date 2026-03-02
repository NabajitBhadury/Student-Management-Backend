package org.perfecto.springlearning.utils.jwt_auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.perfecto.springlearning.models.Student;
import org.perfecto.springlearning.services.StudentService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final StudentService studentService;

    public JWTAuthenticationFilter(JWTService jwtService, StudentService studentService) {
        this.jwtService = jwtService;
        this.studentService = studentService;
    }


    @Override
    protected void doFilterInternal
            (HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authorizationHeader.substring(7);
        final String studentId = jwtService.extractClaim(token, claims -> claims.getSubject());

        if(studentId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if(jwtService.validateToken(token)){
                final Student student = studentService.getStudentById(UUID.fromString(studentId));
                StudentPrincipal principal = new StudentPrincipal(student);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
