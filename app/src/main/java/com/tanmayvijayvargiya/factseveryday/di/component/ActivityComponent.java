/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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
 *
 */

package com.tanmayvijayvargiya.factseveryday.di.component;

import com.tanmayvijayvargiya.factseveryday.di.ActivityScope;
import com.tanmayvijayvargiya.factseveryday.views.ActivityHome;
import com.tanmayvijayvargiya.factseveryday.views.FactViewActivity;
import com.tanmayvijayvargiya.factseveryday.views.LoginActivity;
import com.tanmayvijayvargiya.factseveryday.views.SearchActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class)
public interface ActivityComponent extends AppComponent {
    void inject(ActivityHome activityHome);
    void inject(LoginActivity loginActivity);
    void inject(SearchActivity searchActivity);
    void inject(FactViewActivity factViewActivity);
}
