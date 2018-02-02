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
package org.flowable.app.service.idm;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.api.auth.service.RoleService;
import com.proper.enterprise.platform.api.auth.service.UserGroupService;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.flowable.app.convert.RemoteGroupConvert;
import org.flowable.app.convert.RemoteUserConvert;
import org.flowable.app.model.common.RemoteGroup;
import org.flowable.app.model.common.RemoteToken;
import org.flowable.app.model.common.RemoteUser;
import org.flowable.app.model.common.ResultListDataRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;


@Service
public class RemoteIdmServiceImpl implements RemoteIdmService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteIdmServiceImpl.class);


    @Autowired
    protected Environment environment;

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected UserGroupService userGroupService;
    @Autowired
    protected UserService userService;
    @Autowired
    private RoleService roleService;

    @Override
    public RemoteUser authenticateUser(String username, String password) {
        return null;
    }

    @Override
    public RemoteToken getToken(String tokenValue) {
        return null;
    }

    @Override
    public RemoteUser getUser(String userId) {
        User user = userService.get(userId);
        RemoteUser remoteUser = RemoteUserConvert.convert(user);
        JsonNode json = null;
        try {
            json = objectMapper.readTree(objectMapper.writeValueAsString(remoteUser));
        } catch (JsonProcessingException e) {
            LOGGER.error("JsonProcessingException", e);
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        }
        if (json != null) {
            return parseUserInfo(json);
        }
        return null;
    }

    @Override
    public List<RemoteUser> findUsersByNameFilter(String filter) {
        DataTrunk<? extends User> dataTrunk = userService.getUsersByCondiction(filter, null, null, null, "Y", 1,
                10);
        List<RemoteUser> remoteUsers = RemoteUserConvert.convert(dataTrunk.getData());
        JsonNode json = null;
        try {
            json = objectMapper.readTree(objectMapper.writeValueAsString(remoteUsers));
        } catch (JsonProcessingException e) {
            LOGGER.error("JsonProcessingException", e);
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        }
        if (json != null) {
            return parseUsersInfo(json);
        }
        return new ArrayList<>();
    }

    @Override
    public List<RemoteUser> findUsersByGroup(String groupId) {
        return null;
    }

    @Override
    public RemoteGroup getGroup(String groupId) {
        return null;
    }

    @Override
    public List<RemoteGroup> findGroupsByNameFilter(String filter) {
        Collection<? extends UserGroup> userGroups = userGroupService.getGroups(filter, null, null);
        List<RemoteGroup> remoteGroups = RemoteGroupConvert.convert(userGroups);
        JsonNode json = null;
        try {
            json = objectMapper.readTree(objectMapper.writeValueAsString(remoteGroups));
        } catch (JsonProcessingException e) {
            LOGGER.error("JsonProcessingException", e);
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        }
        if (json != null) {
            return parseGroupsInfo(json);
        }
        return new ArrayList<>();
    }

    @Override
    public ResultListDataRepresentation getRolesByNameFilter(String filter) {
        String roleNameFilter = filter;
        if (StringUtils.isEmpty(roleNameFilter)) {
            roleNameFilter = "%";
        } else {
            roleNameFilter = "%" + roleNameFilter + "%";
        }
        Collection roles = roleService.getAllSimilarRolesByName(roleNameFilter);
        Iterator iterator = roles.iterator();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map;
        while (iterator.hasNext()) {
            Role role = (Role) iterator.next();
            map = new TreeMap<>();
            map.put("id", role.getId());
            map.put("name", role.getName());
            list.add(map);
        }
        return new ResultListDataRepresentation(list);
    }

    protected JsonNode callRemoteIdmService(String url, String username, String password) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + new String(
                Base64.encodeBase64((username + ":" + password).getBytes(Charset.forName("UTF-8")))));

        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            sslsf = new SSLConnectionSocketFactory(builder.build(), NoopHostnameVerifier.INSTANCE);
            clientBuilder.setSSLSocketFactory(sslsf);
        } catch (Exception e) {
            LOGGER.warn("Could not configure SSL for http client", e);
        }

        CloseableHttpClient client = clientBuilder.build();

        try {
            HttpResponse response = client.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return objectMapper.readTree(response.getEntity().getContent());
            }
        } catch (Exception e) {
            LOGGER.warn("Exception while getting token", e);
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    LOGGER.warn("Exception while closing http client", e);
                }
            }
        }
        return null;
    }

    protected List<RemoteUser> parseUsersInfo(JsonNode json) {
        List<RemoteUser> result = new ArrayList<>();
        if (json != null && json.isArray()) {
            ArrayNode array = (ArrayNode) json;
            for (JsonNode userJson : array) {
                result.add(parseUserInfo(userJson));
            }
        }
        return result;
    }

    protected RemoteUser parseUserInfo(JsonNode json) {
        RemoteUser user = new RemoteUser();
        user.setId(json.get("id").asText());
        user.setFirstName(json.get("firstName").asText());
        user.setLastName(json.get("lastName").asText());
        user.setEmail(json.get("email").asText());
        user.setFullName(json.get("fullName").asText());

        if (json.has("groups")) {
            for (JsonNode groupNode : ((ArrayNode) json.get("groups"))) {
                user.getGroups().add(new RemoteGroup(groupNode.get("id").asText(), groupNode.get("name").asText()));
            }
        }

        if (json.has("privileges")) {
            for (JsonNode privilegeNode : ((ArrayNode) json.get("privileges"))) {
                user.getPrivileges().add(privilegeNode.asText());
            }
        }

        return user;
    }

    protected List<RemoteGroup> parseGroupsInfo(JsonNode json) {
        List<RemoteGroup> result = new ArrayList<>();
        if (json != null && json.isArray()) {
            ArrayNode array = (ArrayNode) json;
            for (JsonNode userJson : array) {
                result.add(parseGroupInfo(userJson));
            }
        }
        return result;
    }

    protected RemoteGroup parseGroupInfo(JsonNode json) {
        RemoteGroup group = new RemoteGroup();
        group.setId(json.get("id").asText());
        group.setName(json.get("name").asText());
        return group;
    }

    protected String encode(String s) {
        if (s == null) {
            return "";
        }

        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (Exception e) {
            LOGGER.warn("Could not encode url param", e);
            return null;
        }
    }

}
