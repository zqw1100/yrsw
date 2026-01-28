package cn.iocoder.yudao.framework.common.biz.system.permission.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * 小区数据权限 Response DTO
 *
 * @author 芋道源码
 */
@Data
public class CommunityDataPermissionRespDTO {

    /**
     * 是否查看所有小区
     */
    private Boolean all;
    /**
     * 可以查看的小区编号
     */
    private Set<String> communityIds = new HashSet<>();

}
