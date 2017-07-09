/*
 *  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.wso2.carbon.identity.oauth.scope.endpoint.util;

import org.apache.commons.logging.Log;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.oauth.scope.endpoint.Exceptions.ScopeEndpointException;
import org.wso2.carbon.identity.oauth.scope.endpoint.dto.ErrorDTO;
import org.wso2.carbon.identity.oauth.scope.endpoint.dto.ScopeDTO;
import org.wso2.carbon.identity.oauth2.OAuth2ScopeService;
import org.wso2.carbon.identity.oauth2.bean.Scope;

import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;

/**
 * This class holds the util methods used by ScopesApiServiceImpl.
 */
public class ScopeUtils {

    public static OAuth2ScopeService getOAuth2ScopeService() {
        return (OAuth2ScopeService) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                .getOSGiService(OAuth2ScopeService.class, null);
    }

    /**
     * Logs the error, builds a ScopeEndpointException with specified details and throws it
     *
     * @param status      response status
     * @param message     error message
     * @param code        status code
     * @param description error description
     * @param log         Log instance
     * @param throwable   throwable
     * @throws ScopeEndpointException
     */
    public static void handleScopeEndpointException(Response.Status status, String message,
                                                    String code, String description, Log log, Throwable throwable,
                                                    boolean isServerException)
            throws ScopeEndpointException {
        if (isServerException) {
            if (throwable == null) {
                log.error(message);
            } else {
                log.error(message, throwable);
            }
        }
        throw buildScopeEndpointException(status, message, code, description);
    }

    private static ScopeEndpointException buildScopeEndpointException(Response.Status status, String message,
                                                                      String code, String description) {
        ErrorDTO errorDTO = getErrorDTO(message, code, description);
        return new ScopeEndpointException(status, errorDTO);
    }

    /**
     * Returns a generic errorDTO
     *
     * @param message specifies the error message
     * @return A generic errorDTO with the specified details
     */
    public static ErrorDTO getErrorDTO(String message, String code, String description) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setCode(code);
        errorDTO.setMessage(message);
        errorDTO.setDescription(description);
        return errorDTO;
    }

    public static Scope getScope(ScopeDTO scopeDTO) {
        return new Scope(
                scopeDTO.getName(),
                scopeDTO.getDescription(),
                scopeDTO.getBindings());
    }

    public static ScopeDTO getScopeDTO(Scope scope) {
        ScopeDTO scopeDTO = new ScopeDTO();
        scopeDTO.setName(scope.getName());
        scopeDTO.setDescription(scope.getDescription());
        scopeDTO.setBindings(scope.getBindings());
        return scopeDTO;
    }

    public static Set<ScopeDTO> getScopeDTOs(Set<Scope> scopes) {
        Set<ScopeDTO> scopeDTOs = new HashSet<>();
        for (Scope scope : scopes) {
            ScopeDTO scopeDTO = new ScopeDTO();
            scopeDTO.setName(scope.getName());
            scopeDTO.setDescription(scope.getDescription());
            scopeDTO.setBindings(scope.getBindings());
            scopeDTOs.add(scopeDTO);
        }
        return scopeDTOs;
    }
}
