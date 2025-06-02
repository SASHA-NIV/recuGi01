/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.AlumnowebJpaController;
import dto.Alumnoweb;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.JwtUtil;

/**
 *
 * @author SASHA
 */
@WebServlet(name = "ListarAlumnos", urlPatterns = {"/listaralumnos"})
public class ListarAlumnos extends HttpServlet {

    
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
        HttpSession session = request.getSession(false);
        response.setContentType("application/json;charset=UTF-8");

        // Verificación de token
        if (session == null || session.getAttribute("token") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Token no encontrado\"}");
            return;
        }

        String token = (String) session.getAttribute("token");
        if (!JwtUtil.validarToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Token inválido o expirado\"}");
            return;
        }

        AlumnowebJpaController ctrl = new AlumnowebJpaController(Persistence.createEntityManagerFactory("com.mycompany_racuGi01_war_1.0-SNAPSHOTPU"));
        List<Alumnoweb> lista = ctrl.findAlumnowebEntities();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        StringBuilder json = new StringBuilder();
        json.append("[");

        for (int i = 0; i < lista.size(); i++) {
            Alumnoweb a = lista.get(i);
            json.append("{")
                .append("\"codiEstdWeb\":").append(a.getCodiEstdWeb()).append(",")
                .append("\"ndniEstdWeb\":\"").append(a.getNdniEstdWeb()).append("\",")
                .append("\"appaEstdWeb\":\"").append(a.getAppaEstdWeb()).append("\",")
                .append("\"apmaEstdWeb\":\"").append(a.getApmaEstdWeb()).append("\",")
                .append("\"nombEstdWeb\":\"").append(a.getNombEstdWeb()).append("\",")
                .append("\"fechNaciEstdWeb\":\"").append(sdf.format(a.getFechNaciEstdWeb())).append("\",")
                .append("\"logiEstd\":\"").append(a.getLogiEstd()).append("\"")
                .append("}");

            if (i < lista.size() - 1) {
                json.append(",");
            }
        }

        json.append("]");

        try (PrintWriter out = response.getWriter()) {
            out.print(json.toString());
        }
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
        processRequest(request, response);
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
