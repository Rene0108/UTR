package hr.fer.utr.labosi.lab2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

public class MinDka {
	
	private static TreeSet<String> skupStanja = new TreeSet<String>();
	private static TreeSet<String> simboliAbecede = new TreeSet<String>();
	private static TreeSet<String> prihvatljivaStanja = new TreeSet<String>();
	private static String pocetnoStanje;

	private static Map<String,String> definiraniPrijelazi = new TreeMap<>();
	private static TreeSet<String> nedostiznaStanja = new TreeSet<String>();
	private static Map<ParoviStanja, List<ParoviStanja>> mapaSaListama = new HashMap<>();
	private static Map<ParoviStanja, Boolean> matrica = new HashMap<ParoviStanja, Boolean>();
	private static List<ParoviStanja> istovjetnaStanja = new ArrayList<>();
	
	public static void main(String[] args) throws IOException {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		// 1.redak --> skup stanja
		String[] omega = reader.readLine().split(",");
		for(String stanje : omega){
			skupStanja.add(stanje);
		}
		//System.out.println("Skup stanja:   " + skupStanja.toString());
		
		// 2.redak --> skup simbola abecede
		String[] sigma = reader.readLine().split(",");
		for(String simbol : sigma){
			simboliAbecede.add(simbol);
		}
		//System.out.println("Simboli abecede:   " + simboliAbecede.toString());
		
		// 3.redak --> skup prihvatljivih stanja
		String[] accepting = reader.readLine().split(",");
		for(String stanje : accepting){
			prihvatljivaStanja.add(stanje);
		}
		//System.out.println("Prihvatljiva stanja:   " + prihvatljivaStanja.toString());
		
		// 4.redak --> pocetno stanje
	    	pocetnoStanje = reader.readLine();
	    	//System.out.println("Pocetno stanje:   " + pocetnoStanje.toString());
	        
	   	 // 5.redak --> Spremanje prijelaza
	   	 String linija = new String();	
	    	while ((linija = reader.readLine()) != null) {
	    		String[] podjela = linija.split("->");
	    		definiraniPrijelazi.put(podjela[0], podjela[1]);
	    	}
	   	 //System.out.println("Prijelazi:   \n" + definiraniPrijelazi.toString() + "\n\n");
	    
	    
	    	// TRAZENJE I UKLANJANJE NEDOSTIZNIH STANJA
	    	for(String stanje : skupStanja) {
	    		nedostiznaStanja.add(stanje);
	    	}
	    	//System.out.println("U nedostizna stanja stavi sva stanja:   " + nedostiznaStanja.toString());
	    
	    	TreeSet<String> dohvatljiva = new TreeSet<String>();
	    	dohvatljiva.add(pocetnoStanje);
	    
	    	TreeSet<String> trenutno = new TreeSet<String>();
	    	trenutno.add(pocetnoStanje);
	    
	   	 while(trenutno.size() > 0) {
	    		TreeSet<String> pomoc = new TreeSet<String>();
	    		for (@SuppressWarnings("unused") String stanje : trenutno) {
	    			for(Entry<String, String> entry : definiraniPrijelazi.entrySet()) {
	    				String[] podjela = entry.getKey().split(",");
	    				if(trenutno.contains(podjela[0]) && !(dohvatljiva.contains(entry.getValue()))) {
	    					pomoc.add(entry.getValue());
	    					dohvatljiva.add(entry.getValue());
	    				}
	    			}
	    		}
	    		trenutno.clear();
	    		trenutno.addAll(pomoc);
	    	}
	    
	    	for(String stanje : dohvatljiva) {
	    		if(nedostiznaStanja.contains(stanje)) {
	    			nedostiznaStanja.remove(stanje);
	    		}
	    	}	    
	    	for(String stanje : nedostiznaStanja) {
	    		if(prihvatljivaStanja.contains(stanje)) {
	    			prihvatljivaStanja.remove(stanje);
	    		}
	    		if(skupStanja.contains(stanje)) {
	    			skupStanja.remove(stanje);
	    		}
	    	}
	    	//System.out.println("Nedostizna stanja:   " + nedostiznaStanja.toString());
	    	//System.out.println("Nedostizna stanja size:   " + nedostiznaStanja.size());

	   	 Map<String, String> prijelaziNovo = new TreeMap<>();
	    	 for(Map.Entry<String, String> entry : definiraniPrijelazi.entrySet()) {
			String[] podjela = entry.getKey().split(",");
			if(!(nedostiznaStanja.contains(podjela[0])) && !(nedostiznaStanja.contains(entry.getValue()))) {
				prijelaziNovo.put(entry.getKey(), entry.getValue());
			}
		}
	    	//System.out.println("Skup stanja:   " + skupStanja.toString());
	    	//System.out.println("Simboli abecede:   " + simboliAbecede.toString());
	    	//System.out.println("Prihvatljiva stanja:   " + prihvatljivaStanja.toString());
	    	//System.out.println("Prijelazi:   \n" + prijelaziNovo.toString() + "\n\n");
	    
	    
	    	// Stvaranje matrice i inicijalizacija svih elemenata na 'false'
	    	List<ParoviStanja> paroviStanja = new ArrayList<ParoviStanja>();
	    	String[] stanja = skupStanja.toArray( new String[skupStanja.size()] );
	    

	    	for(int i = 0; i < stanja.length; i++) {
	    		for(int j = 0; j < stanja.length; j++) {
	    			ParoviStanja noviPar = new ParoviStanja(stanja[i], stanja[j]);
	    			paroviStanja.add(noviPar);
	    		}
	    	}
	    	for(ParoviStanja par : paroviStanja) {
	    		matrica.put(par, false);
		}
	    	//System.out.println(paroviStanja.toString());
	    
	    	// Oznacavanje neistovjetnih parova stanja
	   	for(int i = 0; i < stanja.length; i++) {
	    		for(int j = 0; j < i; j++) {
	    			ParoviStanja par = paroviStanja.get(i * (stanja.length) + j);
	    			if ( (!(prihvatljivaStanja.contains(par.x)) && prihvatljivaStanja.contains(par.y)) ||
	    			     (prihvatljivaStanja.contains(par.x) && !(prihvatljivaStanja.contains(par.y)))) {
	    			} else {
	    				matrica.replace(par, true);
	    			}
	    		}
	    	}
	    	// KONTROLA MATRICE
	    	/*for(int i = 0; i < stanja.length; i++) {
	    	for(int j = 0; j < i; j++) {
	    		ParoviStanja par = paroviStanja.get(i * (stanja.length) + j);
	    		System.out.print("[" + par.x +", " + par.y + "]  ");
	    	}
	    	System.out.println("\n");
	    	}*/
	    	// KONTROLA oznacenosti
	    	/*for(int i = 0; i < stanja.length; i++) {
	    	for(int j = 0; j < i; j++) {
	    		ParoviStanja par = paroviStanja.get(i * (stanja.length) + j);
	    		System.out.print("[" + matrica.get(par) + "]  ");
	    	}
	    	System.out.println("\n");
	   	 }*/
	    
	    	//Daljnji algoritam
	    	for(int i = 0; i < stanja.length; i++) {
	    		for(int j = 0; j < i; j++) {
	    			ParoviStanja par = paroviStanja.get(i * (stanja.length) + j);
	    			if(matrica.get(par) == true) {
	    				//System.out.println(par.toString());
	    				boolean ostaje = true;
	    				for(String simbol : simboliAbecede) {
	    					String kljuc1 = par.x + "," + simbol;
	    					String kljuc2 = par.y + "," + simbol;
	    					String sljedece1 = prijelaziNovo.get(kljuc1);
	    					String sljedece2 = prijelaziNovo.get(kljuc2);
	    					if((prihvatljivaStanja.contains(sljedece1) && !(prihvatljivaStanja.contains(sljedece2))) ||
	    					   (!(prihvatljivaStanja.contains(sljedece1)) && (prihvatljivaStanja.contains(sljedece2)))) {
	    						matrica.replace(par, false);
	    						ostaje = false;
	    						provjeriListu(par);
	    						break;
	    					} 
	    				}
	    				if(ostaje) {
	    					for(String simbol : simboliAbecede) {
		    					String kljuc1 = par.x + "," + simbol;
		    					String kljuc2 = par.y + "," + simbol;
		    					String sljedece1 = prijelaziNovo.get(kljuc1);
		    					String sljedece2 = prijelaziNovo.get(kljuc2);
		    					if(sljedece1 != sljedece2) {
		    						ParoviStanja noviPar = new ParoviStanja(sljedece1, sljedece2);
			    					if(mapaSaListama.get(noviPar) == null) {
			    						List<ParoviStanja> lista = new ArrayList<>();
			    						lista.add(par);
			    						mapaSaListama.put(noviPar, lista);
			    					} else {
			    						List<ParoviStanja> lista = mapaSaListama.get(noviPar);
			    						lista.add(par);
			    						mapaSaListama.replace(noviPar, lista);
			    					}
		    					}
		    				}
	    				}
	    			}
	    		}
	    }
	    
	    // Sortiranje stanja unutar istovjetnih stanja
	    for(int i = 0; i < stanja.length; i++) {
	    	for(int j = 0; j < i; j++) {
	    		ParoviStanja par = paroviStanja.get(i * (stanja.length) + j);
	    		if(matrica.get(par) == true) {
	    			List<String> pomoc = new ArrayList<String>();
	    			pomoc.add(par.x);
	    			pomoc.add(par.y);
	    			Collections.sort(pomoc);
	    			par.x = pomoc.get(0);
	    			par.y = pomoc.get(1);
	    			istovjetnaStanja.add(par);
	    		}
	    	}
	    }	
	    
	    // provjera nadogradivanja
	    for(int i = 0; i < istovjetnaStanja.size(); i++) {
	    	for(int j = i + 1; j < istovjetnaStanja.size(); j++) {
	    		if(istovjetnaStanja.get(i).x == istovjetnaStanja.get(j).x) {
	    			ParoviStanja opetNovi = new ParoviStanja(istovjetnaStanja.get(i).y, istovjetnaStanja.get(j).y);
	    			if(!(istovjetnaStanja.contains(opetNovi))) {
	    				istovjetnaStanja.add(opetNovi);
	    			}
	    		}
	    	}
	    }
	    
	    // Provjera konacnog skupa istovjetnih stanja
	    /*System.out.println("\nIstovjetna stanja:     ");
	    for(ParoviStanja par : istovjetnaStanja) {
	    	System.out.println(par + "     ");
	    }
	    System.out.println("\n");*/
	    
	    for(ParoviStanja par : istovjetnaStanja) {
	    	skupStanja.remove(par.y);
	    	prihvatljivaStanja.remove(par.y);
	    	if (pocetnoStanje.equals(par.y)) pocetnoStanje = par.x;
	    	for(String simbol : simboliAbecede) {
	    		String string = par.y + "," + simbol;
	    		if(prijelaziNovo.containsKey(string)) {
	    			prijelaziNovo.remove(string);
	    		}

		}
	    	for(Entry<String, String> entry : prijelaziNovo.entrySet()) {
				if (entry.getValue().contentEquals(par.y)) {
					entry.setValue(par.x);
				}
			}
		}

	    	// ISPIS
	    	String ispis = new String();
	    	for(String stanje : skupStanja) {
	    		ispis = ispis + stanje + ",";
	    	}
	    	ispis = ispis.substring(0, ispis.length() - 1);
	    	ispis += "\n";
	    	for(String simbol : simboliAbecede) {
	    		ispis = ispis + simbol + ",";
	    	}
	   	ispis = ispis.substring(0, ispis.length() - 1);
	    	ispis += "\n";
	    	for(String stanje : prihvatljivaStanja) {
	    		ispis = ispis + stanje + ",";
	    	}
	    	if(prihvatljivaStanja.size() == 0) ispis += "\n";
	    	ispis = ispis.substring(0, ispis.length() - 1);
	    	ispis += "\n";
	    	ispis += pocetnoStanje;
	    	ispis += "\n";
	    	for(Map.Entry<String, String> entry : prijelaziNovo.entrySet()) {
    			ispis = ispis + entry.getKey() + "->" + entry.getValue() + "\n";
		}
	    
	    	System.out.print(ispis);
	    
	   	 reader.close();
	    
	}
	
	public static void provjeriListu(ParoviStanja par) {
    		if(mapaSaListama.get(par) == null) {
    			return;
    		} else {
    			List<ParoviStanja> lista = mapaSaListama.get(par);
    			for(ParoviStanja iterator : lista) {
    				matrica.replace(iterator, false);
    				provjeriListu(iterator);
    			}
    		}
    	}

}




class ParoviStanja {

    public String x;
    public String y;

    public ParoviStanja(String x, String y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
 
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
 
        ParoviStanja key = (ParoviStanja) o;
        if (x != null ? !x.equals(key.x) : key.x != null) {
            return false;
        }
 
        if (y != null ? !y.equals(key.y) : key.y != null) {
            return false;
        }
 
        return true;
    }
 
    @Override
    public int hashCode() {
        int result = x != null ? x.hashCode() : 0;
        result = 31 * result + (y != null ? y.hashCode() : 0);
        return result;
    }
 
    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
    
    public String getKey() {
    	return this.x;
    }
    
    public String getValue() {
    	return this.y;
    }
}




















