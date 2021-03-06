/*
 * Copyright 2020 Efeturi Money. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.efemoney.lexiko

import com.squareup.moshi.Moshi
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent
import io.ktor.server.application.*
import io.ktor.util.*
import javax.inject.Scope

@Scope
internal annotation class ApplicationScope

@Scope
internal annotation class CallScope


@ApplicationScope
@Component(
  modules = [FirebaseModule::class, MoshiModule::class],
  dependencies = [ApplicationEnvironment::class]
)
internal interface ApplicationComponent {

  val moshi: Moshi

  val callComponentFactory: CallComponent.Factory

  @Component.Factory
  interface Factory {
    fun create(@BindsInstance application: Application, environment: ApplicationEnvironment): ApplicationComponent
  }
}

@CallScope
@Subcomponent
internal interface CallComponent {

  @Subcomponent.Factory
  interface Factory {
    fun create(@BindsInstance call: ApplicationCall): CallComponent
  }
}


internal val Application.component
  get() = attributes.computeIfAbsent(ApplicationComponentKey) {
    DaggerApplicationComponent.factory().create(this, environment)
  }

internal val ApplicationCall.component
  get() = attributes.computeIfAbsent(CallComponentKey) {
    application.component.callComponentFactory.create(this)
  }


private val ApplicationComponentKey = AttributeKey<ApplicationComponent>("ApplicationComponentKey")

private val CallComponentKey = AttributeKey<CallComponent>("CallComponentKey")
