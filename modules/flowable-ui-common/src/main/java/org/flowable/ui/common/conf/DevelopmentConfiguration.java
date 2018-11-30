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
package org.flowable.ui.common.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Development @Profile specific datasource override
 *
 * @author Yvo Swillens
 */
@Configuration
@Profile({"dev"})
public class DevelopmentConfiguration {
//    如果是dev启动那么flowable默认写死地址开启mysql去部署流程
//    protected static final String DATASOURCE_DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
//    protected static final String DATASOURCE_URL = "jdbc:mysql://127.0.0.1:3306/flowable?characterEncoding=UTF-8";
//    protected static final String DATASOURCE_USERNAME = "flowable";
//    protected static final String DATASOURCE_PASSWORD = "flowable";
//
//    @Bean
//    @Primary
//    public DataSource developmentDataSource() {
//        return DataSourceBuilder
//            .create()
//            .driverClassName(DATASOURCE_DRIVER_CLASS_NAME)
//            .url(DATASOURCE_URL)
//            .username(DATASOURCE_USERNAME)
//            .password(DATASOURCE_PASSWORD)
//            .build();
//    }

}