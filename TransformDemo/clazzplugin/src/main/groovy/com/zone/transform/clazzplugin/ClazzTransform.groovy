package com.zone.transform.clazzplugin

import com.android.build.api.transform.Context
import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.Project
import com.android.utils.FileUtils


class ClazzTransform extends Transform {

    private Project mProject

    // 构造函数，我们将Project保存下来备用
    ClazzTransform(Project project) {
        this.mProject = project
    }


    @Override
    String getName() {
        return 'ClazzTransform'
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }



    @Override
    void transform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs, TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {
        println('--------------------transform 开始--------------------')
        // Transform的inputs有两种类型，一种是目录，一种是jar包，要分开遍历
        inputs.each {
            TransformInput input ->
                // 遍历文件夹
                //文件夹里面包含的是我们手写的类以及R.class、BuildConfig.class以及R$XXX.class等
                input.directoryInputs.each {
                    DirectoryInput directoryInput ->
                        // 注入代码
                        ClazzByJavassit.injectToast(directoryInput.file.absolutePath, mProject)

                        // 获取输出目录
                        def dest = outputProvider.getContentLocation(directoryInput.name,
                                directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)

                        println("directory output dest: $dest.absolutePath")
                        println("directory Input output dest: $directoryInput.file.absolutePath")

                        // 将input的目录复制到output指定目录
                        FileUtils.copyDirectory(directoryInput.file, dest)
                }

                //对类型为jar文件的input进行遍历
                input.jarInputs.each {
                        //jar文件一般是第三方依赖库jar文件
                    JarInput jarInput ->
                        // 重命名输出文件（同目录copyFile会冲突）
                        def jarName = jarInput.name
                        println("jar: $jarInput.file.absolutePath")
                        def md5Name = DigestUtils.md5Hex(jarInput.file.absolutePath)
                        if (jarName.endsWith('.jar')) {
                            jarName = jarName.substring(0, jarName.length() - 4)
                        }
                        def dest = outputProvider.getContentLocation(jarName + md5Name, jarInput.contentTypes, jarInput.scopes, Format.JAR)

                        println("jar output dest: $dest.absolutePath")
                        FileUtils.copyFile(jarInput.file, dest)
                }
        }


        println('--------------------transform 结束--------------------')

    }
}