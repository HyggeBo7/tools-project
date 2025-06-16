package top.dearbo.common.core.sql;


import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import top.dearbo.common.base.bean.BaseQuery;
import top.dearbo.common.base.bean.FieldSort;
import top.dearbo.common.core.exception.AppException;
import top.dearbo.common.core.lang.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * sql操作工具类
 *
 * @author ruoyi
 */
public class SqlUtil {
	/**
	 * 定义常用的 sql关键字
	 */
	public static String SQL_REGEX = "and |extractvalue|updatexml|exec |insert |select |delete |update |drop |count |chr |mid |master |truncate |char |declare |or |+|user()";

	/**
	 * 仅支持字母、数字、下划线、空格、逗号、小数点（支持多个字段排序）
	 */
	public static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,\\.]+";

	/**
	 * 限制orderBy最大长度
	 */
	private static final int ORDER_BY_MAX_LENGTH = 500;

	/**
	 * 排序字段转sql
	 *
	 * @param condition 查询条件
	 * @return String
	 */
	public static String getOrderBy(BaseQuery condition) {
		List<FieldSort> sorts = condition.getSorts();
		if (CollectionUtils.isEmpty(sorts) && (ArrayUtils.isNotEmpty(condition.getAsc()) || ArrayUtils.isNotEmpty(condition.getDesc()))) {
			sorts = new ArrayList<>();
		}
		if (ArrayUtils.isNotEmpty(condition.getAsc())) {
			for (String ascField : condition.getAsc()) {
				sorts.add(new FieldSort(ascField));
			}
		}
		if (ArrayUtils.isNotEmpty(condition.getDesc())) {
			for (String descField : condition.getDesc()) {
				sorts.add(new FieldSort(descField, false));
			}
		}
		StringBuilder orderByBuild = new StringBuilder();
		if (CollectionUtils.isNotEmpty(sorts)) {
			for (FieldSort sort : sorts) {
				String key = sort.getKey();
				if (StringUtils.isNotBlank(key)) {
					if (orderByBuild.length() > 0) {
						orderByBuild.append(",");
					}
					if (condition.isOrderCamelUnderline()) {
						orderByBuild.append(StringUtil.toCamelUnderline(key));
					} else {
						orderByBuild.append(key);
					}
					if (BooleanUtils.isTrue(sort.getAsc())) {
						orderByBuild.append(" ASC");
					} else {
						orderByBuild.append(" DESC");
					}
				}
			}
		}
		return orderByBuild.toString();
	}

	/**
	 * 检查字符，防止注入绕过
	 */
	public static String escapeOrderBySql(String value) {
		if (StringUtils.isNotEmpty(value) && !isValidOrderBySql(value)) {
			AppException.throwEx("参数不符合规范，不能进行查询");
		}
		if (StringUtils.length(value) > ORDER_BY_MAX_LENGTH) {
			AppException.throwEx("参数已超过最大限制，不能进行查询");
		}
		return value;
	}

	/**
	 * 验证 order by 语法是否符合规范
	 */
	public static boolean isValidOrderBySql(String value) {
		return value.matches(SQL_PATTERN);
	}

	/**
	 * SQL关键字检查
	 */
	public static void filterKeyword(String value) {
		if (StringUtils.isEmpty(value)) {
			return;
		}
		String[] sqlKeywords = StringUtils.split(SQL_REGEX, "\\|");
		for (String sqlKeyword : sqlKeywords) {
			if (StringUtils.indexOfIgnoreCase(value, sqlKeyword) > -1) {
				AppException.throwEx("参数存在SQL注入风险");
			}
		}
	}
}
