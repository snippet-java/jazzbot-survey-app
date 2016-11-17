package com.ibm.sample.jazzbot.app;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/start")
public class Start extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	String sessionId = request.getParameter("sessionId");
    	
    	String output = SurveyService.getNextQAObject(sessionId);
    	
    	response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(output);
		out.close();
    }
}
