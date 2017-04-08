package buildingHierarchical.xmlfiles;

//Created by Jose Johny jose.johny@wipro.com or jose.johny@gmail.com
import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class InvReducer extends Reducer<Text, Text, Text, NullWritable> {
	NestElements newelem =new NestElements();	
	private ArrayList<String> inventory = new ArrayList<String>();
	private String product = "";
	
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
					
			for (Text Rec : values) {
				//String invArray[] = invRec.toString().split(",");
				String record = Rec.toString();
				int keyIndex = record.indexOf(",");
				String filepart = record.substring(0, keyIndex);					
				String valuepart = record.substring(keyIndex+1);
				
				if (filepart.equals("invrec")) {
					
					inventory.add(valuepart);
				/*	totInv += Integer.valueOf(invArray[3]) + Integer.valueOf(invArray[4]);
					WareHouseId=invArray[2];
					Qty_on_Hand=Integer.valueOf(invArray[3]);
					Qty_on_Ordered=Integer.valueOf(invArray[4]);
					ReOrder_level=Integer.valueOf(invArray[5]);*/
					
				} else if (filepart.equals("prodrec")) {
				/*	prodId=invArray[1];
					prodname = invArray[2];
					//suppliername = invArray[2];
					 * */				
					product =valuepart;
					}
							
			}		
			// If product is not null
					//	if (product != null) {
							// nest the comments underneath the post element
							String productwithInventory = newelem.nestElements(product, inventory);

							// write out the XML
							//context.write(new Text(product+inventory.toString()),NullWritable.get());
							if(productwithInventory ==null) {
								context.write(new Text("Test " + product +inventory),NullWritable.get());
							}else
							{
								context.write(new Text(productwithInventory),NullWritable.get());
							}
							
							
	//	}

		}}
