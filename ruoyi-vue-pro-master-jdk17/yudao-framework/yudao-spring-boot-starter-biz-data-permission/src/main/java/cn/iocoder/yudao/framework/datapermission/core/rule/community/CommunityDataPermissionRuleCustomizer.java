package cn.iocoder.yudao.framework.datapermission.core.rule.community;

/**
 * {@link CommunityDataPermissionRule} 的自定义配置接口
 *
 * @author 芋道源码
 */
public interface CommunityDataPermissionRuleCustomizer {

    /**
     * 自定义配置
     *
     * @param rule CommunityDataPermissionRule 对象
     */
    void customize(CommunityDataPermissionRule rule);

}
