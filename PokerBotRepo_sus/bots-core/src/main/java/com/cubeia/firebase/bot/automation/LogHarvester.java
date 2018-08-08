/**
 * Copyright (C) 2010 Cubeia Ltd <info@cubeia.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.cubeia.firebase.bot.automation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class LogHarvester {
	
	
	/**
	 * Converts the output of the stats logfile to a spreadsheet friendly format
	 * @param inFile 
	 * @param outFile
	 * @throws IOException
	 */
	public void processLogFile(String inFile, String outFile) throws IOException {
	
        BufferedReader in = new BufferedReader(new FileReader(inFile));
        BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
        String str;
        
        // Skip the first three lines, nothing important here - move along!
        str = in.readLine();
        str = in.readLine();
        str = in.readLine();

        boolean headerPrinted = false;
        while ((str = in.readLine()) != null) {
        	Scanner scanner = new Scanner(str);
        	if ( !headerPrinted ) {
        		printHeader(scanner, out);
        		headerPrinted = true;
        	}
        	scanner = new Scanner(str);
        	printStatLine(scanner, out);
        	
        }
        in.close();
        out.close();
	}    

	/**
	 * Extract and print header information to file
	 * @param scanner
	 * @param out
	 * @throws IOException 
	 */
	public static void printHeader(Scanner scanner, BufferedWriter out) throws IOException {
		
		// skip the first two entries (date & time)
		if ( scanner.hasNext() ) {
			scanner.next();
		}
		if ( scanner.hasNext() ) {
			scanner.next();
		}
		
		while ( scanner.hasNext() ) {
			String header = scanner.next();
			if ( !Pattern.matches("[0-9]+", header) ) {
				System.out.print("\t" +header);
				out.write("\t" +header);
			}
		}
		System.out.println("");
		out.newLine();
	}
	
	/**
	 * Extract and print stats information to file
	 * @param scanner
	 * @param out
	 * @throws IOException 
	 */
	public static void printStatLine(Scanner scanner, BufferedWriter out) throws IOException {
		
		while ( scanner.hasNext() ) {
			String value = scanner.next();
			if ( Pattern.matches("[0-9]+", value) ) {
				System.out.print("\t" +value);
				out.write("\t" +value);
			}
		}
		System.out.println("");
		out.newLine();
	}
	
}
