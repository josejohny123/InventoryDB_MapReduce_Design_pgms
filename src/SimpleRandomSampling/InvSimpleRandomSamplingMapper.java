package SimpleRandomSampling;

//Created by Jose Johny jose.johny@wipro.com or jose.johny@gmail.com
import java.io.IOException;
import java.util.Random;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class InvSimpleRandomSamplingMapper extends Mapper<Object, Text, NullWritable, Text> {
	
	private Random rands = new Random();
	private Double percentage=null;

	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {
		// retrieve the percentage that is passed in via the configuration
		// like this: conf.set("filter_percentage", .5); for .5%
		//String strPercentage = context.getConfiguration().get("filter_percentage");
		String strPercentage1 ="10";
		percentage = Double.parseDouble(strPercentage1) / 100.0;
	}

	
	@Override
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
	
		
		if (rands.nextDouble() < percentage) {
			context.write(NullWritable.get(), value);
		}
}
}
