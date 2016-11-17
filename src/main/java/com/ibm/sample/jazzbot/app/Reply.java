package com.ibm.sample.jazzbot.app;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/reply")
public class Reply extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	String sessionId = request.getParameter("sessionId");
    	String option = request.getParameter("text");
    	
    	String output = SurveyService.getNextQAObject(sessionId, Integer.parseInt(option));
    	
    	response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(output);
		out.close();
    }
    
//    public static void main(String[] args) {
//    	System.out.println("Map Size: " + SurveyService.positionMap.size());
//    	System.out.println(SurveyService.getNextQAObject("12345"));
//    	System.out.println(SurveyService.getNextQAObject("12345", 1));
//    	System.out.println(SurveyService.getNextQAObject("12345", 3));
//    	System.out.println("2 Map Size: " + SurveyService.positionMap.size());
//    	System.out.println("-------------------------------------");
//    	System.out.println(SurveyService.getNextQAObject("23456"));
//    	System.out.println(SurveyService.getNextQAObject("23456", 1));
//    	System.out.println(SurveyService.getNextQAObject("23456", 2));
//    	System.out.println("3 Map Size: " + SurveyService.positionMap.size());
//    }
}
