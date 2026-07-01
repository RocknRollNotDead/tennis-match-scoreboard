package ru.codeportfolio.servlets;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.codeportfolio.models.Match;

import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/currency/*", "/currencies"})

@Controller
@RequestMapping("/matches")
public class MatchesController {


    private final Gson gson = new Gson();

    @RequestMapping("/")
    public List<Match> getMatches(){
        return new ArrayList<>();
    }
/*
    public void init(){

        DataSource dataSource = (DataSource) getServletContext().getAttribute("dataSource");
        currencyService = new CurrencyService(dataSource);
    }


    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String servletPath = req.getServletPath();
        String path = req.getPathInfo();

        String json;
        String code;
        if (servletPath.equals("/currencies")) {
            json = gson.toJson(currencyService.getAllCurrencies());
        } else {
            try{
                code = path.substring(1);
            } catch (NullPointerException e){
                throw new UncorrectRequestException("request must be not null");
            }

            if (code.length() == 3){

                json = gson.toJson(currencyService.getCurrency(code));
            } else {
                throw new UncorrectRequestException("Uncorrect request. Request must have 3 symbols");
            }
        }

        resp.setStatus(HttpServletResponse.SC_OK); // 200
        resp.getWriter().write(json);

    }


    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String code = req.getParameter("name");
        String name  = req.getParameter("name");
        String sign = req.getParameter("sign");

        CurrencyDto result = currencyService.addCurrency(code, name, sign);


        if (result != null){
            resp.setStatus(HttpServletResponse.SC_CREATED); // 201
        }

        String json = gson.toJson(result);
        resp.getWriter().write(json);

    }*/

}

