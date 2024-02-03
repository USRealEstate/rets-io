package org.realtor.rets.validation.terms;


/**
 * BooleanTerm.java Created Aug 25, 2003
 * <p>
 * <p>
 * Copyright 2003, Avantia inc.
 *
 * @author scohen
 * @version $Revision: 1.2 $
 */
public class BooleanTerm extends AbstractTerm {
    public BooleanTerm(boolean b) {
        value = new Boolean(b);
    }

    public boolean equals(Object another) {
        if (another instanceof BooleanTerm) {
            BooleanTerm otherBool = (BooleanTerm) another;
            Boolean otherValue = (Boolean) otherBool.getValue();

            return (value.equals(otherValue));
        } else {
            return false;
        }
    }
}
