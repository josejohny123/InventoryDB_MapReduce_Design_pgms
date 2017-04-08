package buildingHierarchical.xmlfiles;
//Created by Jose Johny jose.johny@wipro.com or jose.johny@gmail.com
import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class InvMapper extends Mapper<LongWritable, Text, Text, Text> {
		
	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		
		String record = value.toString();
		int keyIndex = record.indexOf(",");
		String keypart = record.substring(0, keyIndex);	
		
		String valuepart = record.substring(keyIndex +1);
			
		context.write(new Text(keypart), new Text("invrec" + "," + record));
			
	}
}
	
