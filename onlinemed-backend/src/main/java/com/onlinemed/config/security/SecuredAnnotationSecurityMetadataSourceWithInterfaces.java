package com.onlinemed.config.security;

import com.onlinemed.controllers.LoginCtrl;
import org.springframework.core.GenericTypeResolver;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.annotation.AnnotationMetadataExtractor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.annotation.SecuredAnnotationSecurityMetadataSource;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * {@inheritDoc}
 */
@SuppressWarnings({"unchecked", "RawUseOfParameterizedType"})
public final class SecuredAnnotationSecurityMetadataSourceWithInterfaces extends SecuredAnnotationSecurityMetadataSource {

    private final AnnotationMetadataExtractor annotationExtractor;
    private final Class<? extends Annotation> annotationType;
    private final String CONTROLLERS_PACKAGE = LoginCtrl.class.getPackageName();
    private static final Class<Annotation>[] restAnnotationsClass;

    static {
        restAnnotationsClass = new Class[]{
                RequestMapping.class, GetMapping.class, PutMapping.class,
                PostMapping.class, DeleteMapping.class, PatchMapping.class
        };
    }


    public SecuredAnnotationSecurityMetadataSourceWithInterfaces() {
        this(new SecuredAnnotationMetadataExtractor());
    }

    public SecuredAnnotationSecurityMetadataSourceWithInterfaces(
            AnnotationMetadataExtractor annotationMetadataExtractor) {

        Assert.notNull(annotationMetadataExtractor, "annotationMetadataExtractor cannot be null");
        annotationExtractor = annotationMetadataExtractor;
        annotationType = (Class<? extends Annotation>) GenericTypeResolver
                .resolveTypeArgument(annotationExtractor.getClass(),
                        AnnotationMetadataExtractor.class);
        Assert.notNull(annotationType, () -> annotationExtractor.getClass().getName()
                + " must supply a generic parameter for AnnotationMetadataExtractor");
    }

    /**
     * Method which control state of Secured annotation.
     * If class implements any of generic interface and has @Secured() annotation,
     * then method <code>findAttributes</code> check case if method do not have a expected annotation
     * then gets the same values from the @secured annotation as the class it is in
     *
     * @param method      current checked method in targetClass
     * @param targetClass target class
     * @return collection of ConfigAttribute
     */
    @Override
    protected Collection<ConfigAttribute> findAttributes(Method method, Class<?> targetClass) {
        Collection<ConfigAttribute> attributes = super.findAttributes(method, targetClass);
        boolean isControllerPackage = targetClass.getPackageName().startsWith(CONTROLLERS_PACKAGE);
        if (isControllerPackage && attributes == null && annotationType == Secured.class && isRestMethod(method)) {
            Secured annotation = targetClass.getAnnotation(Secured.class);
            return (annotation == null) ? null : annotationExtractor.extractAttributes(annotation);
        }
        return attributes;
    }

    /**
     * The method checking whether the method is checked is of the REST type based on the annotation
     */
    protected boolean isRestMethod(Method method) {
        for (Class<Annotation> annotationsClass : restAnnotationsClass) {
            if (method.getAnnotation(annotationsClass) != null)
                return true;
        }
        return false;
    }
}

/**
 * {@inheritDoc}
 */
class SecuredAnnotationMetadataExtractor implements AnnotationMetadataExtractor<Secured> {

    /**
     * {@inheritDoc}
     */
    public Collection<ConfigAttribute> extractAttributes(Secured secured) {
        String[] attributeTokens = secured.value();
        List<ConfigAttribute> attributes = new ArrayList<>(attributeTokens.length);
        for (String token : attributeTokens) {
            attributes.add(new SecurityConfig(token));
        }
        return attributes;
    }
}
