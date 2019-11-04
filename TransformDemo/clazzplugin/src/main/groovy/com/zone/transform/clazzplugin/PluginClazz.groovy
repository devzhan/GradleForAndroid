package com.zone.transform.clazzplugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class PluginClazz implements  Plugin<Project> {
    void apply(Project project) {
        AppExtension appExtension = project.extensions.findByType(AppExtension.class)
        appExtension.registerTransform(new ClazzTransform(project))
        project.task("JustTest").doLast {
            println('plugin test')
        }
    }
}