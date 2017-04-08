package CustomInpFormat;

//Created by Jose Johny jose.johny@wipro.com or jose.johny@gmail.com
import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class ReducerInventory extends Reducer<InvKeyWritable, InvValueWritable, NullWritable, Text> {
	
	
	 private org.apache.hadoop.mapreduce.lib.output.MultipleOutputs<NullWritable, Text> mos;
	 public void setup(Context context) {
	  mos = new org.apache.hadoop.mapreduce.lib.output.MultipleOutputs<NullWritable, Text>(context);
	 }

	 
	protected void reduce(InvKeyWritable ProdID, Iterable<InvValueWritable> Inv_Recs , Context context)
			throws IOException, InterruptedException {
		
		
		for(InvValueWritable invvalue: Inv_Recs){
		//	mos.write(ProdID.getWhid().toString(), NullWritable.get(), new Text(invvalue.toString()));
			mos.write("bins", NullWritable.get(), new Text(invvalue.toString()), ProdID.getWhid().toString());
			}

	}
	
	public void cleanup(Context context) throws IOException, InterruptedException {
        mos.close();
}
}