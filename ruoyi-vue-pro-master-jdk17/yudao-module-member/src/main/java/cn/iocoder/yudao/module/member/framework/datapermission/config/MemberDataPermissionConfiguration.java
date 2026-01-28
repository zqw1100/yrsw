package cn.iocoder.yudao.module.member.framework.datapermission.config;

import cn.iocoder.yudao.framework.datapermission.core.rule.community.CommunityDataPermissionRuleCustomizer;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterApplyDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterDeviceDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterDeviceHistoryDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFaultDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFeeBillDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFeeDeductFailDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterHouseDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterHouseOwnerDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterWorkOrderDO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * member 模块的数据权限 Configuration
 *
 * @author 芋道源码
 */
@Configuration(proxyBeanMethods = false)
public class MemberDataPermissionConfiguration {

    @Bean
    public CommunityDataPermissionRuleCustomizer memberCommunityDataPermissionRuleCustomizer() {
        return rule -> {
            rule.addCommunityColumn(MemberWaterHouseDO.class);
            rule.addCommunityColumn(MemberWaterApplyDO.class);
            rule.addCommunityColumn(MemberWaterHouseOwnerDO.class);
            rule.addCommunityColumn(MemberWaterFaultDO.class);
            rule.addCommunityColumn(MemberWaterWorkOrderDO.class);
            rule.addCommunityColumn(MemberWaterDeviceDO.class);
            rule.addCommunityColumn(MemberWaterDeviceHistoryDO.class);
            rule.addCommunityColumn(MemberWaterFeeBillDO.class);
            rule.addCommunityColumn(MemberWaterFeeDeductFailDO.class);
        };
    }

}
