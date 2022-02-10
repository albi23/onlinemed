package com.onlinemed.config.generator;

import com.blueveery.springrest2ts.filters.JavaTypeFilter;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Class implementing functionality for filtering classes that will be painted by the generator to * .ts files
 */
public class ExtendsWithInclusionJavTypeFilter implements JavaTypeFilter {

    private final Class<?> baseType;
    protected Set<Class<?>> inclusionsClass = new HashSet<>();

    /**
     * @param javaType - checked class
     * @return - true if class inherit <code>baseType</code> or is included in inclusionsClass
     */
    public boolean accept(Class javaType) {
        return this.baseType.isAssignableFrom(javaType) || inclusionsClass.contains(javaType);
    }

    /**
     * Method called during the process of printing information about the filtered class
     *
     * @param javaType - checked class
     * @return - true if class inherit <code>baseType</code> or is included in inclusionsClass
     */
    private int isAcceptable(Class<?> javaType) {
        return (this.baseType.isAssignableFrom(javaType)) ? 0 : inclusionsClass.contains(javaType) ? 1 : -1;
    }

    /**
     * Creates a ExtendsWithInclusionJavTypeFilter class object with the given base type class
     *
     * @param baseType -  definition from which class other classes must inherit
     */
    public ExtendsWithInclusionJavTypeFilter(Class<?> baseType) {
        if (baseType.isAnnotation()) {
            throw new IllegalStateException("Annotation could not be a base Type");
        } else {
            this.baseType = baseType;
        }
    }

    /**
     * @param inclusion - manually composed classes that will be subject to the mapping process
     */
    protected void registerInclusionsClassesFilter(List<Class<?>> inclusion) {
        inclusion.stream().filter(Objects::nonNull).forEach(inclusionsClass::add);
    }

    /**
     * The method responsible for printing information during the class filtering process
     *
     * @param packageClass - Class package
     * @param logger       - instance of <code>Logger</code>
     * @param indentation  -
     */
    public void explain(Class packageClass, Logger logger, String indentation) {
        switch (isAcceptable(packageClass)) {
            case 0:
                logger.info(indentation + String.format("TRUE => class %s extends type %s", packageClass.getSimpleName(), this.baseType.getSimpleName()));
                break;
            case 1:
                logger.info(indentation + String.format("TRUE => class %s is in inclusion set", packageClass.getSimpleName()));
                break;
            default:
                logger.warn(indentation + String.format("FALSE => class %s doesn't extends base type %s and is not in inclusion set", packageClass.getSimpleName(), this.baseType.getSimpleName()));
        }
    }

}
