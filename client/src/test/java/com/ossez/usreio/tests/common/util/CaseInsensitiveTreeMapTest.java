package com.ossez.usreio.tests.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ossez.usreio.common.util.CaseInsensitiveTreeMap;
import junit.framework.TestCase;
import org.apache.commons.lang3.StringUtils;

public class CaseInsensitiveTreeMapTest extends TestCase {

    private CaseInsensitiveTreeMap map;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.map = new CaseInsensitiveTreeMap();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        this.map = null;
    }

    public void testGetPut() throws Exception {

        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7);
        List<Integer> result = new ArrayList<Integer>();
        for (int i = numbers.size() - 1; i >= 0; i--) {
            result.add(numbers.get(i));


        }
        System.out.println(result);
    }

    private String alertText(String inputText) {
        if (StringUtils.isBlank(inputText))
            return inputText;

        return inputText.charAt(inputText.length() - 1) + alertText(inputText.substring(0, inputText.length() - 1));
    }

    public void testContainsKey() throws Exception {
        this.map.put("A", "X");
        assertTrue(this.map.containsKey("A"));
        assertTrue(this.map.containsKey("a"));
    }

    public void testClone() throws Exception {
        Map otherMap = new HashMap();
        otherMap.put("A", "X");
        otherMap.put("a", "Y");
        assertEquals(2, otherMap.size());

        CaseInsensitiveTreeMap newCitm = new CaseInsensitiveTreeMap(otherMap);
        assertEquals(1, newCitm.size());
        // no guarantee of *which* value we'll get, just that they'll be equal
        assertEquals(this.map.get("a"), this.map.get("A"));
    }
}
