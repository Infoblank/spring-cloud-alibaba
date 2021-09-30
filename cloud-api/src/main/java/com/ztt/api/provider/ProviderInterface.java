package com.ztt.api.provider;

import com.ztt.common.entity.CommonUser;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author ZTT
 */

public interface ProviderInterface {

    @RequestMapping("providerApi")
    List<CommonUser> getCom();

}
