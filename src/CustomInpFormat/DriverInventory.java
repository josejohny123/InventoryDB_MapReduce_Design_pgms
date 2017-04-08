package CustomInpFormat;

//Created by Jose Johny jose.johny@wipro.com or jose.johny@gmail.com

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


public class DriverInventory extends Configured implements Tool {


	@SuppressWarnings("deprecation")
	public int run(String[] args) throws Exception {

		Configuration conf = new Configuration();
		FileSystem hdfsfile = FileSystem.get(conf);
		Job job = Job.getInstance(conf);
		
		job.setJobName(this.getClass().getName());
		
		job.setJarByClass(getClass());
		
		// configure output and input source
		InvKeyValueInputFormat.addInputPath(job, new Path(args[0]));
		hdfsfile.delete(new Path(args[1]));
		TextOutputFormat.setOutputPath(job, new Path(args[1]));
		
		job.setInputFormatClass(InvKeyValueInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);		
		MultipleOutputs.addNamedOutput(job, "bins", TextOutputFormat.class, NullWritable.class, Text.class);
	/*	MultipleOutputs.addNamedOutput(job, "WH02", TextOutputFormat.class, NullWritable.class, Text.class);
		MultipleOutputs.addNamedOutput(job, "WH03", TextOutputFormat.class, NullWritable.class, Text.class);
		MultipleOutputs.addNamedOutput(job, "WH04", TextOutputFormat.class, NullWritable.class, Text.class);*/
           
		job.setMapperClass(MapperInventory.class);
		job.setReducerClass(ReducerInventory.class);
	  // job.setPartitionerClass(WareHouseIdPartitioner.class);
	//job.setNumReduceTasks(4);
		job.setMapOutputKeyClass(InvKeyWritable.class);
		job.setMapOutputValueClass(InvValueWritable.class);
		// configure output
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
		
		return job.waitForCompletion(true) ? 0 : 1;
}

public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new DriverInventory(), args);
		System.exit(exitCode);
	}
}
