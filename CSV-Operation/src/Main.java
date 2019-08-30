import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	public static void main(String[] args) {
        System.setProperty("file.encoding", "ISO-8859-1");

		//initializing path variables for use on reader/writer
		String inputPath = System.getProperty("user.dir")+"/src/Input/TA_PRECO_MEDICAMENTO.csv";
		String outputPath = System.getProperty("user.dir")+"/src/Output/output.csv";
		
		//some variable initialization, as well as return arrays
		String line = "";
		double maxPf = 0;
		double minPf = 0;
		ArrayList<String> maxPfs = new ArrayList<String>();
		ArrayList<String> minPfs = new ArrayList<String>();
		ArrayList<String> productTypes = new ArrayList<String>();
		ArrayList<String> substOriginal = new ArrayList<String>();
		
		
		NumberFormat nf = NumberFormat.getInstance(Locale.FRANCE);//here a NumberFormat with FRANCE locale is used, so as to parse Double values with comma floating points.
		
		
		
		//inital try/catch block, regarding IO exceptions
		try {
			//initializing reader and writer
			FileReader fReader = new FileReader(inputPath);
			FileWriter csvWriter = new FileWriter(outputPath);
			BufferedReader reader = new BufferedReader(fReader);
			
			//First line is header, can print it here already.
			System.out.println("File's Fields:  \n"+reader.readLine()+"\n");
			while ((line = reader.readLine()) != null) { //iterates until end of input.
				String[] row = line.split(";");	//using split function to analyze eacb field individually.
				
				//try/catch block pertaining to operations with PF.
				try {
					Number pfN = nf.parse(row[13]);
					Double pf = pfN.doubleValue();//using numberformat to parse row 13, 'PF Sem Imposto'
					if(pf < 100 && substOriginal.indexOf(row[0]) == -1) {//populating array that will later be used in one of output.csv file's reading comparisons.
						substOriginal.add(row[0]);
					}
					
					if(pf > maxPf) {//when PF is greater than max, re-initializes array.
						maxPf = pf;
						maxPfs = new ArrayList<String>();
						maxPfs.add(row[9]);
					}else if(pf == maxPf) {//when PF equals max, product is added to maxPFs array
						maxPfs.add(row[9]);
					}else if(row[11].equals("Genérico") && pf < minPf || minPf == 0) {//generic products with least PF, or 'first' generic, since minPF starts at 0
						minPfs = new ArrayList<String>();
						minPf = pf;
						minPfs.add(row[9]);
						
					}else if (row[11].equals("Genérico") && pf == minPf ) {//in case there is more than one generic product with lowest price
						minPfs.add(row[9]);
					}
					if(pf > 100 && row[38].equals("Sim") && row[39].equals("Tarja Vermelha")) {//output.csv file population logic.
						csvWriter.append(line);
						csvWriter.append("\n");
						
					}
				//these exceptions may happen if there are badly formatted lines, but this shouldn't stop the whole process.
				} catch (ParseException e1) {
					
				} catch (ArrayIndexOutOfBoundsException e2) {
					
				}
				try {
					//since this operation isn't involved with PF, it is located 'outside' the first try, since parse exceptions shouldn't stop this other process from happening.
					if(productTypes.indexOf(row[11])==-1) {
						productTypes.add(row[11]);
					}				
				} catch (ArrayIndexOutOfBoundsException e2) {
					
				}

			}
			
			//closing and flushing streams
			csvWriter.flush();
			csvWriter.close();
			fReader.close();
			reader.close();
			
		} catch (IOException e) {
			System.out.println("Problem with file path or formatting! Check README for instructions.");
		}
		
		System.out.println("Product(s) with highest Non-taxed Factory cost: \n"+maxPfs+"\n");
		System.out.println("Product Types: \n"+productTypes+"\n");
		System.out.println("Generic product(s) with lower cost: \n"+minPfs+"\n");
		
		
		//second main operation: reading and sorting output.csv file.
		
		//relevant variable initialization
        Scanner in = new Scanner(System.in);
        System.out.println("First part complete! output.csv has been generated. Input anything to continue.");
        String s = in.nextLine();
		Double sum = 0.0;
		productTypes = new ArrayList<String>();
		int countTypes = 0;
		String sameSubs = "No";
		
		//ArrayList<String> sortedOutput = new ArrayList<String>(); //testing
		try {
			FileReader outputFileReader = new FileReader(outputPath);
			BufferedReader outputReader = new BufferedReader(outputFileReader);
			//FileWriter csvWriterAlpha = new FileWriter(outputPath);
			while((line = outputReader.readLine()) != null) {
				String[] row = line.split(";");
				//sortedOutput.add(row[9]+line);//testing
				try {
					Number pfN = nf.parse(row[13]);
					Double pf = pfN.doubleValue();
					sum += pf;//basic sum logic.
					
					if(substOriginal.indexOf(row[0]) != -1 && sameSubs.equals("No")) {//checks if current product's substance is somewhere in original file's substance list, and adds if so.
						sameSubs = "Yes";
					}
					
					
					
					
				} catch (ParseException e) {

				}
				if(productTypes.indexOf(row[11])==-1) {//counting how many different product types in output.csv file
					productTypes.add(row[11]);
					countTypes++;
				}
				
			}
			
			//Arrays.sort(sortedOutput.toArray());//testing
			
		} catch (IOException e) {
			System.out.println("Problem with file path or formatting! Check README for instructions.");
		}
		
		System.out.println("Sum of output.csv file's product costs: \n"+sum+"\n");
		System.out.println("Quantity of output.csv file's product types: \n"+countTypes+"\n");
		System.out.println("Products in output.csv with same substance as original file's products witn pf < 100?: \n"+sameSubs+"\n");
		
	}

}
