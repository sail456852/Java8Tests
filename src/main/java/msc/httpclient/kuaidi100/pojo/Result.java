package msc.httpclient.kuaidi100.pojo;

import msc.httpclient.kuaidi100.utils.JacksonHelper;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.ArrayList;

public class Result {

	/** 消息体，请忽略 */
	private String message = "";
	@JsonIgnore
	/** 快递单当前签收状态，包括0在途中、1已揽收、2疑难、3已签收、4退签、5同城派送中、6退回、7转单等7个状态，其中4-7需要另外开通才有效，详见章2.3.3 */
	private String nu = "";
	@JsonIgnore
	/** 是否签收标记，明细状态请参考2.3.3节state字段 */
	private String ischeck = "0";
	@JsonIgnore
	/** 快递公司编码,一律用小写字母 */
	private String com = "";
	/** 通讯状态，请忽略*/
	private String status = "0";
	@JsonIgnore
	/** 返回数据 */
	private ArrayList<ResultItem> data = new ArrayList<ResultItem>();
	@JsonIgnore
	/** 快递单当前签收状态，包括0在途中、1已揽收、2疑难、3已签收、4退签、5同城派送中、6退回、7转单等7个状态，其中4-7需要另外开通才有效，详见章2.3.3 */
	private String state = "0";
	@JsonIgnore
	/** 快递单明细状态标记，暂未实现，请忽略 */
	private String condition = "";

	@SuppressWarnings("unchecked")
	public Result clone() {
		Result r = new Result();
		r.setCom(this.getCom());
		r.setIscheck(this.getIscheck());
		r.setMessage(this.getMessage());
		r.setNu(this.getNu());
		r.setState(this.getState());
		r.setStatus(this.getStatus());
		r.setCondition(this.getCondition());
		r.setData((ArrayList<ResultItem>) this.getData().clone());

		return r;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNu() {
		return nu;
	}

	public void setNu(String nu) {
		this.nu = nu;
	}

	public String getCom() {
		return com;
	}

	public void setCom(String com) {
		this.com = com;
	}

	public ArrayList<ResultItem> getData() {
		return data;
	}

	public void setData(ArrayList<ResultItem> data) {
		this.data = data;
	}

	public String getIscheck() {
		return ischeck;
	}

	public void setIscheck(String ischeck) {
		this.ischeck = ischeck;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	@Override
	public String toString() {
		return JacksonHelper.toJSON(this);
	}
}
