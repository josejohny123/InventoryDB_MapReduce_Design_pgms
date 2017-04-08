package join.dc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

//Created by Jose Johny jose.johny@wipro.com or jose.johny@gmail.com
public class InvDCMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
		
		private Map<String, String> prodMap = new HashMap<String, String>();

		protected void setup(Context context) throws java.io.IOException, InterruptedException{
			
			Path[] files = DistributedCache.getLocalCacheFiles(context.getConfiguration());
			
			
			for (Path p : files) {
				if (p.getName().equals("Product.txt")) {
					BufferedReader reader = new BufferedReader(new FileReader(p.toString()));
			
					String line = reader.readLine();
					while(line != null) {
						String[] tokens = line.split(",");
						String prodID = tokens[0];
						String prodName = tokens[1];
						prodMap.put(prodID, prodName);
						line = reader.readLine();
					}
					reader.close();
				}		
			}	
			
			if (prodMap.isEmpty()) {
				throw new IOException("Unable to load Product data.");
			}

	}	
		
	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String[] tokens = value.toString().split(",");
			
		String prodID = tokens[0];
		String prodName = prodMap.get(prodID);
		// String warehouse_id;
		Integer quantity_on_hand;
		Integer quantity_ordered;
		// Integer reorder_level;
		Integer total_inv;
		
		// warehouse_id = tokens[1];
		quantity_on_hand = Integer.valueOf(tokens[2]);
		quantity_ordered = Integer.valueOf(tokens[3]);
		// reorder_level = Integer.valueOf(tokens[4]);

		total_inv = quantity_on_hand + quantity_ordered;
			
		context.write(new Text(prodName), new IntWritable(total_inv));
			
		}
	}

