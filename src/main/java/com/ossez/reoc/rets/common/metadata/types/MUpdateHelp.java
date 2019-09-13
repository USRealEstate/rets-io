package com.ossez.reoc.rets.common.metadata.types;

import java.util.Map;

import com.ossez.reoc.rets.common.metadata.MetaObject;
import com.ossez.reoc.rets.common.metadata.MetadataType;

public class MUpdateHelp extends MetaObject {
    public static final String METADATAENTRYID = "MetadataEntryID";
    public static final String UPDATEHELPID = "UpdateHelpID";
    public static final String VALUE = "Value";

	public MUpdateHelp() {
		this(DEFAULT_PARSING);
	}

	public MUpdateHelp(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getUpdateHelpID() {
		return getStringAttribute(UPDATEHELPID);
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
		return UPDATEHELPID;
	}

	@Override
	protected void addAttributesToMap(Map attributeMap) {
		attributeMap.put(METADATAENTRYID, sAttrMetadataEntryId);
		attributeMap.put(UPDATEHELPID, sAlphanum32);
		attributeMap.put(VALUE, sText1024);
	}

}
