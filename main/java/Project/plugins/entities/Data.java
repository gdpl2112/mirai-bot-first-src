package Project.plugins.entities;

public class Data {
	private Number total;
	private String updateTime;
	private List[] list;

	public Number getTotal(){
		return this.total;
	}

	public Data setTotal(Number total) {
		this.total = total;
		return this;
	}

	public String getUpdateTime(){
		return this.updateTime;
	}

	public Data setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public List[] getList(){
		return this.list;
	}

	public Data setList(List[] list) {
		this.list = list;
		return this;
	}
}