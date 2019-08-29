import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	public static void main(String[] args) {
		String inputPath = System.getProperty("user.dir")+"/src/Input/TA_PRECO_MEDICAMENTO.csv";
		String outputPath = System.getProperty("user.dir")+"/src/Input/output.csv";
		String line = "";
		double maxPf = 0;
		double minPf = 0;
		ArrayList<String> maxPfs = new ArrayList<String>();
		ArrayList<String> minPfs = new ArrayList<String>();
		ArrayList<String> productTypes = new ArrayList<String>();
		
		NumberFormat nf = NumberFormat.getInstance(Locale.FRANCE);
		
		
		

		try {
			FileReader fReader = new FileReader(inputPath);
			BufferedReader reader = new BufferedReader(fReader);
			ArrayList<String> header = new ArrayList<String>(Arrays.asList(reader.readLine().split(";"))) ;
			System.out.println(header);
			while ((line = reader.readLine()) != null) {
				String[] row = line.split(";");	
				try {
					Number pfN = nf.parse(row[13]);
					Double pf = pfN.doubleValue();
					if(pf > maxPf) {
						maxPf = pf;
						maxPfs = new ArrayList<String>();
						maxPfs.add(row[5]);
					}else if(pf == maxPf) {
						maxPfs.add(row[5]);
					}else if(row[11].equals("Genérico") && pf < minPf || minPf == 0) {
						minPfs = new ArrayList<String>();
						minPf = pf;
						minPfs.add(row[5]);
						
					}else if (row[11].equals("Genérico") && pf == minPf ) {
						minPfs.add(row[5]);
					}
					if(pf > 100 && row[38].equals("Sim") && row[39].equals("Tarja Vermelha")) {
						
					}
				} catch (ParseException e1) {
					
				} catch (ArrayIndexOutOfBoundsException e2) {
					
				}
				try {
					if(productTypes.indexOf(row[11])==-1) {
						productTypes.add(row[11]);
					}				
				} catch (ArrayIndexOutOfBoundsException e2) {
					
				}

			}
			
			
			
		} catch (IOException e) {
			
		}
		System.out.println(maxPfs);
		System.out.println(productTypes);
		System.out.println(minPfs);
		
		
	}

}
