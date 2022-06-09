//package hr.fer.utr.labosi.lab3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SimPa {
	
	private static ArrayList<String> nizovi = new ArrayList<String>();
	private static ArrayList<String> skupStanja = new ArrayList<String>();
	private static ArrayList<String> simboliAbecede = new ArrayList<String>();
	private static ArrayList<String> simboliStoga = new ArrayList<String>();
	private static ArrayList<String> prihvatljivaStanja = new ArrayList<String>();
	private static String pocetnoStanje;
	private static String pocetniZnakStoga;

	private static ArrayList<String> stanjaDefiniranihPrijelaza = new ArrayList<String>();
	private static ArrayList<String> znakoviPrijelaza = new ArrayList<String>();
	private static ArrayList<String> simboliStogaPrijelaza = new ArrayList<String>();
	private static ArrayList<String> iducaStanja = new ArrayList<String>();
	private static ArrayList<String> iduciSimboliStoga = new ArrayList<String>();
	
	private static File file = new File("source.txt");

	public static void main(String[] args) throws IOException {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		// 1.redak --> ulazni nizovi
		String[] nizovi2 = reader.readLine().split("\\|");
		for(String niz : nizovi2){
			nizovi.add(niz);
		}
		// 2.redak --> skup stanja
		String[] omega = reader.readLine().split(",");
		for(String stanje : omega){
			skupStanja.add(stanje);
		}
		// 3.redak --> skup ulaznih znakova
		String[] sigma = reader.readLine().split(",");
		for(String simbol : sigma){
			simboliAbecede.add(simbol);
		}
		// 4.redak --> skup znakova stoga
		String[] stackElements = reader.readLine().split(",");
		for(String elem : stackElements){
			simboliStoga.add(elem);
		}
		// 5.redak --> skup prihvatljivih stanja
		String[] accepting = reader.readLine().split(",");
		for(String stanje : accepting){
			prihvatljivaStanja.add(stanje);
		}
		// 5.redak --> pocetno stanje
	    pocetnoStanje = reader.readLine(); 
	    // 6.redak --> pocetni znak stoga
	    pocetniZnakStoga = reader.readLine();
	        
	    // 6.redak --> Spremanje funkcije prijelaza (5 paralelnih lista):     - trenutnoStanje
	    //                                                                    - ulazniZnak
	    //                                                                    - znakStoga
	    //                                                                    - novoStanje
	    //                                                                    - nizZnakovaStoga
	    String iterator = new String();	
	    while ((iterator = reader.readLine()) != null) {
	    	String[] podjela = iterator.split("->");	    	
	    	String[] lijevo = podjela[0].split(",");    		    	
	    	String[] desno = podjela[1].split(",");
	    	stanjaDefiniranihPrijelaza.add(lijevo[0]);
	    	znakoviPrijelaza.add(lijevo[1]);
	    	simboliStogaPrijelaza.add(lijevo[2]);
	    	iducaStanja.add(desno[0]);
	    	iduciSimboliStoga.add(desno[1]);
	    }
	    
	    /*
	    System.out.println("nizovi --> " + nizovi.toString());
	    System.out.println("skupStanja --> " + skupStanja.toString());
	    System.out.println("simboliAbecede --> " + simboliAbecede.toString());
	    System.out.println("simboliStoga --> " + simboliStoga.toString());
	    System.out.println("prihvatljivaStanja --> " + prihvatljivaStanja.toString());
	    System.out.println("pocetnoStanje --> " + pocetnoStanje.toString());
	    System.out.println("pocetniZnakStoga --> " + pocetniZnakStoga.toString());
	    System.out.println();
	    System.out.println("stanjaDefiniranihPrijelaza --> " + stanjaDefiniranihPrijelaza.toString());
	    System.out.println("znakoviPrijelaza --> " + znakoviPrijelaza.toString());
	    System.out.println("simboliStogaPrijelaza --> " + simboliStogaPrijelaza.toString());
	    System.out.println("iducaStanja --> " + iducaStanja.toString());
	    System.out.println("iduciSimboliStoga --> " + iduciSimboliStoga.toString());
	    */
	 
		
	    // OBRADA PODATAKA AUTOMATOM
	    for (String niz : nizovi) {
	    	
	    	String izlaz = new String("");
	    	izlaz += pocetnoStanje + "#" + pocetniZnakStoga + "|";
	    	
	    	String[] simboli = niz.split(",");    // splitanje niza na simbole
	    	boolean prihvaca = true;
	    	
	    	String stanje = new String(pocetnoStanje);
	    	String stog = new String(pocetniZnakStoga);

            // Krecemo u obradu simbola
	    	int k = 0;
            while (k < simboli.length) {
            	
            	boolean postojiEpsilonPrijelaz = false;
            	boolean postojiPrijelaz = false;
            	String prviZnakStoga = new String("");
        		prviZnakStoga += stog.charAt(0);	
        		
        		// Provjeravamo epsilon prijelaze
            	for (int i = 0; i < stanjaDefiniranihPrijelaza.size(); i++) {
            		if (stanjaDefiniranihPrijelaza.get(i).equals(stanje) && znakoviPrijelaza.get(i).equals("$") && simboliStogaPrijelaza.get(i).equals(prviZnakStoga)) {
            			stanje = iducaStanja.get(i);
            			stog = iduciSimboliStoga.get(i) + stog.substring(1);	
            			izlaz += stanje + "#" + stog + "|";
            			postojiEpsilonPrijelaz = true;
            			break;
            		}
            	}
            	if (postojiEpsilonPrijelaz) continue;
            	
            	//System.out.println("Stanje: " + stanje + "\nStog: " + stog + "\nk = " + k);
        		
            	for (int i = 0; i < stanjaDefiniranihPrijelaza.size(); i++) {
            		if (stanjaDefiniranihPrijelaza.get(i).equals(stanje) && znakoviPrijelaza.get(i).equals(simboli[k]) && simboliStogaPrijelaza.get(i).equals(prviZnakStoga)) {
            			stanje = iducaStanja.get(i);
            			if (iduciSimboliStoga.get(i).equals("$")) {
            				stog = stog.substring(1);
            			} else {
                			stog = iduciSimboliStoga.get(i) + stog.substring(1);      				
            			}
            			izlaz += stanje + "#" + stog + "|";
            			postojiPrijelaz = true;
            			break;
            		}
            	}
            	k++;
            	if (postojiEpsilonPrijelaz == false && postojiPrijelaz == false) {
            		if (k <= simboli.length) {
            			prihvaca = false;
            		}
            		break;
            	}
            }
            
            // Jos jednom provjeravamo epsilon prijelaze
            String prviZnakStoga = new String("");
    		prviZnakStoga += stog.charAt(0);
        	for (int i = 0; i < stanjaDefiniranihPrijelaza.size(); i++) {
        		if (stanjaDefiniranihPrijelaza.get(i).equals(stanje) && znakoviPrijelaza.get(i).equals("$") && simboliStogaPrijelaza.get(i).equals(prviZnakStoga)) {
        			stanje = iducaStanja.get(i);
        			stog = iduciSimboliStoga.get(i);	
        			izlaz += stanje + "#" + stog + "|";
        			break;
        		}
        	}
            
            if (prihvaca == false) {
            	izlaz += "fail|0";
            } else if (prihvatljivaStanja.contains(stanje)) {
            	izlaz += "1";
            } else {
            	izlaz += "0";
            }
            
            System.out.println(izlaz);
            
	    } 
	    reader.close();
	}
	    
	public static String[] addX(int n, String arr[], String x) {
	    
		// create a new array of size n+1
	    String newarr[] = new String[n + 1];
	    
	    for (int i = 0; i < n; i++) {
	    	newarr[i] = arr[i];
	    }
	    newarr[n] = x;
	  
	    return newarr;
	}

}