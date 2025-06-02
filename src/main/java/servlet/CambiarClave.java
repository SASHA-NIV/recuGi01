/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.AlumnowebJpaController;
import dto.Alumnoweb;
import java.io.IOException;
import java.io.PrintWriter;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.JwtUtil;
import util.PasswordUtil;

/**
 *
 * @author SASHA
 */
@WebServlet(name = "CambiarClave", urlPatterns = {"/cambiarclave"})
public class CambiarClave extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        response.setContentType("text/plain");

        if (session == null || session.getAttribute("usuario") == null || session.getAttribute("token") == null) {
            response.getWriter().write("❌ No hay sesión activa o token faltante.");
            return;
        }

        String token = (String) session.getAttribute("token");
        if (!JwtUtil.validarToken(token)) {
            response.getWriter().write("❌ Token inválido o expirado.");
            return;
        }

        String claveActual = request.getParameter("actual");
        String nuevaClave = request.getParameter("nueva");

        Alumnoweb usuario = (Alumnoweb) session.getAttribute("usuario");
        AlumnowebJpaController dao = new AlumnowebJpaController(Persistence.createEntityManagerFactory("com.mycompany_racuGi01_war_1.0-SNAPSHOTPU"));

        // Verificar clave actual
        if (!PasswordUtil.checkPassword(claveActual, usuario.getPassEstd())) {
            response.getWriter().write("❌ La clave actual es incorrecta.");
            return;
        }

        // Cifrar la nueva clave
        String hashNueva = PasswordUtil.hashPassword(nuevaClave);
        usuario.setPassEstd(hashNueva);

        try {
            dao.edit(usuario); // actualizar en base de datos
            response.setContentType("text/plain; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("✅ Clave actualizada con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("❌ Error al actualizar la clave.");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
