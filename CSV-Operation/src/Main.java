import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class Main {

	public static void main(String[] args) {
		String inputPath = System.getProperty("user.dir")+"/src/Input/TA_PRECO_MEDICAMENTO.csv";
		String outputPath = System.getProperty("user.dir")+"/src/Input/output.csv";
		String line = "";
		ArrayList<String> table = new ArrayList<String>();
		double maxPf = 0;
		ArrayList<Double> maxPfs = new ArrayList<Double>();
		NumberFormat nf = NumberFormat.getInstance(Locale.FRANCE);
		
		
		try {
			FileReader fReader = new FileReader(inputPath);
			BufferedReader reader = new BufferedReader(fReader);
			ArrayList<String> header = new ArrayList<String>(Arrays.asList(reader.readLine().split(";"))) ;
			System.out.println(header);
			int pfSemImpostosIndex = header.indexOf("PF Sem Impostos");
			int count = 0;
			while ((line = reader.readLine()) != null) {
				count++;
				String[] row = line.split(";");	
				Number pfN = nf.parse(row[13]);
				Double pf = pfN.doubleValue();
				if(pf > maxPf) {
					maxPf = pf;
					maxPfs = new ArrayList<Double>();
					maxPfs.add(pf);
				}else if(pf == maxPf) {
					maxPfs.add(pf);
				}
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e1) {
			
		}
		System.out.println(maxPfs);
	}

}
