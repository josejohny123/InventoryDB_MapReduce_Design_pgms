package TotalOrderSorting;

//Created by Jose Johny jose.johny@wipro.com or jose.johny@gmail.com
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.partition.InputSampler;
import org.apache.hadoop.mapreduce.lib.partition.TotalOrderPartitioner;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class InvTotalOrderSorting extends Configured implements Tool{

@Override
	public int run(String[] args) throws Exception {
	
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);
		job.setJobName(this.getClass().getName());
		job.setJarByClass(getClass());


		Path inputPath = new Path(args[0]);
		Path partitionFile = new Path(args[1] + "_partitions.lst");
		Path outputStage = new Path(args[1] + "_staging");
		Path outputOrder = new Path(args[1]);
		double sampleRate = Double.parseDouble(args[2]);
		
		FileSystem.get(conf).delete(outputOrder, true);
		FileSystem.get(conf).delete(outputStage, true);
		FileSystem.get(conf).delete(partitionFile, true);
		
		// configure output and input source
		TextInputFormat.addInputPath(job, inputPath);
		TextOutputFormat.setOutputPath(job, outputStage);
		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);		
		
		job.setMapperClass(InvTotalOrderSortingMapper.class);
		job.setNumReduceTasks(0);
		//job.setReducerClass(InvTotalOrderSortingReducer.class);

		// configure output
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		 
		
		// Submit the job and get completion code.
				int code = job.waitForCompletion(true) ? 0 : 1;

				if (code == 0) {
					Job orderJob = new Job(conf, "TotalOrderSortingStage");
					orderJob.setJarByClass(InvTotalOrderSorting.class);

					// Here, use the identity mapper to output the key/value pairs in
					// the SequenceFile
					orderJob.setMapperClass(Mapper.class);
					orderJob.setReducerClass(InvTotalOrderSortingReducer.class);

					// Set the number of reduce tasks to an appropriate number for the
					// amount of data being sorted
					orderJob.setNumReduceTasks(2);

					// Use Hadoop's TotalOrderPartitioner class
					orderJob.setPartitionerClass(TotalOrderPartitioner.class);

					// Set the partition file
					TotalOrderPartitioner.setPartitionFile(orderJob.getConfiguration(),
							partitionFile);

					orderJob.setOutputKeyClass(Text.class);
					orderJob.setOutputValueClass(Text.class);

					// Set the input to the previous job's output
					orderJob.setInputFormatClass(SequenceFileInputFormat.class);
					SequenceFileInputFormat.setInputPaths(orderJob, outputStage);

					// Set the output path to the command line parameter
					TextOutputFormat.setOutputPath(orderJob, outputOrder);

					// Set the separator to an empty string
					orderJob.getConfiguration().set(
							"mapred.textoutputformat.separator", "");

					// Use the InputSampler to go through the output of the previous
					// job, sample it, and create the partition file
					InputSampler.writePartitionFile(orderJob,
							new InputSampler.RandomSampler(sampleRate, 2));
					// Submit the job
					code = orderJob.waitForCompletion(true) ? 0 : 2;
				}

				// Cleanup the partition file and the staging directory
				FileSystem.get(new Configuration()).delete(partitionFile, false);
				FileSystem.get(new Configuration()).delete(outputStage, true);

				return code;
}
public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new InvTotalOrderSorting(), args);
		System.exit(exitCode);
	}
}
