/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.tryzub.validators;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 *
 * @author tszin
 */
public class ValidWords {
    
	Vector v = new Vector();

	public ValidWords() {
		try {
			InputStreamReader reader = new InputStreamReader(this.getClass().getResourceAsStream("F:\\MyProjects\\tryzub\\photo\\wordlist\\wordlist.txt"), "utf-8");
			BufferedReader in = new BufferedReader(reader);
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				v.add(inputLine);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

   
	
	public boolean contains(String word) {
		return v.contains(word);
	}

	
	public int size() {
		return v.size();
	}
}
