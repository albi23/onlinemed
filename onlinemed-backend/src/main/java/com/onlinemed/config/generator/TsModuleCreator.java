package com.onlinemed.config.generator;

import com.blueveery.springrest2ts.converters.JavaPackageToTsModuleConverter;
import com.blueveery.springrest2ts.converters.TsModuleCreatorConverter;
import com.blueveery.springrest2ts.tsmodel.TSModule;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TsModuleCreator extends TsModuleCreatorConverter implements JavaPackageToTsModuleConverter {

    protected Map<String, TSModule> packagesMap = new HashMap<>();
    protected Map<String,TsLintComments> packageOnTsLinComment = new HashMap<>();
    protected int numberOfSubPackages;

    public TsModuleCreator(int numberOfSubPackages) {
        super(numberOfSubPackages);
        this.numberOfSubPackages = numberOfSubPackages;
    }


    /**
     * Method which add tsLint comment in module to which it belongs, after imports block
     */
    public void registerTsLintCommentInPackage(String packageName, TsLintComments tsLintComments) {
        this.packageOnTsLinComment.put(packageName, tsLintComments);
    }

    /**
     * The method responsible for generating modules on the frontend side
     */
    @Override
    public TSModule getTsModule(Class javaType) {
        String packageName = javaType.getPackage().getName();
        TSModule tsModule = packagesMap.get(packageName);
        if (tsModule == null) {
            String[] subPackages = packageName.split("\\.");
            StringBuilder moduleName = new StringBuilder();
            for (int i = Math.max(subPackages.length - numberOfSubPackages, 0); i < subPackages.length; i++) {
                moduleName.append(subPackages[i]);
                if (i + 1 < subPackages.length) {
                    moduleName.append("-");
                }
            }
            TsLintComments tsLint = packageOnTsLinComment.get(packageName);
            if (Objects.nonNull(tsLint)) {
                tsModule = new TsModuleWithTsLintComment(moduleName.toString(), Paths.get("."), false, tsLint);
            } else {
                tsModule = new TSModule(moduleName.toString(), Paths.get("."), false);
            }
            packagesMap.put(packageName, tsModule);
            getTsModules().add(tsModule);
        }
        return tsModule;
    }
}
