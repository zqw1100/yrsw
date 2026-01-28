package cn.iocoder.yudao.module.pay.framework.datapermission.config;

import cn.iocoder.yudao.framework.datapermission.core.rule.community.CommunityDataPermissionRuleCustomizer;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletRechargeDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletTransactionDO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * pay 模块的数据权限 Configuration
 *
 * @author 芋道源码
 */
@Configuration(proxyBeanMethods = false)
public class PayDataPermissionConfiguration {

    @Bean
    public CommunityDataPermissionRuleCustomizer payCommunityDataPermissionRuleCustomizer() {
        return rule -> {
            rule.addCommunityColumn(PayWalletDO.class);
            rule.addCommunityColumn(PayWalletRechargeDO.class);
            rule.addCommunityColumn(PayWalletTransactionDO.class);
        };
    }

}
