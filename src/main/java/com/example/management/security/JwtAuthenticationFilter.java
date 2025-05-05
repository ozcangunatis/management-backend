package com.example.management.security;

import com.example.management.service.ApplicationUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final ApplicationUserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui") || path.startsWith("/swagger-ui.html")) {
            filterChain.doFilter(request, response);
            return;
        }


        final String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("⛔ Token yok veya 'Bearer ' ile başlamıyor.");
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);
        System.out.println("2️⃣ Token (sadece JWT kısmı): " + token);
        final String username = jwtService.extractUsername(token);
        System.out.println("3️⃣ Token'dan çıkarılan kullanıcı email: " + username);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            System.out.println("4️⃣ Veritabanından bulunan user: " + userDetails.getUsername());

            if (jwtService.isTokenValid(token, userDetails)) {
                System.out.println("✅ Token geçerli");
                String role = jwtService.extractAllClaims(token).get("role", String.class);
                System.out.println("5️⃣ Token içindeki rol: " + role);
                var authority = new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role);
                System.out.println("6️⃣ Yetki oluşturuldu: " + authority.getAuthority());
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                Collections.singletonList(authority)
                        );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                System.out.println("🧠 Authentication Authorities: " + authToken.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);

                System.out.println("7️⃣ Authentication SecurityContext'e atandı.");
            }
        }

        filterChain.doFilter(request, response);
        System.out.println("8️⃣ Filtre zinciri devam etti.");
    }
}
