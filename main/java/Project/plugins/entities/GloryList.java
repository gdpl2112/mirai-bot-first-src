package Project.plugins.entities;

public class GloryList {
	private Number result;
	private Number returnCode;
	private String returnMsg;
	private Data data;

	public Number getResult(){
		return this.result;
	}

	public GloryList setResult(Number result) {
		this.result = result;
		return this;
	}

	public Number getReturnCode(){
		return this.returnCode;
	}

	public GloryList setReturnCode(Number returnCode) {
		this.returnCode = returnCode;
		return this;
	}

	public String getReturnMsg(){
		return this.returnMsg;
	}

	public GloryList setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
		return this;
	}

	public Data getData(){
		return this.data;
	}

	public GloryList setData(Data data) {
		this.data = data;
		return this;
	}
}