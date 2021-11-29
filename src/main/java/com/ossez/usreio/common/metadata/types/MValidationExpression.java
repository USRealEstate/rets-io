package com.ossez.usreio.common.metadata.types;

import java.util.Map;

import com.ossez.usreio.common.metadata.AttrType;
import com.ossez.usreio.common.metadata.MetaObject;
import com.ossez.usreio.common.metadata.MetadataType;
import com.ossez.usreio.common.metadata.attrib.AttrEnum;

public class MValidationExpression extends MetaObject {

	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String VALIDATIONEXPRESSIONID = "ValidationExpressionID";
	public static final String VALIDATIONEXPRESSIONTYPE = "ValidationExpressionType";
	public static final String VALUE = "Value";
	private static final String[] VALIDATIONEXPRESSIONTYPES = "ACCEPT,REJECT,SET".split(",");
	private static final AttrType sExpressionType = new AttrEnum(VALIDATIONEXPRESSIONTYPES);

	public MValidationExpression() {
		this(DEFAULT_PARSING);
	}

	public MValidationExpression(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getValidationExpressionID() {
		return getStringAttribute(VALIDATIONEXPRESSIONID);
	}

	public String getValidationExpressionType() {
		return getStringAttribute(VALIDATIONEXPRESSIONTYPE);
	}

	public String getValue() {
		return getStringAttribute(VALUE);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return sNoChildren;
	}

	@Override
	protected String getIdAttr() {
		return VALIDATIONEXPRESSIONID;
	}

	@Override
	protected void addAttributesToMap(Map attributeMap) {
		attributeMap.put(METADATAENTRYID, sAttrMetadataEntryId);
		attributeMap.put(VALIDATIONEXPRESSIONID, sAlphanum32);
		attributeMap.put(VALIDATIONEXPRESSIONTYPE, sExpressionType);
		attributeMap.put(VALUE, sText512);
	}

}
