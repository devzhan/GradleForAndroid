package com.zone.transform


import org.gradle.api.Plugin
import org.gradle.api.Project

public class PluginImpl implements Plugin<Project> {
    Project project
    void apply(Project project) {
            this.project = project

        applyTask()
    }

    def applyTask(){
        project.extensions.registerTransform(new AJXTransform() )

    }


}
