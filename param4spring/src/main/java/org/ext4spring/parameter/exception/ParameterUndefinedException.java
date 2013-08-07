package org.ext4spring.parameter.exception;
/**
 * Thrown if a required parameter has null value
 */
public class ParameterUndefinedException extends ParameterException{

    private static final long serialVersionUID = -3706825790421580912L;

    public ParameterUndefinedException(String message) {
        super(message);
    }


}
