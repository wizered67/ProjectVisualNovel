package com.wizered67.game.conversations.xmlio;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Wrapper around XmlElement that only provides methods for getting LazyCommandParameters from it. Used to allow
 * embedding variables as parameters in Xml.
 * @author Adam Victor
 */
public class EmbeddedVariableElement {
    //todo need new pattern for specifying language since it can take expressions
    private static final Pattern EMBEDDED_VARIABLE_PATTERN_WITH_LANGUAGE = Pattern.compile("\\{(\\S+?)_(\\S+?)\\}");
    private static final Pattern EMBEDDED_VARIABLE_PATTERN_NO_LANGUAGE = Pattern.compile("\\{(\\S+?)\\}");

    private static final StringToTConverter<String> STRING_TO_STRING_CONVERTER = new StringToTConverter<String>() {
        @Override
        public String convert(String string) {
            return string;
        }
    };

    private static final StringToTConverter<Integer> STRING_TO_INT_CONVERTER = new StringToTConverter<Integer>() {
        @Override
        public Integer convert(String string) {
            return Integer.parseInt(string);
        }
    };

    private static final StringToTConverter<Float> STRING_TO_FLOAT_CONVERTER = new StringToTConverter<Float>() {
        @Override
        public Float convert(String string) {
            return Float.parseFloat(string);
        }
    };

    private static final StringToTConverter<Boolean> STRING_TO_BOOLEAN_CONVERTER = new StringToTConverter<Boolean>() {
        @Override
        public Boolean convert(String string) {
            return Boolean.parseBoolean(string);
        }
    };

    private interface StringToTConverter<T> {
        T convert(String string);
    }

    private XmlReader.Element m_element;
    public EmbeddedVariableElement(XmlReader.Element element) {
        m_element = element;
    }

    private <T> LazyCommandParameter<T> makeLazyCommandParameterFromString(String string, T defaultValue, Class<T> type, StringToTConverter<T> converter) {
        //todo figure out if <text> needs special handling
        if (string == null) {
            return new LazyConstantCommandParameter<T>(defaultValue);
        }
        Matcher matcher = EMBEDDED_VARIABLE_PATTERN_WITH_LANGUAGE.matcher(string);
        if (matcher.matches()) {
            String language = matcher.group(1);
            String expression = matcher.group(2);
            return new LazyVariableCommandParameter<T>(language, expression, type, defaultValue);
        } else {
            matcher = EMBEDDED_VARIABLE_PATTERN_NO_LANGUAGE.matcher(string);
            if (matcher.matches()) {
                String expression = matcher.group(1);
                return new LazyVariableCommandParameter<T>(null, expression, type, defaultValue);
            } else {
                return new LazyConstantCommandParameter<T>(converter.convert(string));
            }
        }
    }

    public LazyCommandParameter<String> getText() {
        return makeLazyCommandParameterFromString(m_element.getText(), null, String.class, STRING_TO_STRING_CONVERTER);
    }

    public LazyCommandParameter<String> getAttribute(String name) {
        return getAttribute(name, null);
    }

    public LazyCommandParameter<String> getAttribute(String name, String defaultValue) {
        return makeLazyCommandParameterFromString(m_element.getAttribute(name), defaultValue, String.class, STRING_TO_STRING_CONVERTER);
    }

    public LazyCommandParameter<Float> getFloatAttribute(String name) {
        return getFloatAttribute(name, null);
    }

    public LazyCommandParameter<Float> getFloatAttribute(String name, Float defaultValue) {
        return makeLazyCommandParameterFromString(m_element.getAttribute(name), defaultValue, Float.class, STRING_TO_FLOAT_CONVERTER);
    }

    public LazyCommandParameter<Integer> getIntAttribute(String name) {
        return getIntAttribute(name, null);
    }

    public LazyCommandParameter<Integer> getIntAttribute(String name, Integer defaultValue) {
        return makeLazyCommandParameterFromString(m_element.getAttribute(name), defaultValue, Integer.class, STRING_TO_INT_CONVERTER);
    }

    public LazyCommandParameter<Boolean> getBooleanAttribute(String name) {
        return getBooleanAttribute(name, null);
    }

    public LazyCommandParameter<Boolean> getBooleanAttribute(String name, Boolean defaultValue) {
        return makeLazyCommandParameterFromString(m_element.getAttribute(name), defaultValue, Boolean.class, STRING_TO_BOOLEAN_CONVERTER);
    }

    /*
    public LazyCommandParameter<String> get(String name) {
        //return m_element.get(name);
    }

    public LazyCommandParameter<String> get(String name, String defaultValue) {
        return m_element.get(name, defaultValue);
    }

    public LazyCommandParameter<Integer> getInt(String name) {
        return m_element.getInt(name);
    }

    public LazyCommandParameter<Integer> getInt(String name, int defaultValue) {
        return m_element.getInt(name, defaultValue);
    }

    public LazyCommandParameter<Float> getFloat(String name) {
        return m_element.getFloat(name);
    }

    public LazyCommandParameter<Float> getFloat(String name, float defaultValue) {
        return m_element.getFloat(name, defaultValue);
    }

    public LazyCommandParameter<Boolean> getBoolean(String name) {
        return m_element.getBoolean(name);
    }

    public LazyCommandParameter<Boolean> getBoolean(String name, boolean defaultValue) {
        return m_element.getBoolean(name, defaultValue);
    }
    */

    //Methods delegated to XmlReader.Element

    public String getName() {
        return m_element.getName();
    }

    public ObjectMap<String, String> getAttributes() {
        return m_element.getAttributes();
    }

    public int getChildCount() {
        return m_element.getChildCount();
    }

    public XmlReader.Element getChild(int index) {
        return m_element.getChild(index);
    }

    public void addChild(XmlReader.Element element) {
        m_element.addChild(element);
    }

    public void setText(String text) {
        m_element.setText(text);
    }

    public void removeChild(int index) {
        m_element.removeChild(index);
    }

    public void removeChild(XmlReader.Element child) {
        m_element.removeChild(child);
    }

    public void remove() {
        m_element.remove();
    }

    public XmlReader.Element getParent() {
        return m_element.getParent();
    }

    @Override
    public String toString() {
        return m_element.toString();
    }

    public String toString(String indent) {
        return m_element.toString(indent);
    }

    public XmlReader.Element getChildByName(String name) {
        return m_element.getChildByName(name);
    }

    public XmlReader.Element getChildByNameRecursive(String name) {
        return m_element.getChildByNameRecursive(name);
    }

    public Array<XmlReader.Element> getChildrenByName(String name) {
        return m_element.getChildrenByName(name);
    }

    public Array<XmlReader.Element> getChildrenByNameRecursively(String name) {
        return m_element.getChildrenByNameRecursively(name);
    }
}
