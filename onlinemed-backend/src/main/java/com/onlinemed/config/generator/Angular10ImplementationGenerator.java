package com.onlinemed.config.generator;

import com.blueveery.springrest2ts.extensions.ModelSerializerExtension;
import com.blueveery.springrest2ts.implgens.Angular4ImplementationGenerator;
import com.blueveery.springrest2ts.spring.RequestMappingUtility;
import com.blueveery.springrest2ts.tsmodel.TSClass;
import com.blueveery.springrest2ts.tsmodel.TSComplexElement;
import com.blueveery.springrest2ts.tsmodel.TSField;
import com.blueveery.springrest2ts.tsmodel.TSMethod;
import com.blueveery.springrest2ts.tsmodel.TSModule;
import com.blueveery.springrest2ts.tsmodel.TSParameter;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * The class is responsible for generating the frontend result based on the defined java classes
 */
public class Angular10ImplementationGenerator extends Angular4ImplementationGenerator {

    private static final String FIELD_NAME_HTTP_SERVICE = "httpService";
    private static final String FIELD_NAME_ERROR_SERVICE = "errorHandlerService";

    private final Set<TSClass> externalImportClass = new HashSet<>();
    private final String[] implementationSpecificFieldNames;
    private final TSClass httpClass;
    private final TSClass errorHandlerClass;

    public Angular10ImplementationGenerator() {
        super(null);
        /* Override implementation set up */
        this.addExternalProjectImport("rxjs", "Subject");
        this.addExternalProjectImport("rxjs/operators", "take");
        this.addExternalProjectImport("src/environments/environment", "environment");


        this.setSerializationExtension(new ScopedJsonSerializerExtension());
        implementationSpecificFieldNames = new String[]{FIELD_NAME_HTTP_SERVICE, FIELD_NAME_ERROR_SERVICE};

        TSModule angularHttpModule = new TSModule("@angular/common/http", null, true);
        httpClass = new TSClass("HttpClient", angularHttpModule, this);

        TSModule errorHandlerModule = new TSModule("./error-handler.service", null, true);
        errorHandlerClass = new TSClass("ErrorHandlerService", errorHandlerModule, this);
        this.externalImportClass.add(errorHandlerClass);

    }

    public void write(BufferedWriter writer, TSMethod method) throws IOException {
        TSClass tsClass = (TSClass) method.getOwner();
        if (method.isConstructor()) {
            this.writeConstructorImplementation(writer, tsClass);
        } else {
            RequestMapping methodRequestMapping = RequestMappingUtility.getRequestMapping(method.getAnnotationList());
            RequestMapping classRequestMapping = RequestMappingUtility.getRequestMapping(tsClass.getAnnotationList());
            String tsPath = this.getEndpointPath(methodRequestMapping, classRequestMapping);
            tsPath = this.useUrlService ? "this.urlService.getBackendUrl() + '" + tsPath : "'" + tsPath;
            String httpMethod = methodRequestMapping.method()[0].toString();
            String requestHeadersVar = "headers";
            String requestParamsVar = "params";
            StringBuilder pathStringBuilder = new StringBuilder(tsPath);
            StringBuilder requestBodyBuilder = new StringBuilder();
            StringBuilder requestParamsBuilder = new StringBuilder();
            this.assignMethodParameters(method, requestParamsVar, pathStringBuilder, requestBodyBuilder, requestParamsBuilder);
            boolean isRequestBodyDefined = !this.isStringBuilderEmpty(requestBodyBuilder);
            boolean isRequestParamDefined = !this.isStringBuilderEmpty(requestParamsBuilder);
            writer.write(requestParamsBuilder.toString());
            boolean isJsonParsingRequired = this.isJsonParseRequired(method);
            boolean isRequestOptionRequired = isJsonParsingRequired || methodRequestMapping.produces().length > 0 && methodRequestMapping.produces()[0].contains("text");
            String contentTypeHeader = this.getContentTypeHeaderFromRequestMapping(httpMethod, methodRequestMapping, isRequestBodyDefined);
            boolean isRequestHeaderDefined = !contentTypeHeader.isEmpty();
            this.writeRequestOption(writer, requestHeadersVar, contentTypeHeader, isRequestHeaderDefined);
            String requestOptions = "";
            String requestBody = requestBodyBuilder.toString();
            String path = pathStringBuilder.toString();
            if (!requestBody.contains("id") && path.contains("{id}")) {
                path = path.replace("{id}", "' + " + requestBody + ".id.split('/')[1] + '");
            }

            requestOptions = this.composeRequestBody(requestBody, isRequestBodyDefined, requestOptions, httpMethod, isJsonParsingRequired, methodRequestMapping.consumes());
            requestOptions = this.composeRequestOptions(requestHeadersVar, requestParamsVar, isRequestParamDefined, isRequestHeaderDefined, requestOptions, isRequestOptionRequired);
            tsPath = path;
            this.writeReturnStatement(writer, httpMethod.toLowerCase(), method, isRequestOptionRequired, tsPath, requestOptions, isJsonParsingRequired);
        }

    }

    @Override
    public void addComplexTypeUsage(TSClass tsClass) {
        externalImportClass.forEach(tsClass::addScopedTypeUsage);
        super.addComplexTypeUsage(tsClass);
    }

    public Set<TSClass> getExternalImportClass() {
        return externalImportClass;
    }

    /**
     * The method responsible for defining your own imports for a given module
     *
     * @param moduleRelativePath - relative path from root folder
     * @param importClassName    - imported clas name
     */
    public void addExternalProjectImport(String moduleRelativePath, String... importClassName) {
        TSModule newModule = new TSModule(moduleRelativePath, null, true);
        Stream.of(importClassName).forEach(className -> {
            TSClass importClass = new TSClass(className, newModule, this);
            externalImportClass.add(importClass);
        });
    }

    /**
     * The method responsible for generating the method that sends requests to the server on the backend side
     */
    @Override
    protected void writeReturnStatement(BufferedWriter writer, String httpMethod, TSMethod method,
                                        boolean isRequestOptionRequired, String tsPath,
                                        String requestOptions, boolean isJsonParsingRequired) throws IOException {
        writer.write("   const subject = new Subject<" + method.getType().getName() + ">();\n");
        String sb = "    this." +
                FIELD_NAME_HTTP_SERVICE +
                "." +
                httpMethod +
                getGenericType(method, isRequestOptionRequired) + "(" +
                "environment.BASE_URL + " +
                tsPath +
                requestOptions +
                ")" +
                getParseResponseFunction(isJsonParsingRequired) +
                "\n      .subscribe(res => subject.next(res)" + defineErrorHandling() +
                "    return subject.asObservable();";

        writer.write(sb);
    }

    @Override
    protected String getParseResponseFunction(boolean isJsonResponse) {
        if (isJsonResponse) {
            ModelSerializerExtension modelSerializerExtension = this.modelSerializerExtension;
            String parseFunction = modelSerializerExtension.generateDeserializationCode("res");
            return "\n      .pipe(map(res => " + parseFunction + "), take(1))";
        } else {
            return "\n      .pipe(take(1))";
        }
    }

    /**
     * Method defining error handling on the frontend side
     */
    private String defineErrorHandling() {
        return ", error => {\n" +
                "       this." + FIELD_NAME_ERROR_SERVICE + ".handleErrors(error);\n" +
                "       subject.error(error);\n" +
                "    });\n";
    }

    @Override
    protected String[] getImplementationSpecificFieldNames() {
        return implementationSpecificFieldNames;
    }

    /**
     * @param httpMethod           - http method type
     * @param requestMapping       - requestMapping Annotation
     * @param isRequestBodyDefined - flag
     * @return a new head definition with the Content-type property set for * .ts files
     */
    @Override
    protected String getContentTypeHeaderFromRequestMapping(String httpMethod, RequestMapping requestMapping, boolean isRequestBodyDefined) {
        if (this.isPutOrPostMethod(httpMethod)) {
            String contentType = this.getContentType(requestMapping.consumes());
            return "new HttpHeaders().set('Content-type', '" + contentType + "');";
        } else {
            return "";
        }
    }

    /**
     * If the method is a constructor, previously defined additional fields are appended as arguments
     *
     * @param method - TSMethod object
     * @return - list of TSParameter object
     */
    @Override
    public List<TSParameter> getImplementationSpecificParameters(TSMethod method) {
        if (method.isConstructor()) {
            List<TSParameter> tsParameters = new ArrayList<>();
            TSParameter httpServiceParameter = new TSParameter(FIELD_NAME_HTTP_SERVICE, httpClass, method, this);
            TSParameter errorHandlerServiceParameter = new TSParameter(FIELD_NAME_ERROR_SERVICE, errorHandlerClass, method, this);
            tsParameters.add(httpServiceParameter);
            tsParameters.add(errorHandlerServiceParameter);
            return tsParameters;
        }
        return Collections.emptyList();
    }

    /**
     * The method responsible for defining new fields in the generated class
     *
     * @param tsComplexType - the element on the basis of which the field will be created
     */
    @Override
    public void addImplementationSpecificFields(TSComplexElement tsComplexType) {
        TSClass tsClass = (TSClass) tsComplexType;
        if (tsClass.getExtendsClass() == null) {
            tsClass.getTsFields().add(new TSField(FIELD_NAME_HTTP_SERVICE, tsComplexType, httpClass));
            tsClass.getTsFields().add(new TSField(FIELD_NAME_ERROR_SERVICE, tsComplexType, errorHandlerClass));
        }
    }

}
