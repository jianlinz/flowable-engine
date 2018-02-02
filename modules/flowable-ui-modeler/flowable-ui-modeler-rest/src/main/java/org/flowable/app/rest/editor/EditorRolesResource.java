package org.flowable.app.rest.editor;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.service.RoleService;
import com.proper.enterprise.platform.core.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.flowable.app.model.common.ResultListDataRepresentation;
import org.flowable.app.service.idm.RemoteIdmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/workflow/service")
public class EditorRolesResource extends BaseController {

    @Autowired
    protected RemoteIdmService remoteIdmService;


    /**
     * 根据用户角色名字，查找相近的，返回
     *
     * @author sunshuai
     */
    @AuthcIgnore
    @GetMapping("/editor-roles")
    public ResultListDataRepresentation getUserRoles(@RequestParam(required = false, value = "filter") String filter) {
        return remoteIdmService.getRolesByNameFilter(filter);
    }

}
