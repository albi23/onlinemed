package com.onlinemed.config.generator;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Class that allows you to define your own comments for TsLint
 */
public class TsLintComments {

    public static final TsLintComments ALIGN = new TsLintComments("align ");/*  - Enforces vertical alignment. **/
    public static final TsLintComments EOFLINE = new TsLintComments("eofline ");/*  - Ensures the file ends with a newline. **/
    public static final TsLintComments MAX_LINE_LENGTH = new TsLintComments("max-line-length ");/*  - Requires lines to be under a certain max length. **/

    private final TsLintComments[] tsRoles;
    public String commentTemple = "/* tslint:disable %s  */";
    private String tsRole;

    public TsLintComments(String tsRole) {
        this.tsRole = tsRole;
        this.tsRoles = new TsLintComments[0];
    }

    public TsLintComments(String tsRole, TsLintComments... tsRoles) {
        this.tsRole = tsRole;
        this.tsRoles = tsRoles;
    }

    public TsLintComments(TsLintComments... tsRoles) {
        this.tsRoles = tsRoles;
    }

    public String getComment() {
        StringBuilder sb = new StringBuilder();
        if (tsRole != null && tsRole.length() > 0) sb.append(tsRole);
        Stream.of(tsRoles)
                .filter(r -> Objects.nonNull(r) && Objects.nonNull(r.getTsRole()) && r.getTsRole().length() > 0)
                .forEach(r -> sb.append(" ").append(r.getTsRole()));
        return String.format(commentTemple, sb.toString());
    }


    public void setCommentTemple(String commentTemple) {
        this.commentTemple = commentTemple;
    }

    public String getTsRole() {
        return tsRole;
    }
}
