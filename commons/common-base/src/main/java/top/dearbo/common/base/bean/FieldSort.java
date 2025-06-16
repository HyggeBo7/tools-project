package top.dearbo.common.base.bean;

import java.io.Serializable;

/**
 * 属性排序
 *
 * @author wb
 */
public class FieldSort implements Serializable {

	public FieldSort(String key) {
		this.key = key;
	}

	public FieldSort(String key, boolean asc) {
		this.key = key;
		this.isAsc = asc;
	}

	/**
	 * 属性名称
	 */
	private String key;

	/**
	 * 是否升序-ture(默认)-升序、false-降序
	 */
	private Boolean isAsc = true;

	public Boolean getAsc() {
		return isAsc;
	}

	public void setAsc(Boolean asc) {
		isAsc = asc;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
