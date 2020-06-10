// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.data;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/** Authentication response class. */
public final class AuthResponse {

  private final String htmlContent;
  private final Boolean isUserLoggedIn;

  public AuthResponse(String htmlContent) {
    UserService userService = UserServiceFactory.getUserService();
    this.htmlContent = htmlContent;
    this.isUserLoggedIn = userService.isUserLoggedIn();
  }
}
