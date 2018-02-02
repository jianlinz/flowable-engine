package org.flowable.app.convert;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import org.apache.commons.lang3.StringUtils;
import org.flowable.app.model.common.RemoteGroup;
import org.flowable.app.model.common.RemoteUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RemoteGroupConvert {
    private RemoteGroupConvert() {

    }

    public static RemoteGroup convert(UserGroup userGroup) {
        RemoteGroup remoteGroup = new RemoteGroup();
        if (null == userGroup || StringUtils.isEmpty(userGroup.getId())) {
            return null;
        }
        remoteGroup.setId(userGroup.getId());
        remoteGroup.setName(userGroup.getName());
        return remoteGroup;
    }


    public static List<RemoteGroup> convert(Collection<? extends UserGroup> userGroups) {
        List<RemoteGroup> remoteGroups = new ArrayList<>();
        for (UserGroup userGroup : userGroups) {
            RemoteGroup remoteGroup = convert(userGroup);
            if (null == remoteGroup) {
                continue;
            }
            remoteGroups.add(remoteGroup);
        }
        return remoteGroups;
    }
}
