package Reports;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NounParser {
	Connection con;
	
	public NounParser(Connection con) {
		this.con = con;
	}
	
	public void runParser() {
		String listingQuery = "select host, str_addr, latitude, longitude from listing";
		InputStream modelInParse = null;
		Parser parser = null;
		try {
			//load chunking model
			modelInParse = new FileInputStream("C:\\Users\\jerem\\OneDrive - University of Toronto\\Documents\\School Stuff\\Summer_2023\\CSCC43\\CSCC43\\src\\Reports\\en-parser-chunking.bin"); //from http://opennlp.sourceforge.net/models-1.5/
			ParserModel model = new ParserModel(modelInParse);
			
			//create parse tree
			parser = ParserFactory.create(model);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Statement statement = con.createStatement();
			Statement innerStatement = con.createStatement();
			ResultSet rs = statement.executeQuery(listingQuery);
			while (rs.next() == true) {
				String latitude = rs.getString("latitude");
				String longitude = rs.getString("longitude");
				String str_addr = rs.getString("str_addr");
				System.out.println("----------------------------------");
				System.out.println("Noun Phrases for " + str_addr + ":");
				
				String nounQuery = "select comments from h_review where l_latitude = " + latitude + " and l_longitude = " + longitude 
							+ " union select comments from u_review where l_latitude = " + latitude + " and l_longitude = " + longitude + ";";
				ResultSet comments = innerStatement.executeQuery(nounQuery);
				Set<String> nounPhrases = new HashSet<>();
				try {
					while (comments.next() == true) {
						Parse topParses[] = ParserTool.parseLine(comments.getString("comments"), parser, 1);
						
						//call subroutine to extract noun phrases
						for (Parse p : topParses)
							getNounPhrases(p, nounPhrases);
						
						//print noun phrases
						for (String s : nounPhrases)
						    System.out.println(s);
						System.out.println("-----------------");
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				finally {
				  if (modelInParse != null) {
				    try {
				    	modelInParse.close();
				    }
				    catch (IOException e) {
				    }
				  }
				  comments.close();
				}
			}
			rs.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//recursively loop through tree, extracting noun phrases
	public void getNounPhrases(Parse p, Set<String> nounPhrases) {
			
	    if (p.getType().equals("NP")) { //NP=noun phrase
	         nounPhrases.add(p.getCoveredText());
	    }
	    for (Parse child : p.getChildren())
	         getNounPhrases(child, nounPhrases);
	}
}
