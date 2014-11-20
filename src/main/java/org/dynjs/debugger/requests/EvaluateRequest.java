package org.dynjs.debugger.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Bob McWhirter
 */
@JsonIgnoreProperties("maxStringLength")
public class EvaluateRequest extends AbstractRequest<EvaluateResponse> {


    private String expr;
    private boolean global;

    public EvaluateRequest() {
        super("evaluate");
    }

    public void setExpression(String expr) {
        this.expr = expr;
    }

    public String getExpression() {
        return this.expr;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public boolean isGlobal() {
        return this.global;
    }


}

