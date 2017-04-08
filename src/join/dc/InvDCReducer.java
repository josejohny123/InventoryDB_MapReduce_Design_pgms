package join.dc;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

//Created by Jose Johny jose.johny@wipro.com or jose.johny@gmail.com

public class InvDCReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
	@Override
	protected void reduce(Text token, Iterable<IntWritable> total_inv , Context context)
			throws IOException, InterruptedException {
		int sum = 0;
		for (IntWritable inv : total_inv) {
			sum += inv.get();
		}
		context.write(token, new IntWritable(sum));
	}
}
