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
		String outputPath = System.getProperty("user.dir")+"/src/Output/output.csv";
		String line = "";
		double maxPf = 0;
		double minPf = 0;
		ArrayList<String> maxPfs = new ArrayList<String>();
		ArrayList<String> minPfs = new ArrayList<String>();
		ArrayList<String> productTypes = new ArrayList<String>();
		ArrayList<String> substOriginal = new ArrayList<String>();
		
		
		NumberFormat nf = NumberFormat.getInstance(Locale.FRANCE);
		
		
		
		
		//
		try {
			FileReader fReader = new FileReader(inputPath);
			FileWriter csvWriter = new FileWriter(outputPath);
			BufferedReader reader = new BufferedReader(fReader);
			System.out.println("File's Fields:  \n"+reader.readLine());
			while ((line = reader.readLine()) != null) {
				String[] row = line.split(";");	
				try {
					Number pfN = nf.parse(row[13]);
					Double pf = pfN.doubleValue();
					if(pf < 100 && substOriginal.indexOf(row[0]) == -1) {
						substOriginal.add(row[0]);
					}
					if(pf > maxPf) {
						maxPf = pf;
						maxPfs = new ArrayList<String>();
						maxPfs.add(row[9]);
					}else if(pf == maxPf) {
						maxPfs.add(row[9]);
					}else if(row[11].equals("Genérico") && pf < minPf || minPf == 0) {
						minPfs = new ArrayList<String>();
						minPf = pf;
						minPfs.add(row[9]);
						
					}else if (row[11].equals("Genérico") && pf == minPf ) {
						minPfs.add(row[9]);
					}
					if(pf > 100 && row[38].equals("Sim") && row[39].equals("Tarja Vermelha")) {
						csvWriter.append(line);
						csvWriter.append("\n");
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
			
			csvWriter.flush();
			csvWriter.close();
			fReader.close();
			reader.close();
			
		} catch (IOException e) {
			
		}
		System.out.println("Product(s) with highest Non-taxed Factory cost: \n"+maxPfs);
		System.out.println("Product Types: \n"+productTypes);
		System.out.println("Generic product(s) with lower cost: \n"+minPfs);
		
		
		Double sum = 0.0;
		productTypes = new ArrayList<String>();
		int countTypes = 0;
		ArrayList<String> sameSubsOriginal = new ArrayList<String>();
		try {
			FileReader outputFileReader = new FileReader(outputPath);
			BufferedReader outputReader = new BufferedReader(outputFileReader);
			FileWriter csvWriterAlpha = new FileWriter(outputPath);
			outputReader.readLine();
			while((line = outputReader.readLine()) != null) {
				String[] row = line.split(";");
				Number pfN;
				try {
					pfN = nf.parse(row[13]);
					Double pf = pfN.doubleValue();
					sum += pf;
					if(substOriginal.indexOf(row[0]) != -1) {
						sameSubsOriginal.add(row[9]);
					}
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(productTypes.indexOf(row[11])==-1) {
					productTypes.add(row[11]);
					countTypes++;
				}
				
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Sum of output.csv file's product costs: \n"+sum);
		System.out.println("Quantity of output.csv file's product types: \n"+countTypes);
		System.out.println("output.csv file's products with same types as original file's products with final factory cost lower than 100: \n"+sameSubsOriginal);
		
	}

}
