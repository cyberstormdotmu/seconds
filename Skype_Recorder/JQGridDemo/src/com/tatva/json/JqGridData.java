package com.tatva.json;

import java.util.List;

public class JqGridData<T> {

	private int total;
	private int page;
	private int records;
	private List<T> rows;
	
	/*public JqGridData(int total,int page,int records,List<T> rows) {
		
		this.total=total;
		this.page=page;
		this.records=records;
		this.rows=rows;
	}
*/
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRecords() {
		return records;
	}

	public void setRecords(int records) {
		this.records = records;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}
	
	/*public String getJsonString()
	{
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("page", page);
		map.put("records", records);
		map.put("total", total);
		map.put("rows", rows);
		return JSONObject.toJSONString(map);
	}*/
	
}
