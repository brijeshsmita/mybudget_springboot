package br.com.gestao.financeira.pessoal.infra.about;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.Logger;

@Startup
@Lock(LockType.READ)
@Singleton
public class ModuleDependencyInfo implements Serializable {

    private final Map<String,Object> dependenciesInfo =new HashMap<>();

    @Inject
    private Logger logger;

    @Produces
    @Named("meussaldos")
    public Map<String, Object> getDependenciesInfo() {
        return Collections.unmodifiableMap(dependenciesInfo);
    }

    @PostConstruct
    public void onInit(){
        this.dependenciesInfo.put("main", loadMainAppDependencyInfo());
        this.dependenciesInfo.put("git", loadMainAppGitDependencyInfo());
    }

    private Map<Object, Object> loadMainAppDependencyInfo() {
        return loadPropertiesFromFirstAvailableFile(
                "META-INF/maven/br.com.meussaldos/meussaldos/pom.properties",
                "/META-INF/maven/br.com.meussaldos/meussaldos/pom.properties");
    }
    private Map<Object, Object> loadMainAppGitDependencyInfo() {
        return loadPropertiesFromFirstAvailableFile(
                "META-INF/git.properties",
                "/META-INF/git.properties");
    }

    private Map<Object,Object> loadPropertiesFromFirstAvailableFile(String... path){
        return firstNonNull(path).map(this::load).orElse(Collections.emptyMap());
    }

    private Map<Object, Object> load(InputStream is) {
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            logger.error(e);
        }
        return Collections.unmodifiableMap(properties);
    }

    private Optional<InputStream> firstNonNull(String... path) {
        return Stream.of(path).map(ModuleDependencyInfo.class::getResourceAsStream).filter(Objects::nonNull)
                .findFirst();
    }

}
