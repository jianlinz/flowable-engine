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
package org.flowable.cmmn.engine.impl.cmd;

import org.flowable.cmmn.engine.impl.persistence.entity.CaseInstanceEntity;
import org.flowable.cmmn.engine.impl.util.CommandContextUtil;
import org.flowable.engine.common.api.FlowableIllegalArgumentException;
import org.flowable.engine.common.api.FlowableObjectNotFoundException;
import org.flowable.engine.common.impl.interceptor.Command;
import org.flowable.engine.common.impl.interceptor.CommandContext;

/**
 * @author Joram Barrez
 */
public class TerminateCaseInstanceCmd implements Command<Void> {

    protected String caseInstanceId;

    public TerminateCaseInstanceCmd(String caseInstanceId) {
        this.caseInstanceId = caseInstanceId;
    }

    @Override
    public Void execute(CommandContext commandContext) {
        if (caseInstanceId == null) {
            throw new FlowableIllegalArgumentException("Case instance id is null");
        }
        
        CaseInstanceEntity caseInstanceEntity = CommandContextUtil.getCaseInstanceEntityManager(commandContext).findById(caseInstanceId);
        if (caseInstanceEntity == null) {
            throw new FlowableObjectNotFoundException("Cannot find case instance entity for id " + caseInstanceId, CaseInstanceEntity.class);
        }
        
        CommandContextUtil.getAgenda(commandContext).planTerminateCaseInstanceOperation(caseInstanceEntity.getId(), true);
        return null;
    }

}
