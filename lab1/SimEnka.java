package hr.fer.utr.labosi.lab1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class SimEnka {
	
	private static ArrayList<String> nizovi = new ArrayList<String>();
	private static ArrayList<String> skupStanja = new ArrayList<String>();
	private static ArrayList<String> simboliAbecede = new ArrayList<String>();
	private static ArrayList<String> prihvatljivaStanja = new ArrayList<String>();
	private static String pocetnoStanje;

	private static ArrayList<String> stanjaDefiniranihPrijelaza = new ArrayList<String>();
	private static ArrayList<String> znakoviPrijelaza = new ArrayList<String>();
	private static ArrayList<String> iducaStanja = new ArrayList<String>();

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
		// 3.redak --> skup simbola abecede
		String[] sigma = reader.readLine().split(",");
		for(String simbol : sigma){
			simboliAbecede.add(simbol);
		}
		// 4.redak --> skup prihvatljivih stanja
		String[] accepting = reader.readLine().split(",");
		for(String stanje : accepting){
			prihvatljivaStanja.add(stanje);
		}
		// 5.redak --> pocetno stanje
	    	pocetnoStanje = reader.readLine(); 
	        
	   	 // 6.redak --> Spremanje funkcije prijelaza (3 paralelne liste):     - trenutnaStanja
	   	 //                                                                   - simboliAbecede
	    	//                                                                   - iducaStanja
	    	String iterator = new String();	
	    	while ((iterator = reader.readLine()) != null) {
	    		String[] podjela = iterator.split("->");	    	
	    		String[] lijevo = podjela[0].split(",");    		    	
	    		String[] desno = podjela[1].split(",");
	    		for (int j = 0; j < desno.length; j++) {
	    			stanjaDefiniranihPrijelaza.add(lijevo[0]);
	    			znakoviPrijelaza.add(lijevo[1]);
	    			iducaStanja.add(desno[j]);
	    		}
	    	}
	    
	   	/*
	    	System.out.println("nizovi --> " + nizovi.toString());
	    	System.out.println("skupStanja --> " + skupStanja.toString());
	    	System.out.println("simboliAbecede --> " + simboliAbecede.toString());
	    	System.out.println("prihvatljivaStanja --> " + prihvatljivaStanja.toString());
	    	System.out.println("pocetnoStanje --> " + pocetnoStanje.toString());
	    	System.out.println();
	    	System.out.println("stanjaDefiniranihPrijelaza --> " + stanjaDefiniranihPrijelaza.toString());
	    	System.out.println("procitaniZnakovi --> " + procitaniZnakovi.toString());
	    	System.out.println("iducaStanja --> " + iducaStanja.toString());
	    	*/
	    
		
	    	// OBRADA PODATAKA AUTOMATOM
	    	for (String niz : nizovi) {
	    	
	    		String[] simboli = niz.split(",");    // splitanje niza na simbole
	    	
	    		// Definiranje triju novih listi za obradu i spremanje stanja
	    		ArrayList<String> trenutnaStanja = new ArrayList<String>();
	    		ArrayList<String> sljedecaStanja = new ArrayList<String>(); 
	    		ArrayList<String> sviPrijelazi = new ArrayList<String>();
	    	
			// U trenutna stanja moramo staviti "pocetnu tocku"
            		trenutnaStanja.add(pocetnoStanje);

            		// Krecemo u obradu simbola niz po niz
            		for (int k = 0; k < simboli.length; k++) {
            	
            			// Provjeri epsilon-prijelaze trenutnih stanja          	
        			while(true) {     
        				for(int i = 0; i < trenutnaStanja.size(); i++) {                                                                         // Za svako stanje koje trenutno imamo
        					for(int j = 0; j < stanjaDefiniranihPrijelaza.size(); j++) {  	                                                     // Prodi po listi stanja s definiranim prijelazima
        						if(znakoviPrijelaza.get(j).equals("$") && trenutnaStanja.get(i).equals(stanjaDefiniranihPrijelaza.get(j))) {     // Ako u listi stanja s definiranim prijelazima postoji epsilon-prijelaz za ovo stanje
        						
        							// Dodaj stanje koje dobivamo epsilon-prijelazom ako ono vec nije u listi stanja koja trenutno imamo
        							String iduce = iducaStanja.get(j);             
        							if (!trenutnaStanja.contains(iduce)){
        								trenutnaStanja.add(iduce);
        								continue; 				          // nastavi na sljedeci korak				
        							}
        						}
        					}
        				}
        				// Kad su provjereni svi epsilon-prijelazi i vise nema ni jednog, izadi iz while petlje
        				break;
        			}
        		
        			// Ako je dodan prijelaz u prazan skup, obrisi ga
    				if(trenutnaStanja.contains("#")) {
    					trenutnaStanja.remove("#");
    				}
    			
    				// Dodavanje stanja u koja su definirani prijelazi u sljedecaStanja      
    				for(int i = 0; i < trenutnaStanja.size(); i++) {												                                // Za svako stanje koje trenutno imamo		
    					for(int j = 0; j < stanjaDefiniranihPrijelaza.size(); j++) {																// Prodi po listi stanja s definiranim prijelazima			
    						if(znakoviPrijelaza.get(j).equals(simboli[k])  && trenutnaStanja.get(i).equals(stanjaDefiniranihPrijelaza.get(j))) {    // Ako postoji definirani prijelaz iz ovog stanja za trenutni simbol
    							// U sljedecaStanja dodaj definirani prijelaz (ako ono vec nije prisutno)
    							String iduce = iducaStanja.get(j);             
    							if (!sljedecaStanja.contains(iduce)){
    								sljedecaStanja.add(iduce);
    							}
    						}
    					}
    				}
        		
    				// Provjeri epsilon-prijelaze sljedecih stanja          	
            			while(true) {     
            				for(int i = 0; i < sljedecaStanja.size(); i++) {                                                                         // Za svako stanje koje trenutno imamo
            					for(int j = 0; j < stanjaDefiniranihPrijelaza.size(); j++) {  	                                                     // Prodi po listi stanja s definiranim prijelazima
            						if(znakoviPrijelaza.get(j).equals("$") && sljedecaStanja.get(i).equals(stanjaDefiniranihPrijelaza.get(j))) {     // Ako u listi stanja s definiranim prijelazima postoji epsilon-prijelaz za ovo stanje
            						
            							// Dodaj stanje koje dobivamo epsilon-prijelazom ako ono vec nije u listi stanja koja trenutno imamo.
            							String iduca = iducaStanja.get(j);             
            							if (!sljedecaStanja.contains(iduca)){
            								sljedecaStanja.add(iduca);
            								continue; 				          // nastavi na sljedeci korak				
            							}
            						}
            					}
            				}
            				// Kad su provjereni svi epsilon-prijelazi i vise nema ni jednog, izadi iz while petlje
            				break;
            			}
            	
                		if(sljedecaStanja.isEmpty()) sljedecaStanja.add("#");                                        // Ako nije zabiljezen ni jedan prijelaz, dodaj prijelaz u prazan skup
    				if(sljedecaStanja.size() != 1 && sljedecaStanja.contains("#")) sljedecaStanja.remove("#");   // Ako za neko stanje postoji prijelaz a za drugo ga nema, ne smijemo zaboraviti maknuti prazan skup
 
    				Collections.sort(trenutnaStanja);            // Sortiranje trenutnih stanja
    			
    				// Dodavanje epsilon-prijelaza u listu za ispis
    				for(String stanje : trenutnaStanja) {
    					if(!sviPrijelazi.contains(stanje)){
    						sviPrijelazi.add(stanje);
    						sviPrijelazi.add(",");
    					}
    				}	
    				sviPrijelazi.set(sviPrijelazi.size() - 1, "|");             // Dodavanje pregrade koja oznacava kraj stanja
    				trenutnaStanja.clear();                                     // Brisanje liste trenutnih stanja koja su "rijesena"
    				trenutnaStanja.addAll(sljedecaStanja);                      // Dodavanje sljedecih stanja u listu trenutnih jer krece njihova obrada
    			
    				Collections.sort(sljedecaStanja);            // Sortiranje sljedecih stanja
            	 
    				// Dodavanje svih ostalih prijelaza u listu za ispis  
    				for(String stanje : sljedecaStanja) {
    					sviPrijelazi.add(stanje);
    					sviPrijelazi.add(",");
    				}	
    				sviPrijelazi.set(sviPrijelazi.size() - 1, "|");             // Dodavanje pregrade koja oznacava kraj stanja
    				sljedecaStanja.clear();                                     // Brisanje liste sljedecih stanja kako bi je mogli koristiti u obradi sljedecih parova stanja
            		}
            
            		sviPrijelazi.remove(sviPrijelazi.size() - 1);                   // Brisanje pregrade sa kraja niza

            		// ISPIS stanja 
            		for (String ispis : sviPrijelazi) {
            			System.out.printf(ispis);
            		}
            		System.out.println();
	    	} 
	    
	    	reader.close();
	}

}
