package CustomInpFormat;

//Created by Jose Johny jose.johny@wipro.com or jose.johny@gmail.com
import java.io.IOException;

import org.apache.hadoop.mapreduce.Mapper;
public class MapperInventory extends Mapper<InvKeyWritable, InvValueWritable, InvKeyWritable, InvValueWritable> {

	protected void map(InvKeyWritable key, InvValueWritable value, Context context) throws IOException, InterruptedException {
//		String[] tokens = value.toString().split(",");
						
		context.write(key, value);
			
		}
	}
