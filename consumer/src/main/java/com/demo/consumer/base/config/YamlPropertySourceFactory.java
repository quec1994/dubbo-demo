package com.demo.consumer.base.config;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class YamlPropertySourceFactory implements PropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        return YamlPropertySourceLoaderUtils.loadPropertySource(name, resource.getResource());
    }

    public static class YamlPropertySourceLoaderUtils {
        private static final YamlPropertySourceLoader SOURCE_LOADER = new YamlPropertySourceLoader();

        public static PropertySource<?> loadPropertySource(String name, Resource resource) throws IOException {
            name = checkName(name, resource);
            boolean canProcess = canProcess(resource);
            if (!canProcess) {
                throw new IllegalArgumentException("Could not load properties from " + resource);
            }
            return load(name, resource);
        }

        private static String checkName(String name, Resource resource) {
            if (StringUtils.hasText(name)) {
                return name;
            }
            return getNameForResource(resource);
        }

        private static String getNameForResource(Resource resource) {
            String name = resource.getDescription();
            if (!StringUtils.hasText(name)) {
                name = resource.getClass().getSimpleName() + "@" + System.identityHashCode(resource);
            }
            return name;
        }

        private static boolean canProcess(Resource resource) {
            String filename = resource.getFilename();
            if (Objects.isNull(filename)) {
                return false;
            }
            boolean canProcess = false;
            for (String fileExtension : SOURCE_LOADER.getFileExtensions()) {
                if (filename.endsWith(fileExtension)) {
                    canProcess = true;
                    break;
                }
            }
            return canProcess;
        }

        private static PropertySource<?> load(String name, Resource resource) throws IOException {
            List<PropertySource<?>> propertySourceList = SOURCE_LOADER.load(name, resource);
            if (CollectionUtils.isEmpty(propertySourceList)) {
                throw new IllegalArgumentException("Can not load properties from " + resource);
            }
            return propertySourceList.get(0);
        }

    }

}
