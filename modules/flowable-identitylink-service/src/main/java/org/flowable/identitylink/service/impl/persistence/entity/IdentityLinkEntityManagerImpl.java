/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.flowable.identitylink.service.impl.persistence.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.flowable.engine.common.api.delegate.event.FlowableEngineEventType;
import org.flowable.engine.common.api.delegate.event.FlowableEventDispatcher;
import org.flowable.engine.common.impl.persistence.entity.data.DataManager;
import org.flowable.identitylink.service.IdentityLinkServiceConfiguration;
import org.flowable.identitylink.service.IdentityLinkType;
import org.flowable.identitylink.service.event.impl.FlowableIdentityLinkEventBuilder;
import org.flowable.identitylink.service.impl.persistence.entity.data.IdentityLinkDataManager;

/**
 * @author Tom Baeyens
 * @author Saeid Mirzaei
 * @author Joram Barrez
 */
public class IdentityLinkEntityManagerImpl extends AbstractEntityManager<IdentityLinkEntity> implements IdentityLinkEntityManager {

    protected IdentityLinkDataManager identityLinkDataManager;

    public IdentityLinkEntityManagerImpl(IdentityLinkServiceConfiguration identityLinkServiceConfiguration, IdentityLinkDataManager identityLinkDataManager) {
        super(identityLinkServiceConfiguration);
        this.identityLinkDataManager = identityLinkDataManager;
    }

    @Override
    protected DataManager<IdentityLinkEntity> getDataManager() {
        return identityLinkDataManager;
    }

    @Override
    public List<IdentityLinkEntity> findIdentityLinksByTaskId(String taskId) {
        return identityLinkDataManager.findIdentityLinksByTaskId(taskId);
    }

    @Override
    public List<IdentityLinkEntity> findIdentityLinksByProcessInstanceId(String processInstanceId) {
        return identityLinkDataManager.findIdentityLinksByProcessInstanceId(processInstanceId);
    }

    @Override
    public List<IdentityLinkEntity> findIdentityLinksByScopeIdAndType(String scopeId, String scopeType) {
        return identityLinkDataManager.findIdentityLinksByScopeIdAndType(scopeId, scopeType);
    }

    @Override
    public List<IdentityLinkEntity> findIdentityLinksByProcessDefinitionId(String processDefinitionId) {
        return identityLinkDataManager.findIdentityLinksByProcessDefinitionId(processDefinitionId);
    }

    @Override
    public List<IdentityLinkEntity> findIdentityLinksByScopeDefinitionIdAndType(String scopeDefinitionId, String scopeType) {
        return identityLinkDataManager.findIdentityLinksByScopeDefinitionIdAndType(scopeDefinitionId, scopeType);
    }

    @Override
    public List<IdentityLinkEntity> findIdentityLinkByTaskUserGroupRoleAndType(String taskId, String userId, String groupId, String roleId, String type) {
        return identityLinkDataManager.findIdentityLinkByTaskUserGroupRoleAndType(taskId, userId, groupId, roleId, type);
    }

    @Override
    public List<IdentityLinkEntity> findIdentityLinkByProcessInstanceUserGroupRoleAndType(String processInstanceId, String userId, String groupId, String roleId, String type) {
        return identityLinkDataManager.findIdentityLinkByProcessInstanceUserGroupRoleAndType(processInstanceId, userId, groupId, roleId, type);
    }

    @Override
    public List<IdentityLinkEntity> findIdentityLinkByProcessDefinitionUserAndGroupAndRole(String processDefinitionId, String userId, String groupId, String roleId) {
        return identityLinkDataManager.findIdentityLinkByProcessDefinitionUserAndGroupAndRole(processDefinitionId, userId, groupId, roleId);
    }

    @Override
    public List<IdentityLinkEntity> findIdentityLinkByScopeIdScopeTypeUserGroupAndType(String scopeId, String scopeType, String userId, String groupId, String roleId, String type) {
        return identityLinkDataManager.findIdentityLinkByScopeIdScopeTypeUserGroupAndType(scopeId, scopeType, userId, groupId, roleId, type);
    }

    @Override
    public List<IdentityLinkEntity> findIdentityLinkByScopeDefinitionScopeTypeUserAndGroupAndRole(String scopeDefinitionId, String scopeType, String userId, String groupId, String roleId) {
        return identityLinkDataManager.findIdentityLinkByScopeDefinitionScopeTypeUserAndGroupAndRole(scopeDefinitionId, scopeType, userId, groupId, roleId);
    }

    @Override
    public IdentityLinkEntity addProcessInstanceIdentityLink(String processInstanceId, String userId, String groupId, String roleId, String type) {
        IdentityLinkEntity identityLinkEntity = identityLinkDataManager.create();
        identityLinkEntity.setProcessInstanceId(processInstanceId);
        identityLinkEntity.setUserId(userId);
        identityLinkEntity.setGroupId(groupId);
        identityLinkEntity.setRoleId(roleId);
        identityLinkEntity.setType(type);
        insert(identityLinkEntity);
        return identityLinkEntity;
    }

    @Override
    public IdentityLinkEntity addScopeIdentityLink(String scopeDefinitionId, String scopeId, String scopeType, String userId, String groupId, String roleId, String type) {
        IdentityLinkEntity identityLinkEntity = identityLinkDataManager.create();
        identityLinkEntity.setScopeDefinitionId(scopeDefinitionId);
        identityLinkEntity.setScopeId(scopeId);
        identityLinkEntity.setScopeType(scopeType);
        identityLinkEntity.setUserId(userId);
        identityLinkEntity.setGroupId(groupId);
        identityLinkEntity.setRoleId(roleId);
        identityLinkEntity.setType(type);
        insert(identityLinkEntity);
        return identityLinkEntity;
    }

    @Override
    public IdentityLinkEntity addTaskIdentityLink(String taskId, String userId, String groupId, String roleId, String type) {
        IdentityLinkEntity identityLinkEntity = identityLinkDataManager.create();
        identityLinkEntity.setTaskId(taskId);
        identityLinkEntity.setUserId(userId);
        identityLinkEntity.setGroupId(groupId);
        identityLinkEntity.setRoleId(roleId);
        identityLinkEntity.setType(type);
        insert(identityLinkEntity);

        return identityLinkEntity;
    }

    @Override
    public IdentityLinkEntity addProcessDefinitionIdentityLink(String processDefinitionId, String userId, String groupId, String roleId) {
        IdentityLinkEntity identityLinkEntity = identityLinkDataManager.create();
        identityLinkEntity.setProcessDefId(processDefinitionId);
        identityLinkEntity.setUserId(userId);
        identityLinkEntity.setGroupId(groupId);
        identityLinkEntity.setRoleId(roleId);
        identityLinkEntity.setType(IdentityLinkType.CANDIDATE);
        insert(identityLinkEntity);
        return identityLinkEntity;
    }

    @Override
    public IdentityLinkEntity addScopeDefinitionIdentityLink(String scopeDefinitionId, String scopeType, String userId, String groupId, String roleId) {
        IdentityLinkEntity identityLinkEntity = identityLinkDataManager.create();
        identityLinkEntity.setScopeDefinitionId(scopeDefinitionId);
        identityLinkEntity.setScopeType(scopeType);
        identityLinkEntity.setUserId(userId);
        identityLinkEntity.setGroupId(groupId);
        identityLinkEntity.setRoleId(roleId);
        identityLinkEntity.setType(IdentityLinkType.CANDIDATE);
        insert(identityLinkEntity);
        return identityLinkEntity;
    }

    @Override
    public IdentityLinkEntity addCandidateUser(String taskId, String userId) {
        return addTaskIdentityLink(taskId, userId, null, null, IdentityLinkType.CANDIDATE);
    }

    @Override
    public List<IdentityLinkEntity> addCandidateUsers(String taskId, Collection<String> candidateUsers) {
        List<IdentityLinkEntity> identityLinks = new ArrayList<>();
        for (String candidateUser : candidateUsers) {
            identityLinks.add(addCandidateUser(taskId, candidateUser));
        }

        return identityLinks;
    }

    @Override
    public IdentityLinkEntity addCandidateGroup(String taskId, String groupId) {
        return addTaskIdentityLink(taskId, null, groupId, null, IdentityLinkType.CANDIDATE);
    }

    @Override
    public List<IdentityLinkEntity> addCandidateGroups(String taskId, Collection<String> candidateGroups) {
        List<IdentityLinkEntity> identityLinks = new ArrayList<>();
        for (String candidateGroup : candidateGroups) {
            identityLinks.add(addCandidateGroup(taskId, candidateGroup));
        }
        return identityLinks;
    }

    @Override
    public List<IdentityLinkEntity> deleteProcessInstanceIdentityLink(String processInstanceId, String userId, String
            groupId, String roleId, String type) {
        List<IdentityLinkEntity> identityLinks = findIdentityLinkByProcessInstanceUserGroupRoleAndType(processInstanceId,
                userId, groupId, roleId, type);

        for (IdentityLinkEntity identityLink : identityLinks) {
            delete(identityLink);
        }

        return identityLinks;
    }

    @Override
    public List<IdentityLinkEntity> deleteScopeIdentityLink(String scopeId, String scopeType, String userId, String groupId, String roleId, String type) {
        List<IdentityLinkEntity> identityLinks = findIdentityLinkByScopeIdScopeTypeUserGroupAndType(scopeId, scopeType, userId, groupId, roleId, type);

        for (IdentityLinkEntity identityLink : identityLinks) {
            deleteIdentityLink(identityLink);
        }

        return identityLinks;
    }

    @Override
    public List<IdentityLinkEntity> deleteTaskIdentityLink(String taskId, List<IdentityLinkEntity>
            currentIdentityLinks, String userId, String groupId, String roleId, String type) {
        List<IdentityLinkEntity> identityLinks = findIdentityLinkByTaskUserGroupRoleAndType(taskId,
                userId, groupId, roleId, type);

        List<IdentityLinkEntity> removedIdentityLinkEntities = new ArrayList<>();
        List<String> identityLinkIds = new ArrayList<>();
        for (IdentityLinkEntity identityLink : identityLinks) {
            delete(identityLink);
            identityLinkIds.add(identityLink.getId());
            removedIdentityLinkEntities.add(identityLink);
        }

        // fix deleteCandidate() in create TaskListener
        if (currentIdentityLinks != null) {
            for (IdentityLinkEntity identityLinkEntity : currentIdentityLinks) {
                if (IdentityLinkType.CANDIDATE.equals(identityLinkEntity.getType()) &&
                        !identityLinkIds.contains(identityLinkEntity.getId())) {

                    if ((userId != null && userId.equals(identityLinkEntity.getUserId()))
                            || (groupId != null && groupId.equals(identityLinkEntity.getGroupId())) || (roleId != null &&
                            roleId.equals(identityLinkEntity.getRoleId()))) {

                        delete(identityLinkEntity);
                        removedIdentityLinkEntities.add(identityLinkEntity);

                    }
                }
            }
        }

        return removedIdentityLinkEntities;
    }

    @Override
    public List<IdentityLinkEntity> deleteProcessDefinitionIdentityLink(String processDefinitionId, String userId,
                                                                        String groupId, String roleId) {
        List<IdentityLinkEntity> identityLinks = findIdentityLinkByProcessDefinitionUserAndGroupAndRole
                (processDefinitionId, userId, groupId, roleId);
        for (IdentityLinkEntity identityLink : identityLinks) {
            delete(identityLink);
        }

        return identityLinks;
    }

    @Override
    public List<IdentityLinkEntity> deleteScopeDefinitionIdentityLink(String scopeDefinitionId, String scopeType, String userId, String groupId, String roleId) {
        List<IdentityLinkEntity> identityLinks = findIdentityLinkByScopeDefinitionScopeTypeUserAndGroupAndRole(scopeDefinitionId, scopeType, userId, groupId, roleId);
        for (IdentityLinkEntity identityLink : identityLinks) {
            deleteIdentityLink(identityLink);
        }

        return identityLinks;
    }

    public void deleteIdentityLink(IdentityLinkEntity identityLink) {
        delete(identityLink, false);

        FlowableEventDispatcher eventDispatcher = getEventDispatcher();
        if (eventDispatcher != null && eventDispatcher.isEnabled()) {
            getEventDispatcher().dispatchEvent(FlowableIdentityLinkEventBuilder.createEntityEvent(FlowableEngineEventType.ENTITY_DELETED, identityLink));
        }
    }

    @Override
    public void deleteIdentityLinksByTaskId(String taskId) {
        identityLinkDataManager.deleteIdentityLinksByTaskId(taskId);
    }

    @Override
    public void deleteIdentityLinksByProcDef(String processDefId) {
        identityLinkDataManager.deleteIdentityLinksByProcDef(processDefId);
    }

    @Override
    public void deleteIdentityLinksByProcessInstanceId(String processInstanceId) {
        identityLinkDataManager.deleteIdentityLinksByProcessInstanceId(processInstanceId);
    }

    @Override
    public void deleteIdentityLinksByScopeIdAndScopeType(String scopeId, String scopeType) {
        identityLinkDataManager.deleteIdentityLinksByScopeIdAndScopeType(scopeId, scopeType);
    }

    @Override
    public IdentityLinkEntity addCandidateRole(String taskId, String roleId) {
        return addTaskIdentityLink(taskId, null, null, roleId, IdentityLinkType.CANDIDATE);
    }

    @Override
    public List<IdentityLinkEntity> addCandidateRoles(String taskId, Collection<String> candidateRoles) {
        List<IdentityLinkEntity> identityLinks = new ArrayList<>();
        for (String candidateRole : candidateRoles) {
            identityLinks.add(addCandidateRole(taskId, candidateRole));
        }

        return identityLinks;
    }

    public IdentityLinkDataManager getIdentityLinkDataManager() {
        return identityLinkDataManager;
    }

    public void setIdentityLinkDataManager(IdentityLinkDataManager identityLinkDataManager) {
        this.identityLinkDataManager = identityLinkDataManager;
    }

}
