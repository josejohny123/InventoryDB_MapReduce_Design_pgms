package TotalSalesQoH;

//Created by Jose Johny jose.johny@wipro.com or jose.johny@gmail.com
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class ReducerSalesQoh extends Reducer<Text, Text, Text, Text> {
	
	private Map<String, String> prodMap = new HashMap<String, String>();
	private Map<String, String> salesMap = new HashMap<String, String>();

	protected void setup(Context context) throws java.io.IOException, InterruptedException{
		
		Path[] files = DistributedCache.getLocalCacheFiles(context.getConfiguration());
		
		
		for (Path p : files) {
			if (p.getName().equals("Product.txt")) {
				FileReader fileReader = new FileReader(p.toString());
				BufferedReader reader = new BufferedReader(fileReader);
		
				String line = reader.readLine();
				while(line != null) {
					String[] tokens = line.split(",");
					String prodID = tokens[0];
					String prodName = tokens[1];
					prodMap.put(prodID, prodName);
					line = reader.readLine();
				}
				reader.close();
				fileReader.close();
			}
			
		 if (p.getName().equals("Sales.txt")) {
				FileReader fileReader = new FileReader(p.toString());
				BufferedReader reader = new BufferedReader(fileReader);
				
				String line = reader.readLine();
				while(line != null) {
					String[] tokens = line.split(",");
					String prodID = tokens[0];
					String salesOrder = tokens[1];
					salesMap.put(prodID, salesOrder);
					line = reader.readLine();
				}
				reader.close();
				fileReader.close();
			}	
		}	
		
		if (prodMap.isEmpty()) {
			throw new IOException("Unable to load Product data.");
		}

		if (salesMap.isEmpty()) {
			throw new IOException("Unable to load Sales data.");
		}

}	
	
	protected void reduce(Text ProdID, Iterable<Text> Inv_Recs , Context context)
			throws IOException, InterruptedException {
		
		String prodid = ProdID.toString();
		String prodName = prodMap.get(prodid);
		String salesOrder = salesMap.get(prodid);
		StringBuffer OutRec= new StringBuffer();
		for (Text inv_recs : Inv_Recs) {
			String[] tokens = inv_recs.toString().split(",");
			OutRec=OutRec.append(" ["+tokens[1]+" " +tokens[2]+ "] ");
				
		}
		context.write(new Text(prodid), new Text(prodName+ " "+ OutRec+" " +salesOrder));
	}
}
