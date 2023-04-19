package pers.warren.coke.maven.plugin;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;
import pers.warren.coke.maven.plugin.tools.tool.ArtifactsLibraries;
import pers.warren.coke.maven.plugin.tools.tool.Libraries;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

//@Mojo(name = "repackage", defaultPhase = LifecyclePhase.PACKAGE, requiresDependencyResolution = ResolutionScope.RUNTIME)
@Mojo(name = "repackage", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true,
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME,
        requiresDependencyCollection = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class RepackageMojo extends AbstractMojo {

    @Parameter(required = false)
    private String mainClass;

    @Parameter(readonly = false)
    private String jvmArguments;

    @Parameter(defaultValue = "${project.build.directory}", required = true)
    private File outputDirectory;

    @Parameter(defaultValue = "${project.build.finalName}", required = true)
    private String finalName;

    @Parameter(defaultValue = "${project.packaging}", required = true)
    private String packaging;

    @Parameter
    private String classifier;

    /**
     * Include system scoped dependencies.
     */
    @Parameter(defaultValue = "true")
    public boolean includeSystemScope;

    @Component
    private MavenProject project;

    public static PluginType PLUGIN_TYPE;

    private final Log logger = getLog();

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        logger.info("打包类型：" + packaging);
        if (packaging != null) {
            if ("jar".equalsIgnoreCase(packaging)) {
                PLUGIN_TYPE = PluginType.JAR;
                //移动数据
                ClassesMove.change(project.getArtifact().getFile());
                //处理依赖
                repackage();
                //处理loader
                try {
                    CopyLoader.start(getTargetFile());
                } catch (Exception e) {
                    throw new MojoExecutionException("write loader exception", e);
                }
            } else if ("war".equalsIgnoreCase(packaging)) {
                PLUGIN_TYPE = PluginType.WAR;
                //默认就是war了
            }
        } else {
            throw new MojoExecutionException("打包方式不是JAR或者WAR类型");
        }

    }

    private void repackage() throws MojoExecutionException, MojoFailureException {
        try {
            File sourceFile = project.getArtifact().getFile();
            Repackager repackager = new Repackager(sourceFile, logger, mainClass);
            File target = getTargetFile();
            Set<Artifact> artifacts = project.getArtifacts();
            Set<Artifact> packartifacts = new HashSet<>();
            if (!includeSystemScope) { //如果设置不打包scope system 包 则跳过
                //logger.info("此次打包的includeSystemScope设置是" + includeSystemScope);
                for (Artifact a : artifacts) {
                    if (a.getScope().equals(Artifact.SCOPE_SYSTEM)) {
                        continue;
                    }
                    packartifacts.add(a);
                }
            } else {
                packartifacts = artifacts;
            }
            Libraries libraries = new ArtifactsLibraries(packartifacts, Collections.emptyList(), getLog());
            repackager.repackage(target, libraries);
        } catch (Exception ex) {
            throw new MojoExecutionException(ex.getMessage(), ex);
        }
    }

    private File getTargetFile() {
        String classifier = (this.classifier != null ? this.classifier.trim() : "");
        if (classifier.length() > 0 && !classifier.startsWith("-")) {
            classifier = "-" + classifier;
        }
        if (!this.outputDirectory.exists()) {
            this.outputDirectory.mkdirs();
        }
        String name = this.finalName + classifier + "." + this.project.getArtifact().getArtifactHandler().getExtension();
        return new File(this.outputDirectory, name);
    }
}