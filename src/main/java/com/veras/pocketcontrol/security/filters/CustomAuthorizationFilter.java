package com.veras.pocketcontrol.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.veras.pocketcontrol.utils.Consts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer ";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println(request.getHeader(AUTHORIZATION));
        if(isOpenPath(request.getServletPath())) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if(authorizationHeader != null && authorizationHeader.startsWith(BEARER)){
                try {
                    String token = authorizationHeader.substring(BEARER.length());
                    Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token);
                    String username = decodedJWT.getSubject();

                    //TO-DO Para se implementarmos roles
                    //String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    //Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    //stream(roles).forEach( role -> {
                    //    authorities.add(new SimpleGrantedAuthority(role));
                    //});

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, null);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception e){
                    System.out.println(e.getMessage());
                    response.setStatus(403);
                    response.getWriter().println(e.getMessage());
                }

            } else {
                response.sendError(FORBIDDEN.value());
            }

        }
    }

    private boolean isOpenPath(String servletPath) {
        return Consts.OPEN_PATHS.contains(servletPath)
                || servletPath.contains("/webjars/")
                || servletPath.contains("/swagger-resources")
                || servletPath.contains("/v2/api-docs")
                || servletPath.contains("/swagger-ui/");
    }
}
