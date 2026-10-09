package com.jdc.fruverinari.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                boolean logueado = auth != null
                        && auth.isAuthenticated()
                        && !(auth.getPrincipal() instanceof String p && "anonymousUser".equals(p));

                request.setAttribute("logueado", logueado);
                request.setAttribute("esAdmin", tieneRol(auth, "ROLE_ADMIN"));
                request.setAttribute("esCajero", tieneRol(auth, "ROLE_CAJERO"));
                request.setAttribute("esCliente", tieneRol(auth, "ROLE_CLIENTE"));
                return true;
            }

            private boolean tieneRol(Authentication auth, String rol) {
                if (auth == null) {
                    return false;
                }
                for (GrantedAuthority authority : auth.getAuthorities()) {
                    if (rol.equals(authority.getAuthority())) {
                        return true;
                    }
                }
                return false;
            }
        });
    }
}
