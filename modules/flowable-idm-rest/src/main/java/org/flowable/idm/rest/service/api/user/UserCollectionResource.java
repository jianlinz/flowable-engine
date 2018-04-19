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

package org.flowable.idm.rest.service.api.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.flowable.common.rest.api.DataResponse;
import org.flowable.common.rest.exception.FlowableConflictException;
import org.flowable.engine.common.api.FlowableIllegalArgumentException;
import org.flowable.engine.common.api.query.QueryProperty;
import org.flowable.idm.api.IdmIdentityService;
import org.flowable.idm.api.User;
import org.flowable.idm.api.UserQuery;
import org.flowable.idm.api.UserQueryProperty;
import org.flowable.idm.rest.service.api.IdmRestResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

/**
 * @author Frederik Heremans
 * @author Joram Barrez
 */
@RestController
@Api(tags = { "Users" }, description = "Manage Users", authorizations = { @Authorization(value = "basicAuth") })
public class UserCollectionResource {

    protected static HashMap<String, QueryProperty> properties = new HashMap<>();

    static {
        properties.put("id", UserQueryProperty.USER_ID);
        properties.put("firstName", UserQueryProperty.FIRST_NAME);
        properties.put("lastName", UserQueryProperty.LAST_NAME);
        properties.put("email", UserQueryProperty.EMAIL);
    }

    @Autowired
    protected IdmRestResponseFactory idmRestResponseFactory;

    @Autowired
    protected IdmIdentityService identityService;

    @ApiOperation(value = "List users", nickname = "listUsers", tags = { "Users" })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Only return group with the given id", paramType = "query"),
            @ApiImplicitParam(name = "firstName", dataType = "string", value = "Only return users with the given firstname", paramType = "query"),
            @ApiImplicitParam(name = "lastName", dataType = "string", value = "Only return users with the given lastname", paramType = "query"),
            @ApiImplicitParam(name = "email", dataType = "string", value = "Only return users with the given email", paramType = "query"),
            @ApiImplicitParam(name = "firstNameLike", dataType = "string", value = "Only return users with a firstname like the given value.", paramType = "query"),
            @ApiImplicitParam(name = "lastNameLike", dataType = "string", value = "Only return users with a lastname like the given value. ", paramType = "query"),
            @ApiImplicitParam(name = "emailLike", dataType = "string", value = "Only return users with an email like the given value.", paramType = "query"),
            @ApiImplicitParam(name = "memberOfGroup", dataType = "string", value = "Only return users which are a member of the given group.", paramType = "query"),
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Property to sort on, to be used together with the order.", allowableValues = "id,firstName,lastname,email", paramType = "query"),
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Indicates the group exists and is returned.")
    })
    @GetMapping(value = "/users", produces = "application/json")
    public DataResponse<UserResponse> getUsers(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams, HttpServletRequest request) {
        UserQuery query = identityService.createUserQuery();

        if (allRequestParams.containsKey("id")) {
            query.userId(allRequestParams.get("id"));
        }
        if (allRequestParams.containsKey("firstName")) {
            query.userFirstName(allRequestParams.get("firstName"));
        }
        if (allRequestParams.containsKey("lastName")) {
            query.userLastName(allRequestParams.get("lastName"));
        }
        if (allRequestParams.containsKey("email")) {
            query.userEmail(allRequestParams.get("email"));
        }
        if (allRequestParams.containsKey("firstNameLike")) {
            query.userFirstNameLike(allRequestParams.get("firstNameLike"));
        }
        if (allRequestParams.containsKey("lastNameLike")) {
            query.userLastNameLike(allRequestParams.get("lastNameLike"));
        }
        if (allRequestParams.containsKey("emailLike")) {
            query.userEmailLike(allRequestParams.get("emailLike"));
        }
        if (allRequestParams.containsKey("memberOfGroup")) {
            query.memberOfGroup(allRequestParams.get("memberOfGroup"));
        }

        return new UserPaginateList(idmRestResponseFactory).paginateList(allRequestParams, query, "id", properties);
    }

    @ApiOperation(value = "Create a user", tags = { "Users" })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Indicates the user was created."),
            @ApiResponse(code = 400, message = "Indicates the id of the user was missing.")

    })
    @PostMapping(value = "/users", produces = "application/json")
    public UserResponse createUser(@RequestBody UserRequest userRequest, HttpServletRequest request, HttpServletResponse response) {
        if (userRequest.getId() == null) {
            throw new FlowableIllegalArgumentException("Id cannot be null.");
        }

        // Check if a user with the given ID already exists so we return a CONFLICT
        if (identityService.createUserQuery().userId(userRequest.getId()).count() > 0) {
            throw new FlowableConflictException("A user with id '" + userRequest.getId() + "' already exists.");
        }

        User created = identityService.newUser(userRequest.getId());
        created.setEmail(userRequest.getEmail());
        created.setFirstName(userRequest.getFirstName());
        created.setLastName(userRequest.getLastName());
        created.setPassword(userRequest.getPassword());
        identityService.saveUser(created);

        response.setStatus(HttpStatus.CREATED.value());

        return idmRestResponseFactory.createUserResponse(created, false);
    }

}
