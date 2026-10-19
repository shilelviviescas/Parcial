package com.pos_magistral.app.configuracion;

import com.pos_magistral.app.entidades.usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class rol_interceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // Ruta sin el contexto de la aplicación (por ejemplo: /mesero/mesas)
        String ruta = request.getRequestURI().substring(request.getContextPath().length());

        HttpSession sesion = request.getSession(false);
        String rolSesion = (sesion != null) ? (String) sesion.getAttribute("rol") : null;

        // Sin sesión iniciada: al login
        if (rolSesion == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        // Rol que exige cada zona según el prefijo de la URL
        String rolRequerido = null;

        if (ruta.startsWith("/mesero")) {
            rolRequerido = usuario.ROL_MESERO;
        } else if (ruta.startsWith("/cajero")) {
            rolRequerido = usuario.ROL_CAJERO;
        } else if (ruta.startsWith("/admin")) {
            rolRequerido = usuario.ROL_ADMIN;
        } else if (ruta.startsWith("/cliente")) {
            rolRequerido = usuario.ROL_CLIENTE;
        }

        // Si el rol no coincide con la zona, también al login
        if (rolRequerido != null && !rolRequerido.equals(rolSesion)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        return true;
    }
}