package TotalSalesQoH;


//Created by Jose Johny jose.johny@wipro.com or jose.johny@gmail.com
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


public class DriverTotalSalesQoH extends Configured implements Tool {


	@SuppressWarnings("deprecation")
	public int run(String[] args) throws Exception {

		Configuration conf = new Configuration();
		FileSystem hdfsfile = FileSystem.get(conf);
		Job job = Job.getInstance(conf);
		
		job.setJobName(this.getClass().getName());
		
		job.setJarByClass(getClass());
		
	    try{
	        DistributedCache.addCacheFile(new URI("/user/cloudera/projects/retail/product/Product.txt#Product.txt"), job.getConfiguration());
	        DistributedCache.addCacheFile(new URI("/user/cloudera/projects/retail/sales/Sales.txt#Sales.txt"), job.getConfiguration());
	        }catch(Exception e){
	        	System.out.println(e);
	        }
		
		
		 URI[] cacheFiles= job.getCacheFiles();
		 if(cacheFiles != null) {
			 for (URI cacheFile : cacheFiles) {
				 System.out.println("Cache file ->" + cacheFile);
			 }
		 } 		
		
		// configure output and input source
		TextInputFormat.addInputPath(job, new Path(args[0]));
		hdfsfile.delete(new Path(args[1]));
		TextOutputFormat.setOutputPath(job, new Path(args[1]));
		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);		
		
		job.setMapperClass(MapperInventory.class);
		job.setReducerClass(ReducerSalesQoh.class);
		// job.setReducerClass(InvDCReducer.class);
		//job.setNumReduceTasks(0);

		// configure output
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		return job.waitForCompletion(true) ? 0 : 1;
}

public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new DriverTotalSalesQoH(), args);
		System.exit(exitCode);
	}
}
