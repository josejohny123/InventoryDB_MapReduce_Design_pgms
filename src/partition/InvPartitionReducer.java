package partition;

//Created by Jose Johny jose.johny@wipro.com or jose.johny@gmail.com
import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class InvPartitionReducer extends Reducer<Text, Text, NullWritable, Text> {
	@Override
	protected void reduce(Text token, Iterable<Text> total_inv , Context context)
			throws IOException, InterruptedException {
		for (Text invRec : total_inv) {
		context.write(NullWritable.get(), new Text(invRec));
	}
}
}
