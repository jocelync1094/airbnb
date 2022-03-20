package com.laioffer.airbnb.filter;

import com.laioffer.airbnb.entity.Authority;
import com.laioffer.airbnb.repository.AuthorityRepository;
import com.laioffer.airbnb.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";

    private JwtUtil jwtUtil;
    private AuthorityRepository authorityRepository;

    @Autowired
    public JwtFilter(JwtUtil jwtUtil, AuthorityRepository authorityRepository) {
        this.jwtUtil = jwtUtil;
        this.authorityRepository = authorityRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //our filter
        String header = request.getHeader(HEADER);
        String jwt = null;
        if (header != null && header.startsWith(PREFIX)) {
            jwt = header.substring(7); //this is our encoded token string
        }
        if (jwt != null && jwtUtil.validateToken(jwt) && SecurityContextHolder.getContext().getAuthentication() == null) { //validates the expiration time
            String username = jwtUtil.extractUsername(jwt); //we take out this because many other API will need this
            //info to access other user info
            //in Spring security if any of your Controllers' API's need User info, you just need to add Principal as a
            // parameter. Spring security will then find the extracted username that is saved in your securityContextHolder
            //THUS we need to put username into security context holder (provided by Spring security)
            Authority authority = authorityRepository.findById(username).orElse(null);
            if (authority != null) {
                List<GrantedAuthority> grantedAuthorities = Arrays.asList(new GrantedAuthority[]{new SimpleGrantedAuthority(authority.getAuthority())});
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        username, null, grantedAuthorities);
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            }

        }
        filterChain.doFilter(request, response);
    }
}
