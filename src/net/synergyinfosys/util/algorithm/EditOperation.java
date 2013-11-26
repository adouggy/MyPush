package net.synergyinfosys.util.algorithm;

/**
 * This class represents edit operations for Edit Distance algorithm
 * 
 * @author ade
 *
 */
public class EditOperation {
	private EditOperator operator;
	private int index;
	private String from;
	private String to;
	
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

//	public EditOperation(){}
	
	public EditOperation(EditOperator operator, int index, String from, String to){
		this.setIndex(index);
		this.setOperator(operator);
		this.setFrom(from);
		this.setTo(to);
	}
	
	public EditOperator getOperator() {
		return operator;
	}
	public void setOperator(EditOperator operator) {
		this.operator = operator;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
	@Override
	public String toString(){
		String res = "";
		switch( this.operator ){
		case INSERT:
			res = this.getOperator().toString() + " [" + this.getTo() + "] after " + this.getIndex();
			break;
		case UPDATE:
			res = this.getOperator().toString() + " from [" + this.getFrom() + "] to [" + this.getTo() + "] at " + this.getIndex();
			break;
		case DELETE:
			res = this.getOperator().toString() + " [" + this.getFrom() + "] at " + this.getIndex();
			break;
		default:
			res = "Error!";	
		}
		return res;
	}
	
	/**
	 * smallest size of String for network transfer
	 * @return
	 */
	public String getNecessaryString(){
		// TODO implement this for networking tansfer
		return "";
	}
}
