package cn.iocoder.yudao.framework.datapermission.core.rule.community;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.biz.system.permission.PermissionCommonApi;
import cn.iocoder.yudao.framework.common.biz.system.permission.dto.CommunityDataPermissionRespDTO;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.datapermission.core.rule.DataPermissionRule;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.mybatis.core.util.MyBatisUtils;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 基于小区的 {@link DataPermissionRule} 数据权限规则实现
 *
 * @author 芋道源码
 */
@AllArgsConstructor
@Slf4j
public class CommunityDataPermissionRule implements DataPermissionRule {

    /**
     * LoginUser 的 Context 缓存 Key
     */
    protected static final String CONTEXT_KEY = CommunityDataPermissionRule.class.getSimpleName();

    private static final String COMMUNITY_COLUMN_NAME = "community_id";

    private final PermissionCommonApi permissionApi;

    /**
     * 基于小区的表字段配置
     *
     * key：表名
     * value：字段名
     */
    private final Map<String, String> communityColumns = new HashMap<>();
    /**
     * 所有表名，是 {@link #communityColumns} 的合集
     */
    private final Set<String> TABLE_NAMES = new HashSet<>();

    @Override
    public Set<String> getTableNames() {
        return TABLE_NAMES;
    }

    @Override
    public Expression getExpression(String tableName, Alias tableAlias) {
        // 只有有登陆用户的情况下，才进行数据权限的处理
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        if (loginUser == null) {
            return null;
        }
        // 只有管理员类型的用户，才进行数据权限的处理
        if (ObjectUtil.notEqual(loginUser.getUserType(), UserTypeEnum.ADMIN.getValue())) {
            return null;
        }

        // 获得数据权限
        CommunityDataPermissionRespDTO communityPermission = loginUser.getContext(CONTEXT_KEY, CommunityDataPermissionRespDTO.class);
        // 从上下文中拿不到，则调用逻辑进行获取
        if (communityPermission == null) {
            communityPermission = permissionApi.getCommunityDataPermission(loginUser.getId());
            if (communityPermission == null) {
                log.error("[getExpression][LoginUser({}) 获取小区数据权限为 null]", JsonUtils.toJsonString(loginUser));
                throw new NullPointerException(String.format("LoginUser(%d) Table(%s/%s) 未返回小区数据权限",
                        loginUser.getId(), tableName, tableAlias.getName()));
            }
            // 添加到上下文中，避免重复计算
            loginUser.setContext(CONTEXT_KEY, communityPermission);
        }

        // 情况一，如果是 ALL 可查看全部，则无需拼接条件
        if (Boolean.TRUE.equals(communityPermission.getAll())) {
            return null;
        }

        // 情况二，如果没有权限，则返回空结果
        if (CollUtil.isEmpty(communityPermission.getCommunityIds())) {
            return new EqualsTo(null, null); // WHERE null = null，可以保证返回的数据为空
        }

        // 情况三，拼接小区过滤条件
        Expression communityExpression = buildCommunityExpression(tableName, tableAlias, communityPermission.getCommunityIds());
        if (communityExpression == null) {
            log.warn("[getExpression][LoginUser({}) Table({}/{}) CommunityPermission({}) 构建的条件为空]",
                    JsonUtils.toJsonString(loginUser), tableName, tableAlias, JsonUtils.toJsonString(communityPermission));
        }
        return communityExpression;
    }

    private Expression buildCommunityExpression(String tableName, Alias tableAlias, Set<String> communityIds) {
        // 如果不存在配置，则无需作为条件
        String columnName = communityColumns.get(tableName);
        if (StrUtil.isEmpty(columnName)) {
            return null;
        }
        // 如果为空，则无条件
        if (CollUtil.isEmpty(communityIds)) {
            return null;
        }
        return new InExpression(MyBatisUtils.buildColumn(tableName, tableAlias, columnName),
                new ParenthesedExpressionList(new ExpressionList<StringValue>(CollectionUtils.convertList(communityIds, StringValue::new))));
    }

    // ==================== 添加配置 ====================

    public void addCommunityColumn(Class<? extends BaseDO> entityClass) {
        addCommunityColumn(entityClass, COMMUNITY_COLUMN_NAME);
    }

    public void addCommunityColumn(Class<? extends BaseDO> entityClass, String columnName) {
        String tableName = TableInfoHelper.getTableInfo(entityClass).getTableName();
        addCommunityColumn(tableName, columnName);
    }

    public void addCommunityColumn(String tableName, String columnName) {
        communityColumns.put(tableName, columnName);
        TABLE_NAMES.add(tableName);
    }

}
