package partition;

//Created by Jose Johny jose.johny@wipro.com or jose.johny@gmail.com
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class InvWareHousePartitioner extends Partitioner<Text, Text> {

	@Override
	public int getPartition(Text whid, Text record, int numReduceTasks) {
		// TODO Auto-generated method stub
		/*String partitionId= whid.toString();
		if (partitionId.equals("WH01")){
			return 1;
		}else if(partitionId.equals("WH02")){
			return 2;
		}
		else if(partitionId.equals("WH03")){
			return 3;
		}
		else 
			return 4;*/
		
		return (whid.toString().charAt(3)) % numReduceTasks;
		
	}
	

}
