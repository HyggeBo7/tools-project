package top.dearbo.common.core.result;

import org.apache.commons.collections4.CollectionUtils;
import top.dearbo.common.base.bean.BaseQuery;
import top.dearbo.common.core.exception.AppException;
import top.dearbo.common.core.xt.MapperUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 分页结果返回
 * @version 1.0.0
 */
public class PageResult<T> implements Serializable {
	private Long total;
	/**
	 * 兼容旧项目-total
	 */
	private Long count;
	private List<T> data;
	private Map<String, Object> extraData;

	public PageResult(Long total) {
		this.total = total;
		this.count = total;
		this.data = new ArrayList<>();
	}

	public PageResult(Long total, List<T> data) {
		this.total = total;
		this.count = total;
		this.data = data;
	}

	public Long getTotal() {
		return total;
	}

	public Long getCount() {
		return count;
	}

	public List<T> getData() {
		return data;
	}

	public void setTotal(Long total) {
		this.total = total;
		this.count = total;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public Map<String, Object> getExtraData() {
		return extraData;
	}

	public void setExtraData(Map<String, Object> extraData) {
		this.extraData = extraData;
	}

	public static <T> PageResult<T> getPageResult(List<T> data, Long total) {
		return new PageResult<>(total, data);
	}

	public static <T> PageResult<T> emptyData() {
		return new PageResult<>(0L, Collections.emptyList());
	}

	public static <T, C> PageResult<C> getPageResultChange(List<T> list, Long total, Class<C> targetClass, Callback<T, C> callback) {
		if (CollectionUtils.isEmpty(list)) {
			return emptyData();
		}
		List<C> result = list.stream().map(originalData -> {
			C targetBean;
			try {
				targetBean = targetClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new AppException("创建对象失败", e);
			}
			if (callback != null) {
				callback.transform(originalData, targetBean);
			} else {
				MapperUtil.copyProperties(originalData, targetBean);
			}
			return targetBean;
		}).collect(Collectors.toList());
		return new PageResult<>(total, result);
	}

	/**
	 * 总共多少页
	 */
	public long getTotalPage(BaseQuery paramQuery) {
		if (total != null && total > 0L) {
			long page = total / paramQuery.getPageSize();
			page += total - paramQuery.getPageSize() * page > 0 ? 1 : 0;
			return page;
		} else {
			return 0L;
		}
	}

	public static String getOrderBy(BaseQuery condition) {
		String[] ascArray = condition.getAsc();
		StringBuilder orderByBuild = new StringBuilder();
		if (ascArray != null && ascArray.length > 0) {
			for (int i = 0; i < ascArray.length; ++i) {
				orderByBuild.append(ascArray[i]);
				if (i < ascArray.length - 1) {
					orderByBuild.append(" ASC,");
				} else {
					orderByBuild.append(" ASC ");
				}
			}
		}
		String[] descArray = condition.getDesc();
		if (descArray != null && descArray.length > 0) {
			if (orderByBuild.length() > 0) {
				orderByBuild.append(",");
			}
			for (int i = 0; i < descArray.length; ++i) {
				orderByBuild.append(descArray[i]);
				if (i < descArray.length - 1) {
					orderByBuild.append(" DESC,");
				} else {
					orderByBuild.append(" DESC ");
				}
			}
		}
		return orderByBuild.toString();
	}

	public interface Callback<T, C> {
		void transform(T originalData, C targetData);
	}

}
