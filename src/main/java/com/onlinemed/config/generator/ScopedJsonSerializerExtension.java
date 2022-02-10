package com.onlinemed.config.generator;

import com.blueveery.springrest2ts.extensions.ModelSerializerExtension;
import com.blueveery.springrest2ts.tsmodel.TSComplexElement;
import com.blueveery.springrest2ts.tsmodel.TSMethod;
import com.blueveery.springrest2ts.tsmodel.TSParameter;

import java.util.Collections;
import java.util.List;

/**
 * The class responsible for generating the data mapping method, returned from the REST method, on the frontend side
 */
public class ScopedJsonSerializerExtension implements ModelSerializerExtension {

    @Override
    public List<TSParameter> getImplementationSpecificParameters(TSMethod method) {
        return Collections.emptyList();
    }

    /**
     * Method that generates the serializing code
     */
    @Override
    public String generateSerializationCode(String modelVariableName) {
        return "JsonScopedSerializer.stringify(" + modelVariableName + ", new JsonScope(false, []))";
    }

    /**
     * Method that generates the deserializing code
     */
    @Override
    public String generateDeserializationCode(String modelVariableName) {
        return "JSON.parse(" + modelVariableName + ")";
    }

    @Override
    public void addComplexTypeUsage(TSComplexElement tsComplexElement) {
    }

    @Override
    public void addImplementationSpecificFields(TSComplexElement tsComplexElement) {
    }
}
