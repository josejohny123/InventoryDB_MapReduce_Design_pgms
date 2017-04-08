package CustomInpFormat;

//Created by Jose Johny jose.johny@wipro.com or jose.johny@gmail.com
import java.io.IOException;

import org.apache.hadoop.mapreduce.TaskAttemptContext;

public class InvKeyValueInputFormat extends org.apache.hadoop.mapreduce.lib.input.FileInputFormat<InvKeyWritable, InvValueWritable> {

	@Override
	public org.apache.hadoop.mapreduce.RecordReader<InvKeyWritable, InvValueWritable> createRecordReader(
			org.apache.hadoop.mapreduce.InputSplit arg0, TaskAttemptContext arg1)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return new InvKeyValueRecordReader();
	}


	
}