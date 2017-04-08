package SecondarySorting;

//Created by Jose Johny jose.johny@wipro.com or jose.johny@gmail.com
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class InvSecondarySortingMapper extends Mapper<LongWritable, Text, Text, Text> {
	
	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String[] tokens = value.toString().split(",");
		
		String product_id;		
		String wareHouseId;
		product_id = tokens[0];	
		wareHouseId= tokens[1];
		//Secondary Sorting  1st by product_id then by wareHouseId
		context.write(new Text(product_id+"^"+wareHouseId), value);
		
		}
}
