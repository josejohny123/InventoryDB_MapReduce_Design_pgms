package BloomFiltering;
//Created by Jose Johny jose.johny@wipro.com or jose.johny@gmail.com
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.bloom.BloomFilter;
import org.apache.hadoop.util.bloom.Key;

public class Product_BloomFilteringDriver {

			
	public static class BloomFilteringMapper extends
			Mapper<Object, Text, Text, NullWritable> {
		public static final Log log = LogFactory.getLog(Product_BloomFilteringDriver.class);

		private BloomFilter filter = new BloomFilter();

		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
			
        Path[] files = DistributedCache.getLocalCacheFiles(context.getConfiguration());			
        for (Path p : files) {
        	//log to the syslog file
        	log.info("Distributed Cache File Name "+ p.toString());

        	if(log.isDebugEnabled()){
        	log.debug("Distributed Cache File Name "+ p.toString());
        	}
        }
			for (Path p : files) {
				if (p.getName().equals("Product_BloomInput.bf")) {
			
				// Open local file for read.
				DataInputStream strm = new DataInputStream(new FileInputStream(
						p.toString()));

				// Read into our Bloom filter.
				filter.readFields(strm);
				strm.close();
			} else {
				throw new IOException(
						"Bloom filter file not set in the DistributedCache.");
			}
		}
		}

		@Override
		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {

			// Parse the input into tokens
			String[] tokens = value.toString().split(",");
			String prodID = tokens[0];

			//Check the Product is present in bloom filter or not
		
				if (prodID.length() > 0
						&& filter.membershipTest(new Key(prodID.getBytes()))) {
					context.write(value, NullWritable.get());
				}
			}
		}
	

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
	/*	if (otherArgs.length != 3) {
			//System.err.println("Usage: BloomFiltering <in> <cachefile> <out>");
			System.exit(1);
		}*/

		FileSystem.get(conf).delete(new Path(otherArgs[1]), true);

		Job job = new Job(conf, "Product Bloom Filtering");
		job.setJarByClass(Product_BloomFilteringDriver.class);
		job.setMapperClass(BloomFilteringMapper.class);
		job.setNumReduceTasks(0);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

	    try{
	        DistributedCache.addCacheFile(new URI("/user/cloudera/projects/retail/bloomfilter/Product_BloomInput.bf#Product_BloomInput.bf"), job.getConfiguration());
	      //  DistributedCache.addCacheFile(new URI(otherArgs[1]), conf);
	    }catch(Exception e){
	        	System.out.println(e);
	        }
		
		
		 URI[] cacheFiles= job.getCacheFiles();
		 if(cacheFiles != null) {
			 for (URI cacheFile : cacheFiles) {
				 System.out.println("Cache file ->" + cacheFile);
			 }
		 } 		
		 
	/*	DistributedCache.addCacheFile(
				FileSystem.get(conf).makeQualified(new Path(otherArgs[1]))
						.toUri(), job.getConfiguration());*/

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}