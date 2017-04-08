package CustomInpFormat;

//Created by Jose Johny jose.johny@wipro.com or jose.johny@gmail.com
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class InvValueWritable implements Writable {

	private Text prodid;
    private IntWritable qoh;
    private IntWritable qty_ordered;
    private IntWritable reorder_lvl;
    
	public InvValueWritable() {
		super();
		this.prodid = new Text();
		this.qoh = new IntWritable();
		this.qty_ordered = new IntWritable();
		this.reorder_lvl = new IntWritable();
	}
	  public void set(String prodid, int qoh, int qty_ordered, int reorder_lvl) {
		  // super();

		  this.prodid.set(prodid);
		  this.qoh.set(qoh);
		  this.qty_ordered.set(qty_ordered);
		  this.reorder_lvl.set(reorder_lvl);

		 }

	
	public Text getProdid() {
		return prodid;
	}


	public void setProdid(Text prodid) {
		this.prodid = prodid;
	}


	public IntWritable getQoh() {
		return qoh;
	}


	public void setQoh(IntWritable qoh) {
		this.qoh = qoh;
	}


	public IntWritable getQty_ordered() {
		return qty_ordered;
	}


	public void setQty_ordered(IntWritable qty_ordered) {
		this.qty_ordered = qty_ordered;
	}


	public IntWritable getReorder_lv() {
		return reorder_lvl;
	}


	public void setReorder_lv(IntWritable reorder_lv) {
		this.reorder_lvl = reorder_lv;
	}


	@Override
	public void readFields(DataInput in) throws IOException {
		 prodid.readFields(in);
		 qoh.readFields(in); 
		 qty_ordered.readFields(in);
		 reorder_lvl.readFields(in);	
		 
	}


	@Override
	public void write(DataOutput out) throws IOException {
	     prodid.write(out);
	     qoh.write(out);
	     qty_ordered.write(out);
	     reorder_lvl.write(out);    
	}
	@Override
	public String toString() {
		return "[prodid=" + prodid + ", qoh=" + qoh
				+ ", qty_ordered=" + qty_ordered + ", reorder_lvl="
				+ reorder_lvl + "]";
	}



}