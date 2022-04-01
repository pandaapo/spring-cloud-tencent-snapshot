/*
 * Tencent is pleased to support the open source community by making Spring Cloud Tencent available.
 *
 * Copyright (C) 2019 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.tencent.cloud.common.metadata.interceptor.feign;

import com.tencent.cloud.common.constant.MetadataConstant;
import com.tencent.cloud.common.metadata.MetadataContext;
import com.tencent.cloud.common.metadata.MetadataContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;

import org.springframework.core.Ordered;

/**
 * Interceptor used for setting peer info in context.
 *
 * @author Haotian Zhang
 */
public class MetadataFirstFeignInterceptor implements RequestInterceptor, Ordered {

	@Override
	public int getOrder() {
		return MetadataConstant.OrderConstant.METADATA_FIRST_FEIGN_INTERCEPTOR_ORDER;
	}

	@Override
	public void apply(RequestTemplate requestTemplate) {
		// get metadata of current thread
		MetadataContext metadataContext = MetadataContextHolder.get();

		// TODO The peer namespace is temporarily the same as the local namespace
		metadataContext.putSystemMetadata(
				MetadataConstant.SystemMetadataKey.PEER_NAMESPACE,
				MetadataContext.LOCAL_NAMESPACE);
		metadataContext.putSystemMetadata(MetadataConstant.SystemMetadataKey.PEER_SERVICE,
				requestTemplate.feignTarget().name());
		metadataContext.putSystemMetadata(MetadataConstant.SystemMetadataKey.PEER_PATH,
				requestTemplate.path());
	}

}