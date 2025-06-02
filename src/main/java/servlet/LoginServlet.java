/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import com.google.gson.Gson;
import dao.AlumnowebJpaController;
import dto.Alumnoweb;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.persistence.EntityManagerFactory;
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
@WebServlet(name = "LoginServlet", urlPatterns = {"/loginservlet"})
public class LoginServlet extends HttpServlet {

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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String dni = request.getParameter("dni");
        String clave = request.getParameter("password");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.mycompany_racuGi01_war_1.0-SNAPSHOTPU");
        AlumnowebJpaController dao = new AlumnowebJpaController(emf);

        List<Alumnoweb> lista = dao.findAlumnowebEntities();

        for (Alumnoweb est : lista) {
            if (est.getNdniEstdWeb().equals(dni)) {
                if (PasswordUtil.checkPassword(clave, est.getPassEstd())) {

                    // 🔐 Genera el JWT
                    String token = JwtUtil.generarToken(est.getNdniEstdWeb());

                    // 💾 Guarda usuario y token en sesión
                    HttpSession sesion = request.getSession();
                    sesion.setAttribute("usuario", est);
                    sesion.setAttribute("token", token);

                    // 🚀 Redirige a principal.html
                    response.setContentType("application/json");
                    response.getWriter().write("{\"success\": true}");
                    return;
                }
                break;
            }
        }

        // ❌ Si no coincide
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"error\": \"Credenciales inválidas\"}");
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
