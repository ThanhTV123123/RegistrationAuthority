/*
 * Copyright 2015 eBay Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package vn.ra.uaf;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Extension {
	private String id;
	private String data;
	private boolean fail_if_unknown;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	@JsonProperty(value="fail_if_unknown")
	public boolean isFail_if_unknown() {
		return fail_if_unknown;
	}
	
	public void setFail_if_unknown(boolean fail_if_unknown) {
		this.fail_if_unknown = fail_if_unknown;
	}
}
