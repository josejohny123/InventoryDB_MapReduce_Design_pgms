package buildingHierarchical.xmlfiles;

//Created by Jose Johny jose.johny@wipro.com or jose.johny@gmail.com
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class InvRSJ extends Configured implements Tool{

	public int run(String[] args) throws Exception {
	
		Configuration conf = new Configuration();

		Job job = Job.getInstance(conf);
		
		job.setJobName("Reduce side Join");
		
		job.setJarByClass(InvReducer.class);
		
		MultipleInputs.addInputPath(job, new Path(args[0]),TextInputFormat.class, ProdMapper.class);
		MultipleInputs.addInputPath(job, new Path(args[1]),TextInputFormat.class, InvMapper.class);
		
		job.setReducerClass(InvReducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		

		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
		
		Path outputPath = new Path(args[2]);
		
		job.setOutputFormatClass(TextOutputFormat.class);
		TextOutputFormat.setOutputPath(job, outputPath);
		
		
		outputPath.getFileSystem(conf).delete(outputPath, true);
		
		return job.waitForCompletion(true) ? 0 : 1;
     }


public static void main(String[] args) throws Exception {
	int exitCode = ToolRunner.run(new InvRSJ(), args);
	System.exit(exitCode);
}
}