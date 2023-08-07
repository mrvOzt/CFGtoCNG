package CFG_TO_CNG;

import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Main {
	
	static int control = 1;
	static ArrayList<String> cfgArr = new ArrayList<String>();
	public static void main(String[] args) throws FileNotFoundException {
		int lineControl = 0;
		Scanner cfgFile = new Scanner(new File("CFG.txt"));
		while(cfgFile.hasNext()) {
			if(lineControl > 0) {
				cfgArr.add(cfgFile.nextLine());
			}
			else {
				cfgFile.nextLine();
			}
			lineControl++;
		}
		System.out.println("CFG Form");
		System.out.println();
		for (int i = 0; i < cfgArr.size(); i++) {
			System.out.println(cfgArr.get(i));
		}
		System.out.println();
		System.out.println("-------------------------------");
		String controlNode = "";
		int nodeIndex;
		String line = "";
		for (int i = 0; i < cfgArr.size(); i++) {
			int cont1 = cfgArr.get(i).indexOf("-");
			int cont2 = cfgArr.get(i).lastIndexOf("S");
			if(cont2 > cont1) {
				cfgArr.add(0,"S0-S");
				System.out.println("Adding new start symbol");
				System.out.println();
				for (int j = 0; j < cfgArr.size(); j++) {
					System.out.println(cfgArr.get(j));
				}
				System.out.println();
				System.out.println("-------------------------------");
				break;
			}
		}
		int counter = 0;
		while(control>0) {
			control = 0;
			counter = 0;
			String eliminationNode = "";
			for (int i = 0; i < cfgArr.size(); i++) {
				line = cfgArr.get(i);
				if(line.contains("€")) {
					counter++;
					cfgArr.set(i, line.replace("€", ""));
					nodeIndex = line.indexOf("-");
					controlNode = String.valueOf(line.charAt(nodeIndex - 1 ));
				}
				if(controlNode != "") {
					for (int j = 0; j < cfgArr.size(); j++) {
						cfgArr.set(j,clearLine(cfgArr.get(j),controlNode,"€"));	
					}
					
				}
				eliminationNode = controlNode;
				controlNode = "";
				if(counter > 0) {
					break;
				}
			}
			System.out.println("Elimination " + eliminationNode + " -> €");
			System.out.println();
			for (int j = 0; j < cfgArr.size(); j++) {
				System.out.println(cfgArr.get(j));
			}
			System.out.println();
			System.out.println("-------------------------------");
		}
		
		for (int i = 0; i < cfgArr.size(); i++) {
			cfgArr.set(i, changeAlphabet(cfgArr.get(i),"0","W"));
			cfgArr.set(i, changeAlphabet(cfgArr.get(i),"1","Z"));
		}
		cfgArr.add("W-0");
		cfgArr.add("Z-1");
		System.out.println("Add W -> 0  ,  Z -> 1");
		System.out.println();
		for (int j = 0; j < cfgArr.size(); j++) {
			System.out.println(cfgArr.get(j));
		}
		System.out.println();
		System.out.println("-------------------------------");
		for (int i = 0; i < cfgArr.size(); i++) {
			cfgArr.set(i, eliminateUnitProd(cfgArr.get(i)));
		}
		for (int i = 0; i < cfgArr.size(); i++) {
			if(cfgArr.get(i).length() < 3) {
				cfgArr.remove(i);
				i--;
			}
		}
		cfgArr.add("W-0");
		cfgArr.add("Z-1");
		System.out.println("Eliminate Unit Production");
		System.out.println();
		for (int j = 0; j < cfgArr.size(); j++) {
			System.out.println(cfgArr.get(j));
		}
		System.out.println();
		System.out.println("-------------------------------");
		for (int i = 0; i < cfgArr.size(); i++) {
			cfgArr.set(i, changeAlphabet(cfgArr.get(i),"ZA","C"));
			cfgArr.set(i, changeAlphabet(cfgArr.get(i),"WB","D"));
			
		}
		cfgArr.add("C-ZA");
		cfgArr.add("D-WB");
		System.out.println("Break variable strings longer than 2");
		System.out.println();
		for (int j = 0; j < cfgArr.size(); j++) {
			System.out.println(cfgArr.get(j));
		}
		System.out.println();
		System.out.println("-------------------------------");
	}
	public static String eliminateUnitProd(String line) { 
			line = line.strip();
			int arrowIndex = line.indexOf("-");
			String productions = line.substring(arrowIndex + 1);
			productions = productions.strip();
			String[] lineArr = productions.split("\\|");
			if(productions.charAt(productions.length() - 1) == '|') {
				line = line.substring(0,line.length() - 1);
			}
			else if(productions.charAt(0) == '|') {
				line = line.replaceFirst("\\|", "");
			}
			for (int i = 0; i < lineArr.length; i++) {
					char[] chArr = lineArr[i].toCharArray();	
							if(lineArr[i].length() == 1) {
								String adding = findProd(lineArr[i],cfgArr);
								line  =line + "|" + adding;
								String remove = "|" + lineArr[i] + "|";
								line = line.replace(remove ,"|");
								remove = "-" + lineArr[i] + "|"; 	
								line = line.replace(remove ,"-");
							}			
			}
			return line;
		
		
	}
	public static String findProd(String Node,ArrayList<String> arr) {
		String prod = "";
		String line = "";
		for (int i = 0; i < arr.size(); i++) {
			if(arr.get(i).charAt(0) == Node.charAt(0)) {
				line = arr.get(i);
			}
		}
		line = line.strip();
		int arrowIndex = line.indexOf("-");
		String productions = line.substring(arrowIndex + 1);
		prod = productions.strip();
		
		
		return prod;
	}
	public static String changeAlphabet(String line,String oldCh,String newCh) {
		line = line.replace(oldCh,newCh);
		return line;
	}
	public static String clearLine(String line,String node,String newCh) {
		int counter = 0;
		if(line.contains(node)) { 
			line = line.strip();
			int arrowIndex = line.indexOf("-");
			String productions = line.substring(arrowIndex + 1);
			productions = productions.strip();
			String[] lineArr = productions.split("\\|");
			if(productions.charAt(productions.length() - 1) == '|') {
				line = line.substring(0,line.length() - 1);
			}
			else if(productions.charAt(0) == '|') {
				line = line.replaceFirst("\\|", "");
			}
			for (int i = 0; i < lineArr.length; i++) {
				if(lineArr[i].contains(node)) {
					char[] chArr = lineArr[i].toCharArray();	
					for (int j = 0; j < lineArr[i].length(); j++) {
						if(chArr[j] == node.charAt(0)) {
							counter++;
							if(lineArr[i].length() == 1) {
								line = line + "|" + "€";
								control++;
							}
							else {
								String s = lineArr[i].substring(0, j) + lineArr[i].substring(j + 1);
								line  = line + "|" + s;
								if(s.contains(node) && counter == 1) {
									line = line  + "|" + s.replace(node, "");
								}
							}				
						}
					}
				}
				counter = 0;
			}
		}
		return line;
	}

}
