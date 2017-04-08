package SecondarySorting;

//Created by Jose Johny jose.johny@wipro.com or jose.johny@gmail.com
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class InvSecondarySortingReducer extends Reducer<Text, Text, Text, NullWritable> {
	@Override
	protected void reduce(Text token, Iterable<Text> inv_rec , Context context)
			throws IOException, InterruptedException {
		for (Text invrec : inv_rec) {
			context.write(invrec, NullWritable.get());
		}
		
	}
}
