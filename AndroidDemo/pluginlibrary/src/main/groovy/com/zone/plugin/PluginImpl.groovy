package com.zone.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

public class PluginImpl implements Plugin<Project> {
    Project project
    void apply(Project project) {
            this.project = project

        applyTask()
    }

    def applyTask(){
        applyRequestConfig()
        applyBuildTask()

    }

    def applyBuildTask(){
        project.task("buildMark",type:GreetingTask)
    }
    def applyRequestConfig(){
        project.task("requestMark",type:RequestConfig)
    }
}
class GreetingTask extends DefaultTask {
    @TaskAction
    def greet() {
        println 'hello from GreetingTask'
    }
}