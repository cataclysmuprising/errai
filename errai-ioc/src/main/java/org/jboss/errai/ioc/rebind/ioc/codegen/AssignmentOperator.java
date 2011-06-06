package org.jboss.errai.ioc.rebind.ioc.codegen;

import org.jboss.errai.ioc.rebind.ioc.codegen.meta.MetaClass;

/**
 * @author Mike Brock <cbrock@redhat.com>
 * @author Christian Sadilek <csadilek@redhat.com>
 */
public enum AssignmentOperator implements Operator {
    Assignment("=", 0),
    PreIncrementAssign("+=", 0, CharSequence.class, Number.class),
    PostIncrementAssign("=+", 0, CharSequence.class, Number.class),
    PreDecrementAssign("-=", 0, Number.class),
    PostDecrementAssign("=-", 0, Number.class);

    private final Operator operator;
    
    AssignmentOperator(String canonicalString, int operatorPrecedence, Class<?>... constraints) {
        operator = new OperatorImpl(canonicalString, operatorPrecedence, constraints);
    }

    public String getCanonicalString() {
        return operator.getCanonicalString();
    }

    public int getOperatorPrecedence() {
        return operator.getOperatorPrecedence();
    }

    public boolean isHigherPrecedenceThan(Operator op) {
        return op.getOperatorPrecedence() < getOperatorPrecedence();
    }

    public boolean isEqualOrHigherPrecedenceThan(Operator op) {
        return op.getOperatorPrecedence() <= getOperatorPrecedence();
    }

    public boolean canBeApplied(MetaClass clazz) {
        return operator.canBeApplied(clazz);
    }
    
    public void assertCanBeApplied(MetaClass clazz) {
        operator.assertCanBeApplied(clazz);
    }
}
