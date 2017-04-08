package CustomInpFormat;

//Created by Jose Johny jose.johny@wipro.com or jose.johny@gmail.com
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class InvKeyWritable implements WritableComparable<InvKeyWritable> {

	   public InvKeyWritable() {
		super();
		this.prodid = new Text();
		this.whid = new Text();
	}

	   public void set(String prodid, String whid) {
		   // super();

		   this.prodid.set(prodid);
		   this.whid.set(whid);

		  }
	private Text prodid;
	    public Text getProdid() {
		return prodid;
	}

	public void setProdid(Text prodid) {
		this.prodid = prodid;
	}

	public Text getWhid() {
		return whid;
	}

	public void setWhid(Text whid) {
		this.whid = whid;
	}

		private Text whid;
	    
	@Override
	public void readFields(DataInput in) throws IOException {
		 prodid.readFields(in);
		 whid.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		 prodid.write(out);
		 whid.write(out);
	}

	@Override
	public int compareTo(InvKeyWritable o) {
		if (whid.compareTo(o.whid) == 0) {
			return prodid.compareTo(o.prodid);
		} else
			return whid.compareTo(o.whid);
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((prodid == null) ? 0 : prodid.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "InvKeyWritable [prodid=" + prodid + ", whid=" + whid + "]";
	}

	
	
	

}