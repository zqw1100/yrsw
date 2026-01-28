package cn.iocoder.yudao.framework.datapermission.config;

import cn.iocoder.yudao.framework.common.biz.system.permission.PermissionCommonApi;
import cn.iocoder.yudao.framework.datapermission.core.rule.community.CommunityDataPermissionRule;
import cn.iocoder.yudao.framework.datapermission.core.rule.community.CommunityDataPermissionRuleCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * CommunityDataPermissionRule 自动配置
 *
 * @author 芋道源码
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(value = {CommunityDataPermissionRuleCustomizer.class})
public class YudaoCommunityDataPermissionAutoConfiguration {

    @Bean
    public CommunityDataPermissionRule communityDataPermissionRule(PermissionCommonApi permissionApi,
                                                                   List<CommunityDataPermissionRuleCustomizer> customizers) {
        CommunityDataPermissionRule rule = new CommunityDataPermissionRule(permissionApi);
        customizers.forEach(customizer -> customizer.customize(rule));
        return rule;
    }

}
