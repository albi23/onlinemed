package com.onlinemed.config.generator;

import com.blueveery.core.ctrls.BaseCtrl;
import com.blueveery.core.model.BaseEntity;
import com.blueveery.springrest2ts.Rest2tsGenerator;
import com.blueveery.springrest2ts.converters.JacksonObjectMapper;
import com.blueveery.springrest2ts.converters.ModelClassesToTsInterfacesConverter;
import com.blueveery.springrest2ts.converters.SpringRestToTsConverter;
import com.blueveery.springrest2ts.converters.TypeMapper;
import com.blueveery.springrest2ts.filters.ExtendsJavaTypeFilter;
import com.blueveery.springrest2ts.implgens.Angular4ImplementationGenerator;
import com.blueveery.springrest2ts.tsmodel.TSArray;
import com.blueveery.springrest2ts.tsmodel.TSSimpleType;
import com.blueveery.springrest2ts.tsmodel.TSType;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.onlinemed.controllers.LoginCtrl;
import com.onlinemed.model.BaseObject;
import com.onlinemed.model.dto.ErrorMessage;
import org.springframework.core.io.InputStreamSource;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation and configuration of the library responsible for
 * generating the class model for the frontend and REST methods
 */
public class SpringREST2tsGeneratorMain {

    public static void main(String[] args) throws IOException {
        Rest2tsGenerator tsGenerator = new Rest2tsGenerator();
        TsModuleCreator javaPackageToTsModuleConverter = new TsModuleCreator(2);
        TsLintComments tsLintComments = new TsLintComments(TsLintComments.MAX_LINE_LENGTH, TsLintComments.ALIGN, TsLintComments.EOFLINE);
        javaPackageToTsModuleConverter.registerTsLintCommentInPackage(LoginCtrl.class.getPackage().getName(), tsLintComments);
        tsGenerator.setJavaPackageToTsModuleConverter(javaPackageToTsModuleConverter);

        Map<Class, TSType> customTypeMapping = tsGenerator.getCustomTypeMapping();
        customTypeMapping.put(UUID.class, TypeMapper.tsString);
        customTypeMapping.put(BigInteger.class, TypeMapper.tsNumber);
        customTypeMapping.put(LocalDateTime.class, TypeMapper.tsNumber);
        customTypeMapping.put(Locale.class, TypeMapper.tsString);
        customTypeMapping.put(LocalDate.class, new TSArray(TypeMapper.tsNumber));
        customTypeMapping.put(InputStreamSource.class, new TSSimpleType("Blob"));
        customTypeMapping.put(Timestamp.class, TypeMapper.tsString);
        customTypeMapping.put(Object.class, TypeMapper.tsAny);


        ExtendsWithInclusionJavTypeFilter javaTypeFilter = new ExtendsWithInclusionJavTypeFilter(BaseEntity.class);
        javaTypeFilter.registerInclusionsClassesFilter(getListOfClassesInPackage(ErrorMessage.class.getPackage().getName()));
        tsGenerator.setModelClassesCondition(javaTypeFilter);
        tsGenerator.setRestClassesCondition(new ExtendsJavaTypeFilter(BaseCtrl.class));

        JacksonObjectMapper jacksonObjectMapper = new JacksonObjectMapper();
        jacksonObjectMapper.setFieldsVisibility(JsonAutoDetect.Visibility.ANY);
        ModelClassesToTsInterfacesConverter modelClassesConverter = new ModelClassesToTsInterfacesConverter(jacksonObjectMapper);
        tsGenerator.setModelClassesConverter(modelClassesConverter);

        Angular4ImplementationGenerator implementationGenerator = new Angular10ImplementationGenerator();
        /* Customize project own imports */
        Angular10ImplementationGenerator extendedImpl = (Angular10ImplementationGenerator) implementationGenerator;
        extendedImpl.addExternalProjectImport("./JsonParser", "JsonScopedSerializer");
        extendedImpl.addExternalProjectImport("./jsonScope", "JsonScope");

        SpringRestToTsConverter restClassesConverter = new SpringRestToTsConverterWithIgnore(implementationGenerator);
        tsGenerator.setRestClassesConverter(restClassesConverter);

        Set<String> packagesNames = new HashSet<>();
        packagesNames.add(BaseCtrl.class.getPackage().getName());
        packagesNames.add(LoginCtrl.class.getPackage().getName());
        packagesNames.add(BaseEntity.class.getPackage().getName());
        packagesNames.add(BaseObject.class.getPackage().getName());

        Path outputDir = Path.of("./target/tsfiles");
        tsGenerator.generate(packagesNames, outputDir);

    }

    /**
     * The method returns all class definitions in the specified package
     *
     * @param packageName - specified package
     * @return - kist of classes in packge
     */
    private static List<Class<?>> getListOfClassesInPackage(final String packageName) {
        final String casesPath = packageName.replace(".", "/");
        File[] files = new File(Objects.requireNonNull(SpringREST2tsGeneratorMain.class.getClassLoader().getResource(casesPath)).getPath()).listFiles();
        return (files == null) ? Collections.emptyList() :
                Stream.of(files).map(File::getName).
                        filter(name -> name.endsWith(".class"))
                        .map(fileName -> {
                            try {
                                return Class.forName(packageName + '.' + fileName.substring(0, fileName.length() - 6));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                                return null;
                            }
                        }).collect(Collectors.toList());
    }

}
