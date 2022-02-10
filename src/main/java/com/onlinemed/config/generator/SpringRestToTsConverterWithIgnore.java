package com.onlinemed.config.generator;

import com.blueveery.springrest2ts.converters.SpringRestToTsConverter;
import com.blueveery.springrest2ts.implgens.ImplementationGenerator;
import com.blueveery.springrest2ts.naming.ClassNameMapper;

import java.lang.reflect.Method;

/**
 * SpringRestToTsConverter extension class providing the ability to override
 * methods based on the @GeneratorIgnore annotation
 */
public class SpringRestToTsConverterWithIgnore extends SpringRestToTsConverter {


    public SpringRestToTsConverterWithIgnore(ImplementationGenerator implementationGenerator) {
        super(implementationGenerator);
    }

    public SpringRestToTsConverterWithIgnore(ImplementationGenerator implementationGenerator, ClassNameMapper classNameMapper) {
        super(implementationGenerator, classNameMapper);
    }

    protected boolean isRestMethod(Method method) {
        if(method.getAnnotation(GeneratorIgnore.class) != null) return false;
        return super.isRestMethod(method);
    }
}
