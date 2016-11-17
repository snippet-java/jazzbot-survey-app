package com.ibm.sample.jazzbot.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class SurveyService {
	
	protected static JsonArray surveyArray;
    protected static Map<String, Integer> positionMap = new HashMap<String, Integer>(); 
	
	static {
		System.out.println("begin static");
		buildupArray();
	}

    private static JsonArray buildupArray() {
		//First get the raw text from survey.txt
		BufferedReader br = null;
		ArrayList<String> surveyList = new ArrayList<String>();
		try {

			String sCurrentLine;
			
			br = new BufferedReader(new FileReader(findFile("survey.txt")));
//			br = new BufferedReader(new FileReader("C:\\Users\\CEKL02\\Downloads\\Jazzbot\\survey app\\survey.txt"));

			while ((sCurrentLine = br.readLine()) != null) {
//    				System.out.println(sCurrentLine);
				surveyList.add(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		surveyArray = new JsonArray();
		//Split each row to question and answers (desc and  (if there is any) next action)
		
		try {
    		for(int itemCounter = 0; itemCounter < surveyList.size(); itemCounter++) {
    			String qaset = surveyList.get(itemCounter);
    			
    			String[] qaArray = qaset.split(";");
    			JsonObject qa = new JsonObject(); //<-- containing question and answers
    			
    			if(qaArray.length > 0) {
    				//set the question
        			//assuming first column is always Question
					qa.addProperty("question", qaArray[0]);
					
					//set the answers (could be one or more)
	    			JsonArray answerArray = new JsonArray();
	    			for(int i=1; i<qaArray.length; i++) {
	    				String answer = qaArray[i];
    					//split further if containing (/) character (meant for next action item)
	    				JsonObject answerObj = new JsonObject();
    					if(answer.contains("/")) {
    						String[] answerAndAction = answer.split("/");
	    					answerObj.addProperty("answer", answerAndAction[0]);
	    					answerObj.addProperty("nextquestion", Integer.parseInt(answerAndAction[1]) - 1);
    					}
    					else {
	    					answerObj.addProperty("answer", answer);
	    					answerObj.addProperty("nextquestion", itemCounter + 1);
    					}
    					answerArray.add(answerObj);
	    			}
	    			qa.add("answers", answerArray);
    			}
    			surveyArray.add(qa);
    		}
    		
		} catch(NumberFormatException ex) {
			System.out.println("Error importing survey set. Please check again your answer set. It contains invalid number to point to next question");
			//clear the survey output, to make sure survey file get fixed and complete survey output is delivered
			surveyArray = new JsonArray();
		}
		System.out.println(surveyArray);
		return surveyArray;
    }
    
    //return the question and answer sets
    private static String retrieveBySurveyPosition(int surveyPosition) {
    	JsonObject qaObj = surveyArray.get(surveyPosition).getAsJsonObject();
    	String outputText = qaObj.get("question").getAsString();
    	JsonArray answerSet = qaObj.get("answers").getAsJsonArray();
    	if(answerSet.size() > 0) {
    		for(int i=0; i<answerSet.size(); i++) {
    			outputText += "\n" + (i + 1) + ") " + answerSet.get(i).getAsJsonObject().get("answer").getAsString();
    		}
    	}
    	return outputText;
    }
    
    //for /start
    protected static String getNextQAObject(String sessionId) {
    	positionMap.put(sessionId, 0);
    	return retrieveBySurveyPosition(0);
    }
    
    //for /reply
    protected static String getNextQAObject(String sessionId, int option) {
    	
    	JsonObject prevObj = surveyArray.get(positionMap.get(sessionId).intValue()).getAsJsonObject();
    	JsonArray prevAnswerSet = prevObj.get("answers").getAsJsonArray();
    	if(prevAnswerSet.size() > 0) {
    		JsonObject answer = prevAnswerSet.get(option-1).getAsJsonObject();
    		int nextsurveyPosition = answer.get("nextquestion").getAsInt();
    		positionMap.put(sessionId, nextsurveyPosition);
    		return retrieveBySurveyPosition(nextsurveyPosition);
    	}
    	return null;
    }
    

	private static String findFile(String filename) {
		File root = new File(System.getProperty("user.dir"));
        try {
            boolean recursive = true;

            Collection<File> files = FileUtils.listFiles(root, new String[] {"txt"}, recursive);

            for (Iterator<File> iterator = files.iterator(); iterator.hasNext();) {
                File file = iterator.next();
                if (file.getName().equals(filename)) {
                	return file.getAbsolutePath();
                }
            }
            	
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
	}
}
