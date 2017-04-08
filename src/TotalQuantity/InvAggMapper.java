package TotalQuantity;

//Created by Jose Johny jose.johny@wipro.com or jose.johny@gmail.com
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class InvAggMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	
	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String[] tokens = value.toString().split(",");
		
		String product_id;
		// String warehouse_id;
		Integer quantity_on_hand;
		Integer quantity_ordered;
		// Integer reorder_level;
		Integer total_inv;
		
		product_id = tokens[0];
		// warehouse_id = tokens[1];
		quantity_on_hand = Integer.valueOf(tokens[2]);
		quantity_ordered = Integer.valueOf(tokens[3]);
		// reorder_level = Integer.valueOf(tokens[4]);

		total_inv = quantity_on_hand + quantity_ordered;
		
		context.write(new Text(product_id), new IntWritable(total_inv));
		
		}
}
