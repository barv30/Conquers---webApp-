package Server.Servlets.GameRoomServlets;

import EnginePack.ArmyUnit;
import EnginePack.GameEngine;
import EnginePack.GameTerritory;
import Games.GamesManager;
import Server.Utils.ResultOfRequest;
import Server.Utils.ServletUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static java.lang.Integer.parseInt;

@WebServlet(name = "ArmyInTerritoryServlet", urlPatterns = {"/pages/GameRoom/ArmyInTerritory"})
public class ArmyInTerritoryServlet extends HttpServlet {

    // urls that starts with forward slash '/' are considered absolute
    // urls that doesn't start with forward slash '/' are considered relative to the place where this servlet request comes from
    // you can use absolute paths, but then you need to build them from scratch, starting from the context path
    // ( can be fetched from request.getContextPath() ) and then the 'absolute' path from it.
    // Each method with it's pros and cons...
    // must start with '/' since will be used in request dispatcher...
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

        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        GamesManager gamesManager = ServletUtils.getGamesManager(getServletContext());
        GameEngine gameEngine=gamesManager.findGameController((String)(request.getSession(false).getAttribute("gameName"))).getGameEngine();
        String row = request.getParameter("r");
        String col = request.getParameter("c");
        int rowNumber = parseInt(row);
        int colNumber = parseInt(col);
        GameTerritory territory = gameEngine.getBoard().getBoardGame()[rowNumber][colNumber];
        List<ArmyUnit> unitInTerritory = territory.getUnitInTerritory();

        String units=" ";
        for(int i=0;i<unitInTerritory.size();i++)
        {
            String dataString = (i+1)+"."+"Type: " +unitInTerritory.get(i).getType() +','+ " Current Power: "+unitInTerritory.get(i).getCurrentPower()+"," + "Max fire power: "+unitInTerritory.get(i).getMaxFirePower()+ '\n';
            units=units+dataString;
        }

        String result = "Your ARMY in this territory: " + '\n' + units;

        out.println(gson.toJson(result));

    }
//we need to check in all servelt if doesnt have session move him to index page.
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

