package cn.iocoder.yudao.module.statistics.dal.mysql.watermeter;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.statistics.dal.dataobject.watermeter.WaterMeterFeeConfigDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface WaterMeterFeeConfigMapper extends BaseMapperX<WaterMeterFeeConfigDO> {

    @Select("SELECT * FROM water_meter_fee_config WHERE deleted = FALSE ORDER BY id DESC LIMIT 1")
    WaterMeterFeeConfigDO selectLatest();
}
