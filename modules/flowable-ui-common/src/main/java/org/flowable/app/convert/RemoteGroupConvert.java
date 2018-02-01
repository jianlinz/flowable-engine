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

    public static RemoteUser convert(User user) {
        if (null == user || StringUtils.isEmpty(user.getId())) {
            return null;
        }
        RemoteUser remoteUser = new RemoteUser();
        remoteUser.setFirstName(user.getUsername());
        remoteUser.setFullName(user.getUsername());
        remoteUser.setGroups(convertToRemoteGroups(user.getUserGroups()));
        remoteUser.setId(user.getId());
        remoteUser.setEmail(user.getEmail());
        return remoteUser;
    }

    public static List<RemoteGroup> convertToRemoteGroups(Collection<? extends UserGroup> userGroups) {
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

    public static List<RemoteUser> convertToRemoteUsers(Collection<? extends User> users) {
        List<RemoteUser> remoteUsers = new ArrayList<>();
        for (User user : users) {
            RemoteUser remoteUser = convert(user);
            if (null == remoteUser) {
                continue;
            }
            remoteUsers.add(remoteUser);
        }
        return remoteUsers;
    }
}
