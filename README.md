web3j OpenAPI Gradle Plugin
============================

Gradle plugin that generates [Web3j-OpenAPI](https://github.com/web3j/web3j-openapi) 
project from Solidity smart contracts.
It smoothly integrates with your project's build lifecycle by adding specific tasks that can be also
run independently.

## Plugin configuration

The minimum Gradle version to run the plugin is `5.+`.

### Using the `buildscript` convention

To install the web3j Plugin using the old Gradle `buildscript` convention, you should add 
the following to the first line of your build file:

```groovy
buildscript {
    repositories {
        mavenCentral()
        maven {
            url 'https://dl.bintray.com/hotkeytlt/maven'
        }
    }
    dependencies {
        classpath 'org.web3j.openapi:web3j-openapi-gradle-plugin:4.5.11'
    }
}

apply plugin: 'org.web3j.openapi'
```

### Using the plugins DSL (To be checked again)

Alternatively, if you are using the more modern plugins DSL, add the following line to your 
build file:

```groovy
plugins {
    id 'org.web3j.openapi' version '4.5.11'
}
```

Then run your project containing Solidity contracts:

```
./gradlew build
```

After applying the plugin, the base directory for generated code (by default 
`$buildDir/generated/source/web3j`) will contain a directory containing the generated
project for all the contracts specified in the configuration.

## Code generation

The `web3j` DSL allows to configure the generated code, e.g.:

```groovy
web3j {
    generatedPackageName = 'com.mycompany'
    generatedFilesBaseDir = "$buildDir/custom/destination"
    excludedContracts = ['Ownable']
    openapi {
        projectName = "TestProject"
    }
}
```

The properties accepted by the `openapi` DSL are listed in the following table: 

|  Name                   | Type       | Default value                       | Description |
|-------------------------|:----------:|:-----------------------------------:|-------------|
| `projectName`           | `String`   | `${rootProject.name}`               | Generated Web3j-OpenAPI project name. |
| `contextPath`           | `String`   | `projectName`                       | Generated Web3j-OpenAPI context path `/{contextPath}/...`. |
| `contractsAbis`         | `String[]` | `[]`                                | Extra contracts ABIS to use for the Web3j-OpenAPI generation |
| `contractsBins`         | `String[]` | `[]`                                | Extra contracts BINs to use for the Web3j-OpenAPI generation |

Check the [web3j-gradle-plugin](https://github.com/web3j/web3j-gradle-plugin#code-generation) 
for the options accepted by the `web3j` DSL.

The `generatedPackageName` is a `.` separated list of words. It will be converted to lower case during the generation.

**The `.{0}` is not accepted in the case we are generating an `OpenAPI` project**.

## Source sets

By default, all `.sol` files in `$projectDir/src/main/solidity` will be processed by the plugin.
To specify and add different source sets, use the `sourceSets` DSL:

```groovy
sourceSets {
    main {
        solidity {
            srcDir { 
                "my/custom/path/to/solidity" 
             }
        }
    }
}
```

Check the [Solidity Plugin](https://github.com/web3j/solidity-gradle-plugin)
documentation to configure the smart contracts source code directories.

Also, you can add more `ABIs` and `BINs` to the generation via 
the `contractsAbis` and `contractsBins` properties (check the table above).

Output directories for generated Web3j-OpenAPI project
will be added to your build automatically.

### Swagger UI

This plugin is able to generate a [SwaggerUI](https://github.com/swagger-api/swagger-ui) for your whole project.
To do so, the following configurations must be done:

#### Swagger-gradle-plugin configuration

To configure the [Swagger-gradle-plugin](https://github.com/swagger-api/swagger-core/tree/master/modules/swagger-gradle-plugin),
which generates an `OpenAPISpecs` file for the current project. Use the following:

```groovy
resolve {
    resourcePackages = ['org.web3j.openapi', '<your defined packages or <default package name (to be specified later)>']
    classpath = sourceSets.main.runtimeClasspath
    outputDir = file('src/main/resources/static')
}
```
Other parameters can also be specified. Check them in, [here](https://github.com/swagger-api/swagger-core/tree/master/modules/swagger-gradle-plugin#parameters)

#### Gradle-swagger-generator-plugin

This plugin, [Gradle-swagger-generator-plugin](https://github.com/int128/gradle-swagger-generator-plugin), generates
the `SwaggerUI` code. The following configuration should be done:

```groovy
swaggerSources {
    openapi {
        inputFile = file('src/main/resources/static/openapi.json')
    }
}
```
Other parameters can also be specified. Check them in, [here](https://github.com/int128/gradle-swagger-generator-plugin#task-type-generateswaggercode)

After refreshing the project, you should see a `completeSwaggerUiGeneration` task in the `web3j` group. 

**This task is not part of the build process ! 
Make sure to execute this task everytime you make changes and interested in seeing them**

The `SwaggerUI` will be found on : `http://{host}:{port}/swagger-ui`

## Plugin tasks

The ``Web3j-OpenAPI-gradle`` plugin adds tasks to your project build using 
a naming convention on a per source set basis
(i.e. `generateWeb3jOpenAPI`, `generate[SourceSet]Web3jOpenAPI`).

To obtain a list and description of all added tasks, run the command:

```
./gradlew tasks --all
```

[web3j]: https://web3j.io/
