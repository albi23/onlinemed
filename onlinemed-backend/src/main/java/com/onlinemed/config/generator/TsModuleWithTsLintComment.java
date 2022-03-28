package com.onlinemed.config.generator;

import com.blueveery.springrest2ts.tsmodel.TSModule;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;

/**
 * TSModule extension class that allows adding TsLint comments definition
 */
public class TsModuleWithTsLintComment extends TSModule {

    private final TsLintComments tsLintComments;

    public TsModuleWithTsLintComment(String name, Path moduleRelativePath, boolean isExternal,
                                     TsLintComments tsLintComments) {
        super(name, moduleRelativePath, isExternal);
        this.tsLintComments = tsLintComments;
    }

    @Override
    public void write(BufferedWriter writer) throws IOException {
        this.writeImportBlock(writer);
        this.writeTsLintComments(writer);
        writer.newLine();
        this.writeScopedElements(writer);
    }

    protected void writeTsLintComments(BufferedWriter writer) throws IOException {
        writer.write(this.tsLintComments.getComment());
        writer.newLine();
    }

}
