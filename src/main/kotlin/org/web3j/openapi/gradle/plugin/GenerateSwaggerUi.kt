/*
 * Copyright 2020 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.openapi.gradle.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.nio.file.Paths
import javax.inject.Inject

open class GenerateSwaggerUi @Inject constructor(
    private val outputDirPath: String
) : DefaultTask() {

    init {
        val generateSwaggerOpenApiUi = project.tasks.getByName("generateSwaggerUIOpenapi")
        val resolveTask = project.tasks.getByName("resolve")

        generateSwaggerOpenApiUi.dependsOn(resolveTask)
        this.dependsOn(generateSwaggerOpenApiUi)
    }

    @TaskAction
    fun moveSwaggerUi() {
        project.buildDir.toPath().resolve("swagger-ui-openapi").toFile().copyRecursively(
            Paths.get(outputDirPath, "static", "swagger-ui").toFile(), true
        )
    }
}
